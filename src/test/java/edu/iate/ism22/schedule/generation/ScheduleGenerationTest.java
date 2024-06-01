package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

class ScheduleGenerationTest extends ScheduleContextTestInitializer {
    
    @Test
    void parallelExecution_test() {
        List<Future<ScheduleIndividual>> individuals = getPopulation();
        List<Boolean> res = individuals.stream().map(Future::isDone).toList();
        System.out.println(res);
        
        try {
            ScheduleIndividual future = individuals.getFirst().get();
            Map<LocalDateTime, Integer> fte = future.getFte();
            List<LocalDateTime> list = fte.entrySet().stream().filter(e -> e.getValue() == 0).map(Map.Entry::getKey).sorted().toList();
            System.out.println(fte.values());
            System.out.println("------------------------------------------------");
            System.out.println(list);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void checkThreads() {
        for (Future<ScheduleIndividual> future : getPopulation()) {
            try {
                ScheduleIndividual individual = future.get();
//                System.out.println(Thread.currentThread().getName());
            
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private List<Future<ScheduleIndividual>> getPopulation() {
        ScheduleFTE fteService = new ScheduleFTE();
        
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            users.add(new Operator("user" + i, scheme2by2));
        }
        for (int i = 0; i < 25; i++) {
            users.add(new Operator("user" + i, scheme5by2));
        }
        
        SchedulePopulationProvider population = new SchedulePopulationProvider(fteService);
        
        LocalInterval localInterval = new LocalInterval(
            LocalDateTime.parse("2024-03-01T00:00"),
            LocalDateTime.parse("2024-04-01T00:00")
        );
        return population.createIndividuals(POPULATION_CAPACITY, users, localInterval);
    }
}