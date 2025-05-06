package com.app.fileintegration.service;

import com.app.fileintegration.entity.Job;
import com.app.fileintegration.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public Optional<Job> findMostRecentJob() {
        return jobRepository.findFirstByOrderByCreateTimeDesc();
    }
}