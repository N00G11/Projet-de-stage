package com.app.fileintegration.Implementation.source;

import com.app.fileintegration.Interface.IDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ApiSource implements IDataSource {

    private final String connexionUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final List<Map<String, Object>> logs = new ArrayList<>();

    public ApiSource(String connexionUrl) {
        this.connexionUrl = connexionUrl;
    }

    @Override
    public List<Map<String, Object>> extract() {
        log("Starting Extraction phase", "INFO");

        List<Map<String, Object>> data = new ArrayList<>();

        try {
            log("Connecting to source API", "INFO");
            URL url = new URL(connexionUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log("Failed to connect. HTTP code: " + responseCode, "ERROR");
                return data;
            }

            log("Connection established", "INFO");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            conn.disconnect();

            log("Parsing response content", "INFO");

            String json = content.toString().trim();
            if (json.startsWith("[")) {
                // JSON Array
                data = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            } else if (json.startsWith("{")) {
                // JSON Object
                Map<String, Object> singleObj = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
                data.add(singleObj);
            } else {
                log("Unknown response format", "ERROR");
            }

            log("Extracted " + data.size() + " records from API", "INFO");
        } catch (Exception e) {
            log("Exception during extraction: " + e.getMessage(), "ERROR");
        }

        log("Extraction phase completed", "INFO");
        return data;
    }

    private void log(String message, String level) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("stamptime", System.currentTimeMillis());
        logEntry.put("message", message);
        logEntry.put("logLevel", level);
        logs.add(logEntry);
    }

    @Override
    public String getType() {
        return "api";
    }
}
