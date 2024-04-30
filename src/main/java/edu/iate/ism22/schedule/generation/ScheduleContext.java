package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.genetic.Fitness;
import edu.iate.ism22.schedule.entity.genetic.Mutation;
import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.genetic.SimpleMutation;
import edu.iate.ism22.schedule.entity.genetic.crossing.Crossing;
import edu.iate.ism22.schedule.entity.genetic.crossing.OnePointsCrossing;
import edu.iate.ism22.schedule.entity.genetic.selection.Selection;
import edu.iate.ism22.schedule.entity.genetic.selection.TournamentSelection;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

public class ScheduleContext {
    
    private final SchedulePopulationProvider schedulePopulationProvider;
    private final Fitness fitness;
    private final Selection selection;
    private final Crossing crossing;
    private final Mutation mutation;
    private final ScheduleFTE scheduleFTE;
    
    public ScheduleContext(SchedulePopulationProvider schedulePopulationProvider, Fitness fitness) {
        this.schedulePopulationProvider = schedulePopulationProvider;
        this.fitness = fitness;
//        this.selection = new BestScoreSelection();
        this.selection = new TournamentSelection();
//        this.crossing = new SaveBestIndividsCrossing();
        this.crossing = new OnePointsCrossing();
        this.mutation = new SimpleMutation();
        this.scheduleFTE = new ScheduleFTE();
    }
    
    /**
     * Отбор: из популяции отбираем N лучших индивидов. Для отбора лучших индивидов сортируем их по score в мапев
     * Map<ScheduleIndividual, Integer> scoreMap.
     * <p>
     * Скрещивание: ScheduleIndividual состоит из списка ScheduleLine (расписание для одного пользователя). Их и будем скрещивать.
     * <p>
     * Мутации: для случайного пользователя в ScheduleIndividual генерируем новое расписание.
     * <p>
     * Элитизм: K лучших индивидов копируются в новое поколение без изменений.
     */
    public void generate() {
        
        /**
         * 1. создаем начальную популяцию (поколение 0) и вычисляем приспособленность для каждой (по фте)
         * 2. заходим в цикл от 0 до CYCLE_NUM и выполняем следующие действия:
         */
        
        // 1. создаем начальную популяцию (поколение 0) и вычисляем приспособленность для каждой (по фте)
        Map<ScheduleIndividual, Integer> scoredPopulation = new HashMap<>();
        for (Future<ScheduleIndividual> future : schedulePopulationProvider.createIndividuals(POPULATION_CAPACITY)) {
            try {
                ScheduleIndividual individual = future.get();
                scoredPopulation.put(individual, fitness.getFitnessScore(individual));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        for (int i = 0; i < 200; i++) {
            // 2. производим отбор и дополняем новыми индивидами популяцию
            List<ScheduleIndividual> bestIndividuals = selection.apply(scoredPopulation);
            
            // 3. выполняем скрещивание
            List<ScheduleIndividual> newPopulation = crossing.cross(bestIndividuals);
            
            // 4. выполняем мутацию
            mutation.apply(newPopulation);
            
            // 5. пересчет fte
            newPopulation.forEach(ind -> ind.setFte(scheduleFTE.fteMatrix(ind)));
            
            // 6. вычисляем новые значения приспособленности
            Map<ScheduleIndividual, Integer> newScoredPopulation = new HashMap<>();
            for (ScheduleIndividual individual : newPopulation) {
                newScoredPopulation.put(individual, fitness.getFitnessScore(individual));
            }
            scoredPopulation = newScoredPopulation;
            
            // 7. выводим итерацию и текущую приспособленность индивида
            int sum = scoredPopulation.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
            Integer first = scoredPopulation.values().stream().sorted().toList().getFirst();
            System.out.println("Iteration: " + i + ", score: " + sum + ", best: " + first);
        }
    }
    
    private List<ScheduleIndividual> completeFuture(List<Future<ScheduleIndividual>> population) {
        List<ScheduleIndividual> completed = new ArrayList<>(population.size());
        for (Future<ScheduleIndividual> future : population) {
            try {
                completed.add(future.get());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return completed;
    }
}
