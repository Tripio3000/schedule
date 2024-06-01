package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.utils.LocalInterval;
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
        
        LocalInterval interval = new LocalInterval(
            LocalDateTime.parse("2024-01-01T00:00"),
            LocalDateTime.parse("2024-02-01T00:00")
        );
        Map<LocalDateTime, Integer> forecastingFTE = forecastFTE.valueFor(interval);

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