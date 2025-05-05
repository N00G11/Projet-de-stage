package com.app.fileintegration.Implementation.target;

import com.app.fileintegration.Interface.IDataTarget;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelTarget implements IDataTarget {

    private final String filePath;

    @Getter
    private List<Map<String, Object>> logs = new ArrayList<>();



    public ExcelTarget(String filePath) {
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


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowCount = 0;

        // Write header
        Row headerRow = sheet.createRow(rowCount++);
        int headerCol = 0;
        for (String key : data.get(0).keySet()) {
            headerRow.createCell(headerCol++).setCellValue(key);
        }

        // Write data
        for (Map<String, Object> rowMap : data) {
            Row row = sheet.createRow(rowCount++);
            int colCount = 0;
            for (Object value : rowMap.values()) {
                row.createCell(colCount++).setCellValue(value != null ? value.toString() : "");
            }
        }


        a = new HashMap<>();
        a.put("stamptime", System.currentTimeMillis());
        a.put("message", "Saving file");
        a.put("logLevel", "INFO");
        logs.add(a);
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
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
            a.put("message", "Inserted" + data.size() + "records in this Excel");
            a.put("logLevel", "INFO");
            logs.add(a);

            a = new HashMap<>();
            a.put("stamptime", System.currentTimeMillis());
            a.put("message", "File saved");
            a.put("logLevel", "INFO");
            workbook.write(outputStream);
            workbook.close();

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
