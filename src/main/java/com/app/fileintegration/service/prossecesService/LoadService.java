package com.app.fileintegration.service.prossecesService;

import com.app.fileintegration.Implementation.target.ApiTarget;
import com.app.fileintegration.Implementation.target.CsvTarget;
import com.app.fileintegration.Implementation.target.ExcelTarget;
import com.app.fileintegration.Implementation.target.JsonTarget;
import com.app.fileintegration.Interface.IDataTarget;
import com.app.fileintegration.entity.DataTarget;
import com.app.fileintegration.entity.Job;
import com.app.fileintegration.repository.DataTargetRepository;
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
public class LoadService {

    private final JobService jobService;
    private final JobRepository jobRepository;
    private final DataTargetRepository dataTargetRepository;
    private IDataTarget dataTarget;
    @Getter
    private String message = "";

    public Boolean load(DataTarget tg, List<Map<String, Object>> data){
        Optional<Job> job = jobService.findMostRecentJob();
        String target = tg.getLien();
        if (target == null || target.isEmpty()){
            message = "Target file path is empty";
            return false;
        }

        String filetarget = target.substring(target.lastIndexOf('.') + 1);
        String apitarget = target.substring(0, 4);
        if (apitarget.equals("http")){
            dataTarget = new ApiTarget(target);
        }else {
            switch (filetarget){
                case "csv":
                    dataTarget = new CsvTarget(target);
                    break;
                case "json":
                    dataTarget = new JsonTarget(target);
                    break;
                case "xlsx":
                    dataTarget = new ExcelTarget(target);
                    break;
                default:
                    message = "File type not supported";
                    break;
            }
        }

        dataTarget.load(data);
        job.get().setDataTarget(tg);
        jobRepository.save(job.get());
        message = "Data loaded to target";
        return true;
    }
}
