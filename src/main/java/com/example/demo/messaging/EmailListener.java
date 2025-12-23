package com.example.demo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class EmailListener {

    @Autowired
    private JavaMailSender mailSender;

    // This method automatically runs when a message hits the queue
    @RabbitListener(queues = "email.candidate") 
    public void processEmail(Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String stage = (String) payload.get("newStage");

        System.out.println("LOG: Received message from RabbitMQ for: " + email);

        if ("HIRED".equals(stage)) {
            sendEmail(email, "Congratulations!", "You have been officially HIRED!");
        } else {
            sendEmail(email, "Status Update", "Your application status is now: " + stage);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("SUCCESS: Email sent to " + to);
        } catch (Exception e) {
            System.err.println("ERROR: Could not send email: " + e.getMessage());
        }
    }
}