package edu.iate.ism22.schedule.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static edu.iate.ism22.schedule.generation.Constants.TIME_QUANT;

public class ReportService {
    
    private static final String fileName = "report.txt";
    
    public void createFullReport(List<Map<LocalDateTime, Integer>> toVisualization, LocalInterval interval) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            LocalDateTime current = interval.getStart();
            while (current.isBefore(interval.getEnd())) {
                
                for (int i = 0; i < toVisualization.size(); i++) {
                    Map<LocalDateTime, Integer> currentFte = toVisualization.get(i);
                    if (i == 0) {
                        writer.write(current + "\t");
                    }
                    writer.write(currentFte.get(current) + "\t");
                }
                writer.write("\n");
                
                current = current.plusMinutes(TIME_QUANT);
            }
            
            System.out.println("Данные успешно записаны в файл " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
    
    public void createBestReport(List<Map<LocalDateTime, Integer>> toVisualization, LocalInterval interval) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            
            LocalDateTime current = interval.getStart();
            while (current.isBefore(interval.getEnd())) {
                
                Map<LocalDateTime, Integer> forecast = toVisualization.get(0);
                Map<LocalDateTime, Integer> firstIteration = toVisualization.get(1);
                Map<LocalDateTime, Integer> lastIteration = toVisualization.getLast();
                
                writer.write(current + "\t");
                writer.write(forecast.get(current) + "\t");
                writer.write(firstIteration.get(current) + "\t");
                writer.write(lastIteration.get(current) + "\t");
                writer.write("\n");
                
                current = current.plusMinutes(TIME_QUANT);
            }
            
            System.out.println("Данные успешно записаны в файл " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
}
