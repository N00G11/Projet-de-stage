package com.app.fileintegration.repository;

import com.app.fileintegration.entity.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {
    Optional<Job> findFirstByOrderByCreateTimeDesc();
}
