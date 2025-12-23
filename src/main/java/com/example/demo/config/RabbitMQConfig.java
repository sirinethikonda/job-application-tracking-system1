package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {
    
    @Bean
    public TopicExchange appExchange() {
        // Requirement 7: Topic Exchange allows flexible routing
        return new TopicExchange("application.events", true, false);
    }

    @Bean
    public Queue candidateQueue() { 
        return new Queue("email.candidate", true); 
    }

    @Bean
    public Binding bindCandidate(Queue candidateQueue, TopicExchange appExchange) {
        // FIX: Use wildcard '#' to catch all application stage changes
        return BindingBuilder.bind(candidateQueue).to(appExchange).with("application.#");
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    
}
}