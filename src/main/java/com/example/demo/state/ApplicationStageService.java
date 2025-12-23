package com.example.demo.state;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ApplicationStageService {
    private final Map<String, Set<String>> allowed = new HashMap<>();

    public ApplicationStageService() {
        // Allowing flexible transitions for the demo
        allowed.put("APPLIED", Set.of("SCREENING", "INTERVIEW", "REJECTED"));
        allowed.put("SCREENING", Set.of("INTERVIEW", "REJECTED"));
        allowed.put("INTERVIEW", Set.of("OFFER", "REJECTED"));
        allowed.put("OFFER", Set.of("HIRED", "REJECTED"));
        allowed.put("HIRED", Set.of());
        allowed.put("REJECTED", Set.of());
    }

    public void validate(String current, String target) {
        if (current.equals(target)) return;
        Set<String> next = allowed.getOrDefault(current, Collections.emptySet());
        if (!next.contains(target)) {
            throw new IllegalStateException("Invalid transition: " + current + " -> " + target);
        }
    }
}