package com.app.fileintegration.repository;

import com.app.fileintegration.entity.Transformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransformationRepository extends CrudRepository<Transformation, Long> {
}
