package edu.iate.ism22.schedule.entity.genetic;

import java.util.List;
import java.util.Map;

public interface Selection {
    
    List<ScheduleIndividual> apply(Map<ScheduleIndividual, Integer> scoredPopulation);
}
