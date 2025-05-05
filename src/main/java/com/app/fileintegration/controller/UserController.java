package com.app.fileintegration.controller;


import com.app.fileintegration.entity.User;
import com.app.fileintegration.repository.JobRepository;
import com.app.fileintegration.repository.UserRepository;
import com.app.fileintegration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JobRepository jobRepository;

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/all/{id}")
    public Optional<User> getUserById(@PathVariable  Long id){
        return userRepository.findById(id);
    }


}