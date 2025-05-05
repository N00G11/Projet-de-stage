package com.app.fileintegration.entity.execution;

import com.app.fileintegration.entity.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class JobExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    @OneToOne(mappedBy = "jobExecution")
    @JsonIgnore
    private Job job;

    @OneToMany(mappedBy = "jobExecution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionLog> executionLogs = new ArrayList<>();
}
