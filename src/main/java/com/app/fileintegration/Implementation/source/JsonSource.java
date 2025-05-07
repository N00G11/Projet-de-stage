package com.app.fileintegration.Implementation.source;

import com.app.fileintegration.Interface.IDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonSource implements IDataSource {

    private String filePath;

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();

    public JsonSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getType() {
        return filePath.substring(filePath.lastIndexOf('.'));
    }

    public List<Map<String, Object>> extract() {


        List<Map<String, Object>> data = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();



        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {


            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            // Parse le JSON en List<Map<String, Object>>

            data = mapper.readValue(
                    jsonContent.toString(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }

        return data;
    }
}