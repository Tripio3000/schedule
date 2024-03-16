package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

class ForecastFTETest {
    
    private ForecastFTE forecastFTE;
    
    @BeforeEach
    void setUp() {
        forecastFTE = new ForecastFTE();
    }
    
    @Test
    void getForecastingFTE() {
        
        Map<LocalDateTime, Integer> forecastingFTE = forecastFTE.getForecastingFTE();
//        System.out.println(forecastingFTE);
        List<LocalDateTime> list = forecastingFTE.keySet().stream().sorted().toList();
        System.out.println(list);
    }
    
    @Test
    void dateTimeTest() {
        String dateTimeString = "01.01.2024 00:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        
        System.out.println(dateTime);  // Выводим LocalDateTime
    }
}