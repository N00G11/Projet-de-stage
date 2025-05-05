package com.app.fileintegration.controller;

import com.app.fileintegration.entity.Job;
import com.app.fileintegration.entity.User;
import com.app.fileintegration.repository.UserRepository;
import com.app.fileintegration.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/processes")
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationService integrationService;
    private final UserRepository userRepository;

    @GetMapping("/{iduser}/{idetljob}")
    public ResponseEntity<?> lancer(@PathVariable(name = "iduser") Long iduser,@PathVariable(name = "idetljob") Long idetljob){
        Optional<User> user = userRepository.findById(iduser);
        String message;
        if (user.isPresent()){
            Optional<Job> etljob = user.get().getJobs().stream().filter(e -> e.getId().equals(idetljob)).findFirst();
            if (etljob.isPresent()){
                String source = etljob.get().getDataSource().getLien();
                String target = etljob.get().getDataTarget().getLien();

                integrationService.processETL(source,target);
                message = integrationService.getMessage();
                if (integrationService.getExtractNumber() == 0){
                    return ResponseEntity.ok(message);
                }
                return ResponseEntity.ok("job it's done" + message);
            }

            return ResponseEntity.badRequest().body("job not found");
        }

        return ResponseEntity.badRequest().body("user not found");
    }
}
