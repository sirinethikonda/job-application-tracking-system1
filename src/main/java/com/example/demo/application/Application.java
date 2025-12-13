package com.example.demo.application;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name="job_id", length = 36)
    private String jobId;

    @Column(name="candidate_id", length = 36)
    private String candidateId;

    private String stage; // APPLIED, SCREENING, INTERVIEW, OFFER, HIRED, REJECTED

    private Timestamp appliedAt;
    private Timestamp updatedAt;

    @Version
    private Long version;

    public Application() {}

    // getters & setters omitted for brevity — add them in your IDE or paste generated ones
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getJobId() { return jobId; } public void setJobId(String jobId) { this.jobId = jobId; }
    public String getCandidateId() { return candidateId; } public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
    public String getStage() { return stage; } public void setStage(String stage) { this.stage = stage; }
    public Timestamp getAppliedAt() { return appliedAt; } public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }
    public Timestamp getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public Long getVersion() { return version; } public void setVersion(Long version) { this.version = version; }
}
