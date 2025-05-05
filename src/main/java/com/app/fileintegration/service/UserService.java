package com.app.fileintegration.service;

import com.app.fileintegration.entity.Job;
import com.app.fileintegration.repository.JobRepository;
import com.app.fileintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public boolean addETLJobToUser(Long idUser, Long idETLJob){
        Optional<Job> etlJob = jobRepository.findById(idETLJob);
        if (etlJob.isPresent()){
            etlJob.get().setUser(userRepository.findById(idUser).get());
            jobRepository.save(etlJob.get());
            return true;
        }
        return false;
    }
}
