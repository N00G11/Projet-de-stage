package com.app.fileintegration.service;

import com.app.fileintegration.Implementation.source.ApiSource;
import com.app.fileintegration.Implementation.source.CsvSource;
import com.app.fileintegration.Implementation.source.ExcelSource;
import com.app.fileintegration.Implementation.source.JsonSource;
import com.app.fileintegration.Implementation.target.ApiTarget;
import com.app.fileintegration.Implementation.target.CsvTarget;
import com.app.fileintegration.Implementation.target.ExcelTarget;
import com.app.fileintegration.Implementation.target.JsonTarget;
import com.app.fileintegration.Implementation.transformation.Mappage;
import com.app.fileintegration.Interface.IDataSource;
import com.app.fileintegration.Interface.IDataTarget;
import com.app.fileintegration.Interface.IDataTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final UserService userService;
    private final ConfigurationService configurationService;
    private IDataSource dataSource;
    private IDataTarget dataTarget;
    private IDataTransformer transformation = new Mappage();

    @Getter
    private int extractNumber = 0;
    @Getter
    private String message = "";


    public void processETL(String source,String Target){
        String filesourece = source.substring(source.lastIndexOf('.') + 1);
        String apisource = source.substring(0, 4);

        String filetarget = Target.substring(Target.lastIndexOf('.') + 1);
        String apitarget = Target.substring(0, 4);

        List<Map<String, Object>> data = new ArrayList<>();

        if (apisource.equals("http")){
            dataSource = new ApiSource(source);
            data = dataSource.extract();
            extractNumber = data.size();
        }else {
            switch (filesourece){
                case "csv":
                    dataSource = new CsvSource(source);
                    data = dataSource.extract();
                    extractNumber = data.size();
                    break;
                case "json":
                    dataSource = new JsonSource(source);
                    data = dataSource.extract();
                    extractNumber = data.size();
                    break;
                case "xlsx":
                    dataSource = new ExcelSource(source);
                    data = dataSource.extract();
                    extractNumber = data.size();
                    break;
                default: throw new IllegalArgumentException("File type not supported");
            }
        }

        if (extractNumber == 0){
            message = "No data extracted from source";
            return;
        }

        List<String> newKeys = List.of("nom","email","none","none","none");

        data = transformation.transform(data,newKeys);

        if (apitarget.equals("http")){
            dataTarget = new ApiTarget(Target);
            dataTarget.load(data);
        }else {
            switch (filetarget){
                case "csv":
                    dataTarget = new CsvTarget(Target);
                    dataTarget.load(data);
                    break;
                case "json":
                    dataTarget = new JsonTarget(Target);
                    dataTarget.load(data);
                    break;
                case "xlsx":
                    dataTarget = new ExcelTarget(Target);
                    dataTarget.load(data);
                    break;
                default: throw new IllegalArgumentException("File type not supported");
            }
        }

        message =" " + extractNumber + " records extracted from source and saved to target";
    }
}
