package com.app.fileintegration.Implementation.target;

import com.app.fileintegration.Interface.IDataTarget;
import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CsvTarget implements IDataTarget {

    private final String filePath;

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();

    public CsvTarget(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load(List<Map<String, Object>> data) {
        Map<String, Object> a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Starting Loading phase");
        a.put("logLevel", "INFO");
        logs.add(a);

        if (data == null || data.isEmpty()) return;

        a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Connecting to target csv file");
        a.put("logLevel", "INFO");
        logs.add(a);

        try (FileWriter writer = new FileWriter(filePath)) {
            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Connection established");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Starting data insertion");
            a.put("logLevel", "INFO");
            logs.add(a);

            // Write header
            Map<String, Object> headerMap = data.get(0);
            List<String> headers = new ArrayList<>(headerMap.keySet());
            writer.append(String.join(",", headers));
            writer.append("\n");

            // Write data
            for (Map<String, Object> row : data) {
                List<String> values = new ArrayList<>();
                for (String key : headers) {
                    Object value = row.getOrDefault(key, "");
                    values.add(value != null ? value.toString() : "");
                }
                writer.append(String.join(",", values));
                writer.append("\n");
            }

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Inserted " + data.size() + " records in this CSV");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Insertion phase completed");
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
    }
}
