package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.genetic.Crossing;
import edu.iate.ism22.schedule.entity.genetic.Fitness;
import edu.iate.ism22.schedule.entity.genetic.Mutation;
import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.genetic.Selection;
import edu.iate.ism22.schedule.entity.genetic.SimpleMutation;
import edu.iate.ism22.schedule.entity.genetic.TournamentSelection;
import edu.iate.ism22.schedule.entity.genetic.TwoPointsCrossing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.CYCLES;
import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

//@RequiredArgsConstructor
public class ScheduleContext {
    
    private final SchedulePopulationProvider schedulePopulationProvider;
    private final Fitness fitness;
    private final Selection selection;
    private final Crossing crossing;
    private final Mutation mutation;
    
    public ScheduleContext(SchedulePopulationProvider schedulePopulationProvider, Fitness fitness) {
        this.schedulePopulationProvider = schedulePopulationProvider;
        this.fitness = fitness;
        this.selection = new TournamentSelection();
        this.crossing = new TwoPointsCrossing();
        this.mutation = new SimpleMutation();
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
        
        for (int i = 0; i < CYCLES; i++) {
            // 2. производим отбор и дополняем новыми индивидами популяцию
            List<ScheduleIndividual> bestIndividuals = selection.apply(scoredPopulation);
//            List<ScheduleIndividual> selectedPopulation =
//                completeFuture(schedulePopulationProvider.createIndividuals(bestIndividuals, POPULATION_CAPACITY));
            
            // 3. выполняем скрещивание
            List<ScheduleIndividual> newPopulation = crossing.cross(bestIndividuals);
            
            // 4. выполняем мутацию
            mutation.apply(newPopulation);
            
            // 5. вычисляем новые значения приспособленности
            Map<ScheduleIndividual, Integer> newScoredPopulation = new HashMap<>();
            for (ScheduleIndividual individual : newPopulation) {
                newScoredPopulation.put(individual, fitness.getFitnessScore(individual));
            }
            
            // 6. выводим итерацию и текущую приспособленность индивида
            int sum = newScoredPopulation.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
            System.out.println("Iteration: " + i + ", score: " + sum);
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
