package com.app.fileintegration.repository;

import com.app.fileintegration.entity.DataSource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceRepository extends CrudRepository<DataSource, Long> {
}
