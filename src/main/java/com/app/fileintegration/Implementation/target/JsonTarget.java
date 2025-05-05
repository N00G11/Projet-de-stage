package com.app.fileintegration.Implementation.target;
import com.app.fileintegration.Interface.IDataTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTarget implements IDataTarget {

    private final String filePath;

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();

    public JsonTarget(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void load(List<Map<String, Object>> data) {
        Map<String, Object> a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Starting Loading phase");
        a.put("logLevel", "INFO");
        logs.add(a);

        a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Connecting to target json file");
        a.put("logLevel", "INFO");
        logs.add(a);


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pour un JSON bien formaté (joli)

        try {
            mapper.writeValue(new File(filePath), data); // Écrit directement la LISTE

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", " Connection established");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Starting data insertion");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "Inserted" + data.size() + "records in this JSON");
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
