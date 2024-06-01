package edu.iate.ism22.schedule.entity.genetic;

import java.time.LocalDateTime;
import java.util.Map;

public interface Fitness {
    
    Integer getFitnessScore(ScheduleIndividual individual, Map<LocalDateTime, Integer> forecastingFTE);
}
