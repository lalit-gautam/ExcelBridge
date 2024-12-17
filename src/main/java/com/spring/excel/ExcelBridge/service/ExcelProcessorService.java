package com.spring.excel.ExcelBridge.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExcelProcessorService {

    @Value("${directory.name}")
    private String DIRECTORY_PATH;

    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger log = LogManager.getLogger(ExcelProcessorService.class);

    public String getDirName(){
        return DIRECTORY_PATH;
    }

    @Transactional
    public void processExcelFiles(){
        File directory = new File(DIRECTORY_PATH);
        File [] excelFiles = directory.listFiles((dir, name) -> name.endsWith(".xlsx") || name.endsWith(".xls"));

        if(excelFiles == null || excelFiles.length == 0){
            log.error("No excel files to Process.");
            return;
        }

        for(File file : excelFiles){
            log.error("Processing File : {}", file.getName());
            processExcelFile(file);
            deleteFile(file);
        }
    }

    private void deleteFile(File file){
        if(file.exists()){
            if (file.delete()) {
                log.info("File deleted successfully: {}", file.getName());
            } else {
                log.error("Failed to delete the file: {}", file.getName());
            }
        }
    }

    private void processExcelFile(File file) {
        try(FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if(sheet == null){
                log.error("Sheet is empty in File : {}", file.getName());
                return;
            }

            String tableName = file.getName().replace(".xlsx", "");
            Map<String, String> columnDefinitions = inferColumnDefinitions(sheet);

            createTable(tableName, columnDefinitions);
            insertData(tableName, sheet, columnDefinitions.keySet());

            try {
                Thread.sleep(2000); // Wait for 2 seconds before attempting deletion
            } catch (InterruptedException e) {
                log.error("Delay of 2 Seconds");
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable(String tableName, Map<String, String> columnDefinitions) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName + " (");
               // .append(" (id BIGINT AUTO_INCREMENT PRIMARY KEY, ");

        for(Map.Entry<String, String> entry : columnDefinitions.entrySet()){
            createTableQuery.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        createTableQuery.setLength(createTableQuery.length() - 2); // to remove last comma
        createTableQuery.append(")");

        entityManager.createNativeQuery(createTableQuery.toString()).executeUpdate();
        log.info("Table created : {}" , tableName);
    }

    private void insertData(String tableName, Sheet sheet, Iterable<String> columnNames) {
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES ";
        StringBuilder valuePlaceholders = new StringBuilder();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            valuePlaceholders.append("(");
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                valuePlaceholders.append("'").append(getCellValue(cell)).append("', ");
            }
            valuePlaceholders.setLength(valuePlaceholders.length() - 2); // Remove last comma
            valuePlaceholders.append("), ");
        }

        valuePlaceholders.setLength(valuePlaceholders.length() - 2); // Remove last comma
        entityManager.createNativeQuery(insertQuery + valuePlaceholders).executeUpdate();
        log.info("Data inserted into table: {}", tableName);
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> null;
        };
    }


    private Map<String, String> inferColumnDefinitions(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        Map<String, String> columnDefinitions = new LinkedHashMap<>();

        for (Cell cell : headerRow) {
            String columnName = cell.getStringCellValue();
            String dataType = inferDataType(sheet, cell.getColumnIndex());
            columnDefinitions.put(columnName, dataType);
        }
        return columnDefinitions;
    }

    private String inferDataType(Sheet sheet, int columnIndex) {
        for (int i = 1; i <= Math.min(sheet.getLastRowNum(), 10); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell cell = row.getCell(columnIndex);
            if (cell == null) continue;

            switch (cell.getCellType()) {
                case STRING:
                    return "VARCHAR(255)";
                case NUMERIC:
                    return "DOUBLE";
                case BOOLEAN:
                    return "BOOLEAN";
                default:
                    continue;
            }
        }
        return "TEXT";
    }



}
