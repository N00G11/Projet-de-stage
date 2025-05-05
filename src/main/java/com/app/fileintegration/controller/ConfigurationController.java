package com.app.fileintegration.controller;

import com.app.fileintegration.entity.*;
import com.app.fileintegration.entity.execution.JobExecution;
import com.app.fileintegration.repository.JobRepository;
import com.app.fileintegration.repository.UserRepository;
import com.app.fileintegration.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/etl/etlConfiduration")
@RequiredArgsConstructor
public class ConfigurationController {
    private final ConfigurationService configurationService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @PostMapping("/etl/{idUser}")
    public ResponseEntity<?> addETLJobToUser(@PathVariable Long idUser, @RequestBody Job job) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            user.get().getJobs().add(job);
            job.setUser(user.get());
            jobRepository.save(job);
            userRepository.save(user.get()); // Ajouté pour sauvegarder la modification
            return ResponseEntity.ok("ETLJob added to user with id: " + idUser);
        } else {
            return ResponseEntity.badRequest().body("User with id: " + idUser + " not found");
        }
    }


    @PostMapping("/datasource/{id}")
    public ResponseEntity<?> datasource(@PathVariable Long id, @RequestBody DataSource dataSource){
        if (configurationService.assignDataSourceToETLJob(id,dataSource)){
            return ResponseEntity.ok("Datasource assigned to job with id: " + id + " successfully");
        }else {
            return ResponseEntity.badRequest().body("Datasource could not be assigned to job with id: " + id  + "because this etljob not exist");
        }
    }

    @PostMapping("/transform/{id}")
    public ResponseEntity<?> transformation(@PathVariable Long id, @RequestBody Transformation transformation){
        if (configurationService.assignTransformationToETLJob(id,transformation)){
            return ResponseEntity.ok("Transformation assigned to job with id: " + id + " successfully");
        }else {
            return ResponseEntity.badRequest().body("Transformation could not be assigned to job with id: " + id  + "because this etljob not exist");
        }
    }

    @PostMapping("/datatarget/{id}")
    public ResponseEntity<?> datatarget(@PathVariable Long id, @RequestBody DataTarget dataTarget){
        if (configurationService.assignDataTargetToETLJob(id,dataTarget)){
            return ResponseEntity.ok("DataTarget assigned to job with id: " + id + " successfully");
        }else {
            return ResponseEntity.badRequest().body("DataTarget could not be assigned to job with id: " + id  + "because this etljob not exist");
        }
    }

    @PostMapping("/jobexecution/{id}")
    public ResponseEntity<?> jobexecution(@PathVariable Long id, @RequestBody JobExecution jobExecution){
        if (configurationService.assignJobExecutionToETLJob(id,jobExecution)){
            return ResponseEntity.ok("JobExecution assigned to job with id: " + id + " successfully");
        }else {
            return ResponseEntity.badRequest().body("JobExecution could not be assigned to job with id: " + id + "because this etljob not exist");
        }
    }
}
