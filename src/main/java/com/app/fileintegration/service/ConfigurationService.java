package com.app.fileintegration.service;


import com.app.fileintegration.entity.DataSource;
import com.app.fileintegration.entity.DataTarget;
import com.app.fileintegration.entity.Job;
import com.app.fileintegration.entity.Transformation;
import com.app.fileintegration.entity.execution.ExecutionLog;
import com.app.fileintegration.entity.execution.JobExecution;
import com.app.fileintegration.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final DataSourceRepository dataSourceRepository;
    private final DataTargetRepository dataTargetRepository;
    private  final TransformationRepository transformationRepository;
    private  final JobExecutionRepository jobExecutionRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final JobRepository jobRepository;



    public boolean addExecutionLogToJobExecution(Long idJobE, ExecutionLog executionLog) {
        Optional<JobExecution> jobExecution = jobExecutionRepository.findById(idJobE);
        if (jobExecution.isPresent()){
            jobExecution.get().getExecutionLogs().add(executionLog);
            executionLogRepository.save(executionLog);
            return true;
        }
        return false;
    }

    public Boolean assignJobExecutionToETLJob(Long idETLJob, JobExecution jobExecution){
        Optional<Job> etlJob = jobRepository.findById(idETLJob);
        if (etlJob.isPresent()){
            etlJob.get().setJobExecution(jobExecution);
            jobExecutionRepository.save(jobExecution);
            return true;
        }
        return false;
    }

    public Boolean assignDataSourceToETLJob(Long idETLJob, DataSource dataSource){
        Optional<Job> etlJob = jobRepository.findById(idETLJob);
        if (etlJob.isPresent()){
            etlJob.get().setDataSource(dataSource);
            dataSourceRepository.save(dataSource);
            return true;
        }
        return false;
    }

    public Boolean assignDataTargetToETLJob(Long idETLJob, DataTarget dataTarget){
        Optional<Job> etlJob = jobRepository.findById(idETLJob);
        if (etlJob.isPresent()){
            etlJob.get().setDataTarget(dataTarget);
            dataTargetRepository.save(dataTarget);
            return true;
        }
        return false;
    }
    public Boolean assignTransformationToETLJob(Long idETLJob, Transformation transformation){
        Optional<Job> etlJob = jobRepository.findById(idETLJob);
        if (etlJob.isPresent()){
            etlJob.get().setTransformation(transformation);
            transformationRepository.save(transformation);
            return true;
        }
        return false;
    }
}
