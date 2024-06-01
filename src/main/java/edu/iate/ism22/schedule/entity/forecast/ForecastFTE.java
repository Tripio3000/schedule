package edu.iate.ism22.schedule.entity.forecast;

import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ForecastFTE implements Forecast<Map<LocalDateTime, Integer>> {
    
    @Value("${forecast.fte.path}")
    private String filePath;
    
    @Override
    public Map<LocalDateTime, Integer> valueFor(LocalInterval interval) {
        try (FileInputStream fis = new FileInputStream("src/main/resources/07-09_2024_by_hour.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            Map<LocalDateTime, Integer> dataMap = new HashMap<>();
            int rowCount = sheet.getLastRowNum();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(3);
                
                LocalDateTime dateTime = LocalDateTime.parse(keyCell.getStringCellValue(), formatter);
                Integer value = (int) valueCell.getNumericCellValue();
                dataMap.put(dateTime, value);
                
            }
            return dataMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
