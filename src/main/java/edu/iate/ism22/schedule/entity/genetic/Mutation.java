package edu.iate.ism22.schedule.entity.genetic;

import java.util.List;

import static edu.iate.ism22.schedule.generation.Constants.MUTATION_CHANCE;

public interface Mutation {
    
    void apply(List<ScheduleIndividual> population);
    
    default int skipIndividuals(int populationSize) {
        return (int) (populationSize * MUTATION_CHANCE);
    }
}
