package com.example.demo.job;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs") // FIX 1: Added /api to match your Postman URL
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> listJobs() {
        return ResponseEntity.ok(jobService.listAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RECRUITER')")
    public ResponseEntity<?> createJob(@RequestBody Map<String,String> body, Authentication auth) {
        // Principal is usually the email string from your JwtAuthFilter
        String principal = (String) auth.getPrincipal();

        // FIX: Instead of casting details, we trust the logic in your Service
        // or manually extract claims if your filter puts them in details.
        // For now, let's use a safe check.
        String companyId = null;
        if (auth.getDetails() instanceof Map) {
            var details = (Map<String, Object>) auth.getDetails();
            companyId = String.valueOf(details.get("companyId"));
        }

        // Call service to save to MySQL
        Job j = jobService.createJob(companyId, body.get("title"), body.get("description"), principal);
        return ResponseEntity.status(201).body(j);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RECRUITER')") // FIX 3: Changed from hasRole
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}