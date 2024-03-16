package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.exception.ForecastException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FitnessByFTE implements Fitness {
    
    private final ForecastFTE forecastFTE;
    
    @Override
    public Integer getFitnessScore(ScheduleIndividual individual) {
        Map<LocalDateTime, Integer> fteByDate = individual.getFte();
        if (fteByDate.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        
        // TODO добавить к методу getForecastingFTE даты выборки прогноза (from, to)
        Map<LocalDateTime, Integer> forecastingFTE = forecastFTE.getForecastingFTE();
        List<LocalDateTime> list = fteByDate.keySet().stream().sorted().toList();
        
        int score = 0;
        for (LocalDateTime currentDT : list) {
            if (!forecastingFTE.containsKey(currentDT)) {
                throw new ForecastException("There is no forecast for the date: " + currentDT);
            }
            score += Math.abs(forecastingFTE.get(currentDT) - fteByDate.get(currentDT));
        }
        
        return score;
    }
}
