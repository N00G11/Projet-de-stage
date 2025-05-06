package com.app.fileintegration.controller;


import com.app.fileintegration.entity.DataSource;
import com.app.fileintegration.entity.DataTarget;
import com.app.fileintegration.service.prossecesService.ExtractionService;
import com.app.fileintegration.service.prossecesService.LoadService;
import com.app.fileintegration.service.prossecesService.TransformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prosseces")
@RequiredArgsConstructor
public class ProssecesController {

    private final ExtractionService extractionService;
    private final TransformationService transformationService;
    private final LoadService loadService;
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> endData;

    @PostMapping("/datasource")
    public ResponseEntity<?> datasource(@RequestBody DataSource dataSource){
        data = extractionService.extract(dataSource);
        if (extractionService.getExtractNumber() != 0){
            return ResponseEntity.ok(extractionService.getMessage());
        }

        return ResponseEntity.badRequest().body(extractionService.getMessage());
    }


    @PostMapping("/transformation")
    public ResponseEntity<?> transform(@RequestBody List<String> newKeys){
        endData = transformationService.transform(data, newKeys);
        if (endData != null){
            return ResponseEntity.ok(transformationService.getMessage());
        }
        return ResponseEntity.badRequest().body(transformationService.getMessage());
    }

    @PostMapping("/datatarget")
    public ResponseEntity<?> load(@RequestBody DataTarget dataTarget){
        if (loadService.load(dataTarget,endData)){
            return ResponseEntity.ok(loadService.getMessage());
        }
        return ResponseEntity.badRequest().body(loadService.getMessage());
    }

}
