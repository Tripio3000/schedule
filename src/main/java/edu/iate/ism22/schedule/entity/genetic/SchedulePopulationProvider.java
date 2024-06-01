package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import edu.iate.ism22.schedule.utils.LocalInterval;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SchedulePopulationProvider {
    private final ScheduleFTE scheduleFTE;
    
    public SchedulePopulationProvider(ScheduleFTE scheduleFTE) {
        this.scheduleFTE = scheduleFTE;
    }
    
    public List<Future<ScheduleIndividual>> createIndividuals(int populationCapacity, List<User> users, LocalInterval interval) {
        
        List<Future<ScheduleIndividual>> population = new ArrayList<>(populationCapacity);
        try (ExecutorService ex = Executors.newCachedThreadPool()) {
            Callable<ScheduleIndividual> task = () -> {
                ScheduleIndividual individual = new ScheduleIndividual(users, interval.getStart(), interval.getEnd());
                individual.setFte(scheduleFTE.fteMatrix(individual));
                return individual;
            };
            for (int i = 0; i < populationCapacity; i++) {
                Future<ScheduleIndividual> individ = ex.submit(task);
                population.add(individ);
            }
        }
        
        return population;
    }
}

