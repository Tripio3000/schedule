package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.MUTATION_CHANCE;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleMutationTest extends ScheduleContextTestInitializer {
    
    @Test
    void mutation() throws ExecutionException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        List<Future<ScheduleIndividual>> population = getPopulation();
        Mutation mutation = new SimpleMutation();
        
        List<ScheduleIndividual> resolved = new ArrayList<>();
        for (Future<ScheduleIndividual> future : population) {
            resolved.add(future.get());
        }
        
        mutation.apply(resolved);
        
        for (int i = 0; i < (int) (MUTATION_CHANCE * resolved.size()); i++) {
            
            List<ScheduleLine> scheduleLines = resolved.getFirst().getScheduleLines();
            
            BitSet bitSet = new BitSet(scheduleLines.size());
            for (int j = 0; j < scheduleLines.size(); j++) {
                ScheduleLine line = scheduleLines.get(j);
                Field cachedField = line.getClass().getDeclaredField("cached");
                cachedField.setAccessible(true);
                List<ScheduleActivity> cached = (List<ScheduleActivity>) cachedField.get(line);
                bitSet.set(j, cached.isEmpty());
            }
            
            assertTrue(bitSet.stream().anyMatch(v -> true));
        }
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
