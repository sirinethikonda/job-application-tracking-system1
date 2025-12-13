package com.example.demo.job;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    private String id;

    @Column(name = "company_id")
    private String companyId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status; // OPEN, CLOSED

    private String createdBy;
}
