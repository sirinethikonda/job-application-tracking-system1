package com.example.demo.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "email.candidate")
    public void candidateQueue(String payload) throws Exception {
        System.out.println("[consumer] candidate queue received: " + payload);
    }

    @RabbitListener(queues = "email.recruiter")
    public void recruiterQueue(String payload) throws Exception {
        System.out.println("[consumer] recruiter queue received: " + payload);
    }
}
