package com.app.fileintegration.repository;


import com.app.fileintegration.entity.DataTarget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTargetRepository extends CrudRepository<DataTarget, Long> {
}
