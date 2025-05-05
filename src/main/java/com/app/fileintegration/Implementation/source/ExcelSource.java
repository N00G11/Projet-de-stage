package com.app.fileintegration.Implementation.source;

import com.app.fileintegration.Interface.IDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelSource implements IDataSource {

    private String filePath;

    public ExcelSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getType() {
        return filePath.substring(filePath.lastIndexOf('.'));
    }

    public List<Map<String, Object>> extract() {
        List<Map<String, Object>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            
            // Création de la liste ordonnée des en-têtes
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            // Lecture des données en respectant l'ordre des colonnes
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (currentRow != null) {
                    Map<String, Object> rowData = new LinkedHashMap<>();
                    
                    // Parcours des colonnes dans l'ordre des en-têtes
                    for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                        String header = headers.get(colIndex);
                        Cell cell = currentRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        
                        Object cellValue;
                        switch (cell.getCellType()) {
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                cellValue = cell.getNumericCellValue();
                                break;
                            case BOOLEAN:
                                cellValue = cell.getBooleanCellValue();
                                break;
                            default:
                                cellValue = "";
                        }
                        rowData.put(header, cellValue);
                    }
                    data.add(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}