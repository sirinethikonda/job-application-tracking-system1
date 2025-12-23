package com.example.demo.application;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    private String id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "candidate_email")
    private String candidateEmail; // This was missing

    @Column(name = "resume_url")
    private String resumeUrl;

    private String stage;

    @Column(name = "created_at")
    private Timestamp createdAt; // This was missing

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // --- GETTERS AND SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}