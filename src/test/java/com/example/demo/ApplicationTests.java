package com.example.demo;

import com.example.demo.state.ApplicationStageService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired private ApplicationStageService stageService;

    @Test
    void testInvalidTransition() {
        // Prove that Requirement 1 (Prevent invalid transitions) is MET
        assertThrows(IllegalStateException.class, () -> {
            stageService.validate("APPLIED", "HIRED"); // Cannot skip Screening/Interview
        });
    }

    @Test
    void testValidTransition() {
        assertDoesNotThrow(() -> {
            stageService.validate("APPLIED", "SCREENING");
        });
    }
}