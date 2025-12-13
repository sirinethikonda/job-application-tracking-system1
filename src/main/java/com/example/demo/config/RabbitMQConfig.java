package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange("application.events", true, false);
    }
    @Bean
    public Queue candidateQueue() { return new Queue("email.candidate", true); }
    @Bean
    public Queue recruiterQueue() { return new Queue("email.recruiter", true); }

    @Bean
    public Binding bindCandidate(Queue candidateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(candidateQueue).to(exchange).with("APPLICATION_CREATED");
    }

    @Bean
    public Binding bindRecruiter(Queue recruiterQueue, TopicExchange exchange) {
        return BindingBuilder.bind(recruiterQueue).to(exchange).with("APPLICATION_CREATED");
    }
}
