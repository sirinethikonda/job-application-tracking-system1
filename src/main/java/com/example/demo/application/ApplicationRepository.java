package com.example.demo.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    
    // FIX 1: Use CandidateEmail instead of CandidateId
    List<Application> findByCandidateEmail(String email);
    
    // FIX 2: Use CandidateEmail instead of CandidateId
    boolean existsByJobIdAndCandidateEmail(String jobId, String email);
    
    List<Application> findByJobId(String jobId);
}