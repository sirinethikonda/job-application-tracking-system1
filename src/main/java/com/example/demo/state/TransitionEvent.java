package com.example.demo.state;

import lombok.Data;

@Data
public class TransitionEvent {
    private String applicationId;
    private String previous;
    private String next;
}
