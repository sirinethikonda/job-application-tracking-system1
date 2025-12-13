package com.example.demo.dto;

import com.example.demo.application.Application;
import com.example.demo.application.ApplicationStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto implements Serializable {
    // This DTO is sent over RabbitMQ for asynchronous processing
    private Long applicationId;
    private String candidateEmail;
    private String recruiterEmail;
    private String jobTitle;
    private Application oldStage;
    private Application newStage;
}