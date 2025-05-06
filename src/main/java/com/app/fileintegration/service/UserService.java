package com.app.fileintegration.service;

import com.app.fileintegration.entity.Job;
import com.app.fileintegration.entity.User;
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

    public boolean addJobToUser(Long idUser, Job job){
        Optional<User> user = userRepository.findById(idUser);
        if (user.isPresent()){
            job.setUser(user.get());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}
