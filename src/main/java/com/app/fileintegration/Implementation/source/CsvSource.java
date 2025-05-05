package com.app.fileintegration.Implementation.source;

import com.app.fileintegration.Interface.IDataSource;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvSource implements IDataSource {

    private String filePath;
    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();

    @Override
    public String getType() {
        return filePath.substring(filePath.lastIndexOf('.') + 1);
    }

    public CsvSource(String filePath) {
        this.filePath = filePath;
    }

    public List<Map<String, Object>> extract() {
        Map<String, Object> a = new LinkedHashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Starting Extration phase");
        a.put("logLevel", "INFO");
        logs.add(a);
        List<Map<String, Object>> data = new ArrayList<>();

        a = new LinkedHashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Connecting to source csv file");
        a.put("logLevel", "INFO");
        logs.add(a);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            a = new LinkedHashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Connection established");
            a.put("logLevel", "INFO");
            logs.add(a);

            String headerLine = br.readLine();
            if (headerLine == null) return data;
            String[] headers = headerLine.split(",");

            a = new LinkedHashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Starting data extraction");
            a.put("logLevel", "INFO");
            logs.add(a);

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }
                data.add(row);
            }

            a = new LinkedHashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Extracted " + data.size() + " records from this CSV");
            a.put("logLevel", "INFO");
            logs.add(a);
        } catch (IOException e) {
            a = new LinkedHashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Connection not established");
            a.put("logLevel", "ERROR");
            logs.add(a);

            a = new LinkedHashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", e.getMessage());
            a.put("logLevel", "ERROR");
            logs.add(a);
            return data;
        }
        return data;
    }
}