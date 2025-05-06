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

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<Map<String, Object>> extract(DataSource ds){
        Optional<Job> job = jobService.findMostRecentJob();
        String source = ds.getLien();

        if (source == null || source.isEmpty()){
            message = "Source file path is empty";
            return null;
        }
        String filesource = source.substring(source.lastIndexOf('.') + 1);
        String apisource = source.substring(0, 4);
        if (apisource.equals("http")){
            dataSource = new ApiSource(source);
        }else {
            switch (filesource){
                case "csv":
                    dataSource = new CsvSource(source);
                    break;
                case "json":
                    dataSource = new JsonSource(source);
                    break;
                case "xlsx":
                    dataSource = new ExcelSource(source);
                    break;
                default: throw new IllegalArgumentException("File type not supported");
            }
        }
        extractNumber = dataSource.extract().size();
        if (extractNumber == 0){
            message = "No data extracted from source";
            return null;
        }

        if (job.isPresent()){
            job.get().setDataSource(ds);
            //ds.setJob(job.get());
            jobRepository.save(job.get());
            message = "Data extracted from source";
        }
        return dataSource.extract();
    }
}
