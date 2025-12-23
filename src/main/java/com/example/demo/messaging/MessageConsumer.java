package com.example.demo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class MessageConsumer {

    @Autowired private JavaMailSender mailSender;

    @RabbitListener(queues = "email.candidate")
    public void handleCandidateEmail(Map<String, Object> payload) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo((String) payload.get("email"));
            message.setSubject("ATS Application Update");
            message.setText("Your application stage has changed to: " + payload.get("newStage"));
            mailSender.send(message);
            System.out.println("Actual email sent to candidate for App: " + payload.get("appId"));
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }
}