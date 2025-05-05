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


        Map<String, Object> a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Starting Extration phase");
        a.put("logLevel", "INFO");
        logs.add(a);

        List<Map<String, Object>> data = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();


        a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Connecting to source json file");
        a.put("logLevel", "INFO");
        logs.add(a);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Connection established");
            a.put("logLevel", "INFO");
            logs.add(a);


            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            // Parse le JSON en List<Map<String, Object>>
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Starting data extraction");
            a.put("logLevel", "INFO");
            data = mapper.readValue(
                    jsonContent.toString(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            logs.add(a);
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Extracted" + data.size() + "records from this JSON");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Extraction phase completed");
            a.put("logLevel", "INFO");
            logs.add(a);

        } catch (IOException e) {
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Connection not established");
            a.put("logLevel", "ERROR");
            logs.add(a);
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", e.getMessage());
            a.put("logLevel", "ERROR");
            logs.add(a);
        }

        return data;
    }
}