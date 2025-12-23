package com.example.demo.application;

import com.example.demo.state.ApplicationStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private ApplicationHistoryRepository historyRepository;
    @Autowired private ApplicationStageService stageService;
    @Autowired private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    // REQUIREMENT 5: POST Application (Transaction: Application + History)
    @PostMapping("/applications")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    @Transactional
    public ResponseEntity<?> apply(@RequestBody Map<String, String> body, Authentication auth) {
        Application app = new Application();
        app.setId(UUID.randomUUID().toString());
        app.setJobId(body.get("jobId"));
        app.setCandidateEmail(auth.getName());
        app.setResumeUrl(body.get("resumeUrl"));
        app.setStage("APPLIED");
        app.setCreatedAt(Timestamp.from(Instant.now()));
        applicationRepository.save(app);

        // Audit Trail entry
        ApplicationHistory hist = new ApplicationHistory();
        hist.setId(UUID.randomUUID().toString());
        hist.setApplicationId(app.getId());
        hist.setNewStage("APPLIED");
        hist.setChangedBy(auth.getName());
        hist.setCreatedAt(Timestamp.from(Instant.now()));
        historyRepository.save(hist);

        return ResponseEntity.status(201).body(app);
    }

 // REQUIREMENT 1 & 7: PATCH Stage (State Machine + Messaging)
    @PatchMapping("/applications/{id}/stage")
    @PreAuthorize("hasAuthority('RECRUITER')")
    @Transactional
    public ResponseEntity<?> changeStage(@PathVariable String id, @RequestBody Map<String,String> body, Authentication auth) {
        String target = body.get("targetStage");
        
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Validation using State Machine (Requirement 1)
        stageService.validate(app.getStage(), target);

        String prev = app.getStage();
        app.setStage(target);
        app.setUpdatedAt(Timestamp.from(Instant.now()));
        applicationRepository.save(app);

        // Requirement 5: Update Audit History in the same Transaction
        ApplicationHistory hist = new ApplicationHistory();
        hist.setId(UUID.randomUUID().toString());
        hist.setApplicationId(id);
        hist.setPreviousStage(prev);
        hist.setNewStage(target);
        hist.setChangedBy(auth.getName());
        hist.setCreatedAt(Timestamp.from(Instant.now()));
        historyRepository.save(hist);

        // REQUIREMENT 7: Send Async Message
        Map<String,Object> msg = new HashMap<>();
        msg.put("appId", id);
        msg.put("newStage", target);
        msg.put("email", app.getCandidateEmail());

        // FIX: Routing Key must match "application.#" from your config
        rabbitTemplate.convertAndSend("application.events", "application.stage.changed", msg);

        return ResponseEntity.ok(app);
    }
}