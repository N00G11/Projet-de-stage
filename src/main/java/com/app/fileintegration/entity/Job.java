package com.app.fileintegration.entity;


import com.app.fileintegration.entity.execution.JobExecution;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;
    private String jobName;
    private String description;
    private LocalDateTime createTime = LocalDateTime.now();
    private String status;

    @ManyToOne
    @JoinColumn(name = "users_id")
    @JsonIgnore
    private User user;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_source_id", referencedColumnName = "id")
    private DataSource dataSource;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_target_id", referencedColumnName = "id")
    private DataTarget dataTarget;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transformation_id", referencedColumnName = "id")
    private Transformation transformation;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_execution_id", referencedColumnName = "id")
    private JobExecution jobExecution;

}
