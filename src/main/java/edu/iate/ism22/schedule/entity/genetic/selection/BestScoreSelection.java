package edu.iate.ism22.schedule.entity.genetic.selection;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

public class BestScoreSelection implements Selection {
    @Override
    public List<ScheduleIndividual> apply(Map<ScheduleIndividual, Integer> scoredPopulation) {
        return scoredPopulation.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(POPULATION_CAPACITY / 2)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
