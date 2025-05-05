package com.app.fileintegration.repository;


import com.app.fileintegration.entity.execution.JobExecution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExecutionRepository extends CrudRepository<JobExecution, Long> {
}
