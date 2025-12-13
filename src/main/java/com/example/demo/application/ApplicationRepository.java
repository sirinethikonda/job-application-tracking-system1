package com.example.demo.application;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByJobIdAndCandidateId(String jobId, String candidateId);
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByJobIdAndStage(String jobId, String stage);
    List<Application> findByJobId(String jobId);
}
