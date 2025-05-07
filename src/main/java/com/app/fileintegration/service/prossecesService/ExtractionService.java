package com.app.fileintegration.service.prossecesService;

import com.app.fileintegration.Implementation.source.ApiSource;
import com.app.fileintegration.Implementation.source.CsvSource;
import com.app.fileintegration.Implementation.source.ExcelSource;
import com.app.fileintegration.Implementation.source.JsonSource;
import com.app.fileintegration.Interface.IDataSource;
import com.app.fileintegration.entity.DataSource;
import com.app.fileintegration.entity.Job;
import com.app.fileintegration.repository.DataSourceRepository;
import com.app.fileintegration.repository.JobRepository;
import com.app.fileintegration.service.JobService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExtractionService {
    private final JobService jobService;
    private final DataSourceRepository dataSourceRepository;
    private final JobRepository jobRepository;
    private IDataSource dataSource;
    @Getter
    private String message = "";
    @Getter
    private int extractNumber = 0;
    @Getter
    private  List<String> extractKeys = null;
    @Getter
    private  List<Map<String, Object>> extractData = null;

    public List<Map<String, Object>> extract(DataSource ds){
         Optional<Job> job = jobService.findMostRecentJob();
        if (job.isPresent()){
            job.get().setDataSource(ds);
            jobRepository.save(job.get());
        }
        if (job.get().getDataSource().getLien() == null || job.get().getDataSource().getType().isEmpty()){
            message = "Source file type is empty";
            return null;
        }
        if (job.get().getDataSource().getLien() == null || job.get().getDataSource().getLien().isEmpty()){
            message = "Source file path is empty";
            return null;
        }else {
            switch (job.get().getDataSource().getType().toLowerCase()) {
                case "api":
                    dataSource = new ApiSource(ds.getLien());
                case "csv":
                    dataSource = new CsvSource(ds.getLien());
                    break;
                case "json":
                    dataSource = new JsonSource(ds.getLien());
                    break;
                case "excel":
                    dataSource = new ExcelSource(ds.getLien());
                    break;
                default:
                    throw new IllegalArgumentException("File type not supported");
            }
        }
        List<Map<String, Object>> data = dataSource.extract();
        extractNumber = data.size();
        if (extractNumber == 0){
            message = "No data extracted from source";
            return null;
        }

        message = "Data extracted from source";

        extractKeys  = extractAllKeys(data);
        extractData = data;
        return data;
    }

    public List<String> extractAllKeys(List<Map<String, Object>> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> uniqueKeys = new LinkedHashSet<>();
        for (Map<String, Object> map : mapList) {
            uniqueKeys.addAll(map.keySet());
        }
        return new ArrayList<>(uniqueKeys);
    }
}
