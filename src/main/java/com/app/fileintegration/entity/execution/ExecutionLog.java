package com.app.fileintegration.entity.execution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;

@Entity
@Data
public class ExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;
    private LocalDateTime stamptime;
    private String message;
    private LogLevel logLevel;


    @ManyToOne
    @JoinColumn(name = "jobExecution_id")
    private JobExecution jobExecution;
}
