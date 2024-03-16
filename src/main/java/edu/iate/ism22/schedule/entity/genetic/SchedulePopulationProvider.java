package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SchedulePopulationProvider {
    
    private final List<User> users;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final ScheduleFTE scheduleFTE;
    
    public SchedulePopulationProvider(List<User> users, LocalDateTime start, LocalDateTime end, ScheduleFTE scheduleFTE) {
        this.users = users;
        this.start = start;
        this.end = end;
        this.scheduleFTE = scheduleFTE;
    }
    
    public List<Future<ScheduleIndividual>> createIndividuals(int populationCapacity) {
        
        List<Future<ScheduleIndividual>> population = new ArrayList<>(populationCapacity);
        try (ExecutorService ex = Executors.newCachedThreadPool()) {
            Callable<ScheduleIndividual> task = () -> {
                ScheduleIndividual individual = new ScheduleIndividual(users, start, end);
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
    
    public List<Future<ScheduleIndividual>> createIndividuals(List<ScheduleIndividual> bestIndividuals, int populationCapacity) {
        
        List<Future<ScheduleIndividual>> newPopulation = new ArrayList<>(populationCapacity);
        for (ScheduleIndividual best : bestIndividuals) {
            newPopulation.add(CompletableFuture.completedFuture(best));
        }
        newPopulation.addAll(
            createIndividuals(populationCapacity - bestIndividuals.size())
        );
        
        // создаем новую популяцию, с учетом лучших индивидуумов с прошлой итерации
        return newPopulation;
    }
    
}

