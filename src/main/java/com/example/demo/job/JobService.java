package com.example.demo.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public Job createJob(String companyId, String title, String description, String createdBy) {
        Job j = new Job();
        j.setId(UUID.randomUUID().toString());
        j.setCompanyId(companyId);
        j.setTitle(title);
        j.setDescription(description);
        j.setStatus("OPEN");
        j.setCreatedBy(createdBy);
        return jobRepository.save(j);
    }

    public List<Job> listAll() { return jobRepository.findAll(); }

    public Job get(String id) { return jobRepository.findById(id).orElse(null); }

    public void delete(String id) { jobRepository.deleteById(id); }
}
