package com.example.demo.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,String> {
    List<Job> findByCompanyId(String companyId);
}
