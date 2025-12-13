package com.example.demo.job;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> listJobs() {
        return ResponseEntity.ok(jobService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable String id) {
        Job j = jobService.get(id);
        if (j==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(j);
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createJob(@RequestBody Map<String,String> body, Authentication auth) {
        var principal = (String) auth.getPrincipal();
        var details = (java.util.Map<String,String>) auth.getDetails();
        String companyId = details.get("companyId");
        Job j = jobService.createJob(companyId, body.get("title"), body.get("description"), principal);
        return ResponseEntity.status(201).body(j);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
