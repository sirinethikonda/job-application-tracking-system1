package com.example.demo.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory,String> {
    List<ApplicationHistory> findByApplicationIdOrderByCreatedAtAsc(String applicationId);
}
