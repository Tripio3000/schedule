package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class TwoPointsCrossingTest extends ScheduleContextTestInitializer {
    
    @Test
    void cross() throws ExecutionException, InterruptedException {
        List<Future<ScheduleIndividual>> population = getPopulation();
        Crossing crossing = new TwoPointsCrossing();
        
        List<ScheduleIndividual> resolved = new ArrayList<>();
        for (Future<ScheduleIndividual> future : population) {
            resolved.add(future.get());
        }
        
        System.out.println(resolved);
        
        System.out.println("-------------------------------------------------");
        crossing.cross(resolved);
        
        System.out.println(resolved);
    }
    
    private List<Future<ScheduleIndividual>> getPopulation() {
        ScheduleFTE fteService = new ScheduleFTE();
        
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new Operator("user" + i, scheme2by2));
        }
        
        SchedulePopulationProvider population = new SchedulePopulationProvider(
            users,
            LocalDateTime.parse("2024-03-01T00:00"),
            LocalDateTime.parse("2024-04-01T00:00"),
            fteService
        );
        
        return population.createIndividuals(20);
    }
}
