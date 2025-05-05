package com.app.fileintegration.repository;

import com.app.fileintegration.entity.execution.ExecutionLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionLogRepository extends CrudRepository<ExecutionLog, Long> {
}
