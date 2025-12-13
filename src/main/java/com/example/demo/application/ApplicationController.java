package com.example.demo.application;

import com.example.demo.state.ApplicationStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationHistoryRepository historyRepository;

    @Autowired
    private ApplicationStageService stageService;

    @Autowired
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Autowired
    private com.example.demo.job.JobRepository jobRepository;

    @PostMapping("/jobs/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> apply(@PathVariable String jobId, Authentication auth) {
        String candidateId = (String) auth.getPrincipal();
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId))
            return ResponseEntity.badRequest().body("Already applied");

        var jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) return ResponseEntity.badRequest().body("Job not found");

        Application app = new Application();
        app.setId(UUID.randomUUID().toString());
        app.setJobId(jobId);
        app.setCandidateId(candidateId);
        app.setStage("APPLIED");
        app.setAppliedAt(Timestamp.from(Instant.now()));
        app.setUpdatedAt(Timestamp.from(Instant.now()));
        applicationRepository.save(app);

        ApplicationHistory hist = new ApplicationHistory();
        hist.setId(UUID.randomUUID().toString());
        hist.setApplicationId(app.getId());
        hist.setPreviousStage(null);
        hist.setNewStage("APPLIED");
        hist.setChangedBy(candidateId);
        hist.setCreatedAt(Timestamp.from(Instant.now()));
        historyRepository.save(hist);

        Map<String,Object> payload = new HashMap<>();
        payload.put("applicationId", app.getId());
        payload.put("jobId", jobId);
        payload.put("candidateId", candidateId);
        payload.put("stage", "APPLIED");
        rabbitTemplate.convertAndSend("application.events", "APPLICATION_CREATED", payload);

        return ResponseEntity.status(201).body(app);
    }

    @GetMapping("/me/applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<Application>> myApplications(Authentication auth) {
        String candidateId = (String) auth.getPrincipal();
        return ResponseEntity.ok(applicationRepository.findByCandidateId(candidateId));
    }

    @GetMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<Application>> listForJob(@PathVariable String jobId,
                                                        @RequestParam(required=false) String stage) {
        if (stage == null) return ResponseEntity.ok(applicationRepository.findByJobId(jobId));
        return ResponseEntity.ok(applicationRepository.findByJobIdAndStage(jobId, stage));
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<?> getApplication(@PathVariable String id, Authentication auth) {
        var appOpt = applicationRepository.findById(id);
        if (appOpt.isEmpty()) return ResponseEntity.notFound().build();

        Application app = appOpt.get();
        @SuppressWarnings("unchecked")
        var details = (Map<String,String>) auth.getDetails();
        String role = details.getOrDefault("role","");
        String userId = (String) auth.getPrincipal();
        if ("CANDIDATE".equals(role) && !app.getCandidateId().equals(userId)) return ResponseEntity.status(403).body("Forbidden");
        return ResponseEntity.ok(app);
    }

    @PatchMapping("/applications/{id}/stage")
    @PreAuthorize("hasAnyRole('RECRUITER','HIRING_MANAGER')")
    public ResponseEntity<?> changeStage(@PathVariable String id, @RequestBody Map<String,String> body, Authentication auth) {
        String target = body.get("targetStage");
        String reason = body.get("reason");
        String changedBy = (String) auth.getPrincipal();

        var appOpt = applicationRepository.findById(id);
        if (appOpt.isEmpty()) return ResponseEntity.notFound().build();
        Application app = appOpt.get();

        // Validate allowed transition
        try {
            stageService.validate(app.getStage(), target);
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        String prev = app.getStage();
        app.setStage(target);
        app.setUpdatedAt(Timestamp.from(Instant.now()));
        applicationRepository.save(app);

        ApplicationHistory hist = new ApplicationHistory();
        hist.setId(UUID.randomUUID().toString());
        hist.setApplicationId(id);
        hist.setPreviousStage(prev);
        hist.setNewStage(target);
        hist.setChangedBy(changedBy);
        hist.setReason(reason);
        hist.setCreatedAt(Timestamp.from(Instant.now()));
        historyRepository.save(hist);

        Map<String,Object> payload = new HashMap<>();
        payload.put("applicationId", id);
        payload.put("previous", prev);
        payload.put("new", target);
        payload.put("changedBy", changedBy);
        rocketPublish(payload, "APPLICATION_STAGE_CHANGED");

        return ResponseEntity.ok(app);
    }

    // helper
    private void rocketPublish(Map<String,Object> payload, String routingKey) {
        rabbitTemplate.convertAndSend("application.events", routingKey, payload);
    }
}
