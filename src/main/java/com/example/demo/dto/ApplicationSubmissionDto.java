package com.example.demo.dto;

// *** DELETED: jakarta.validation.constraints.NotNull ***
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationSubmissionDto {
    // @NotNull(message = "Job ID must be provided for submission.")
    private Long jobId;
}