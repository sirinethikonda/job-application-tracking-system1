package com.example.demo.application;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "application_history")
public class ApplicationHistory {
    @Id
    @Column(length=36)
    private String id;

    @Column(name = "application_id", length = 36)
    private String applicationId;

    private String previousStage;
    private String newStage;
    private String changedBy;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private Timestamp createdAt;

    public ApplicationHistory() {}

    // getters & setters
    public String getId(){ return id; } public void setId(String id){ this.id = id; }
    public String getApplicationId(){ return applicationId; } public void setApplicationId(String applicationId){ this.applicationId = applicationId; }
    public String getPreviousStage(){ return previousStage; } public void setPreviousStage(String previousStage){ this.previousStage = previousStage; }
    public String getNewStage(){ return newStage; } public void setNewStage(String newStage){ this.newStage = newStage; }
    public String getChangedBy(){ return changedBy; } public void setChangedBy(String changedBy){ this.changedBy = changedBy; }
    public String getReason(){ return reason; } public void setReason(String reason){ this.reason = reason; }
    public Timestamp getCreatedAt(){ return createdAt; } public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
}
