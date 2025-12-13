package com.example.demo.company;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @Column(length = 36)
    private String id;

    private String name;

    @Column(name="created_at")
    private Timestamp createdAt;

    public Company() {}
    public String getId(){ return id; } public void setId(String id){ this.id = id; }
    public String getName(){ return name; } public void setName(String name){ this.name = name; }
    public Timestamp getCreatedAt(){ return createdAt; } public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
}
