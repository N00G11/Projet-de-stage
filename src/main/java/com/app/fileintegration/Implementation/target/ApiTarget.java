package com.app.fileintegration.Implementation.target;

import com.app.fileintegration.Interface.IDataTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiTarget implements IDataTarget {

    private final String apiUrl;

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();


    public ApiTarget(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public void load(List<Map<String, Object>> data) {
        Map<String, Object> a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Starting Loading phase");
        a.put("logLevel", "INFO");
        logs.add(a);


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            for (Map<String, Object> utilisateur : data) {
                // On sérialise un utilisateur à la fois
                String jsonData = mapper.writeValueAsString(utilisateur);

                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Connecting to target api");
                a.put("logLevel", "INFO");
                logs.add(a);

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection == null) {
                    a = new HashMap<>();
                    a.put("stamptime", System.currentTimeMillis());
                    a.put("message", "Connection not established");
                    a.put("logLevel", "ERROR");
                    logs.add(a);
                    continue;
                }
                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Connection established");
                a.put("logLevel", "INFO");
                logs.add(a);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "*/*");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonData.getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                BufferedReader reader;
                if (responseCode >= 200 && responseCode < 300) {
                    a = new HashMap<>();
                    a.put("stamptime", System.currentTimeMillis());
                    a.put("message", "Réponse HTTP : " + responseCode);
                    a.put("logLevel", "INFO");
                    logs.add(a);
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                } else {
                    a = new HashMap<>();
                    a.put("stamptime", System.currentTimeMillis());
                    a.put("message", "Réponse HTTP : " + responseCode);
                    a.put("logLevel", "WARNING");
                    logs.add(a);
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                }

                StringBuilder response = new StringBuilder();
                String line;

                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Inserting data");
                a.put("logLevel", "INFO");
                logs.add(a);


                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                reader.close();
                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Inserted");
                a.put("logLevel", "INFO");
                logs.add(a);

                connection.disconnect();
                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Inserted" + data.size() + "records in this CSV");
                a.put("logLevel", "INFO");
                logs.add(a);

                a = new HashMap<>();
                a.put("stamptime", System.currentTimeMillis());
                a.put("message", "Insertion phase completed");
                a.put("logLevel", "INFO");
                logs.add(a);
            }

        } catch (Exception e) {
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
