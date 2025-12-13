package com.example.demo.dto;

import com.example.demo.application.ApplicationStage;
// *** DELETED: jakarta.validation.constraints.NotNull ***
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StageChangeDto {
    
    // @NotNull(message = "The new stage must be provided.")
    private ApplicationStage newStage;
}