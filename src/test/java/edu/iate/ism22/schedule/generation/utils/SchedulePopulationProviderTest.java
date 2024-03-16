package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

class SchedulePopulationProviderTest extends ScheduleContextTestInitializer {
    
    
    @Test
    void name() {
        List<Future<ScheduleIndividual>> individuals = getPopulation();
        List<Boolean> res = individuals.stream().map(Future::isDone).toList();
        System.out.println(res);
        
        try {
            ScheduleIndividual future = individuals.getFirst().get();
            Map<LocalDateTime, Integer> fte = future.getFte();
            
            System.out.println(fte.values());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    private List<Future<ScheduleIndividual>> getPopulation() {
        ScheduleFTE fteService = new ScheduleFTE();
        
        User user1 = new Operator("user1", scheme2by2);
        User user2 = new Operator("user2", scheme5by2);
        
        SchedulePopulationProvider population = new SchedulePopulationProvider(
            List.of(user1, user2),
            LocalDateTime.parse("2024-01-01T00:00"),
            LocalDateTime.parse("2024-02-01T00:00"),
            fteService
        );
        
        List<Future<ScheduleIndividual>> individuals = population.createIndividuals(POPULATION_CAPACITY);
        return individuals;
    }
}