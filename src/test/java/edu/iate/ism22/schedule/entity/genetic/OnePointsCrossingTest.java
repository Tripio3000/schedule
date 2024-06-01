package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.genetic.crossing.Crossing;
import edu.iate.ism22.schedule.entity.genetic.crossing.OnePointsCrossing;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class OnePointsCrossingTest extends ScheduleContextTestInitializer {
    
    @Test
    void cross() throws ExecutionException, InterruptedException {
        List<Future<ScheduleIndividual>> population = getPopulation();
        Crossing crossing = new OnePointsCrossing();
        
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
        
        SchedulePopulationProvider population = new SchedulePopulationProvider(fteService);
        
        LocalInterval localInterval = new LocalInterval(
            LocalDateTime.parse("2024-03-01T00:00"),
            LocalDateTime.parse("2024-04-01T00:00")
        );
        return population.createIndividuals(20, users, localInterval);
    }
}
