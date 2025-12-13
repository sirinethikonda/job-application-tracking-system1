package com.example.demo.state;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationStageService {
    private final Map<String, Set<String>> allowed = new HashMap<>();

    public ApplicationStageService() {
        allowed.put("APPLIED", Set.of("SCREENING","REJECTED"));
        allowed.put("SCREENING", Set.of("INTERVIEW","REJECTED"));
        allowed.put("INTERVIEW", Set.of("OFFER","REJECTED"));
        allowed.put("OFFER", Set.of("HIRED","REJECTED"));
        allowed.put("HIRED", Set.of());
        allowed.put("REJECTED", Set.of());
    }

    public void validate(String current, String target) {
        if (current == null) throw new IllegalStateException("Current stage is null");
        if (target == null) throw new IllegalStateException("Target stage is null");
        if (current.equals(target)) return;
        if ("REJECTED".equals(target)) return;
        Set<String> next = allowed.getOrDefault(current, Collections.emptySet());
        if (!next.contains(target)) {
            throw new IllegalStateException("Invalid transition: " + current + " -> " + target);
        }
    }
}
