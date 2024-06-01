package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.forecast.CachedForecast;
import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.entity.genetic.Fitness;
import edu.iate.ism22.schedule.entity.genetic.FitnessByFTE;
import edu.iate.ism22.schedule.entity.genetic.Mutation;
import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.genetic.SimpleMutation;
import edu.iate.ism22.schedule.entity.genetic.crossing.Crossing;
import edu.iate.ism22.schedule.entity.genetic.crossing.OnePointsCrossing;
import edu.iate.ism22.schedule.entity.genetic.selection.Selection;
import edu.iate.ism22.schedule.entity.genetic.selection.TournamentSelection;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import edu.iate.ism22.schedule.utils.LocalInterval;
import edu.iate.ism22.schedule.utils.ReportService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

public class ScheduleContext {
    
    private final SchedulePopulationProvider schedulePopulationProvider;
    private final Forecast<Map<LocalDateTime, Integer>> forecast;
    private final Fitness fitness;
    private final Selection selection;
    private final Crossing crossing;
    private final Mutation mutation;
    private final ScheduleFTE scheduleFTE;
    private final ReportService reportService;
    
    public ScheduleContext() {
        this.scheduleFTE = new ScheduleFTE();
        this.schedulePopulationProvider = new SchedulePopulationProvider(scheduleFTE);
        this.forecast = new CachedForecast(new ForecastFTE());
        this.fitness = new FitnessByFTE();
//        this.selection = new BestScoreSelection();
        this.selection = new TournamentSelection();
//        this.crossing = new SaveBestIndividsCrossing();
        this.crossing = new OnePointsCrossing();
        this.mutation = new SimpleMutation();
        this.reportService = new ReportService();
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
    public void generate(List<User> users, LocalInterval interval) {
        Map<LocalDateTime, Integer> forecastByInterval = forecast.valueFor(interval);
        
        // 1. создаем начальную популяцию (поколение 0) и вычисляем приспособленность для каждой (по фте)
        Map<ScheduleIndividual, Integer> scoredPopulation = new HashMap<>();
        for (Future<ScheduleIndividual> future : schedulePopulationProvider.createIndividuals(POPULATION_CAPACITY, users, interval)) {
            try {
                ScheduleIndividual individual = future.get();
                scoredPopulation.put(individual, fitness.getFitnessScore(individual, forecastByInterval));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        // список лучших особей в каждой итерации (для визуализации)
        List<Map<LocalDateTime, Integer>> bestIndividualsPerIteration = new ArrayList<>();
        bestIndividualsPerIteration.add(forecastByInterval);
        
        // основной цикл алгоритма
        for (int i = 0; i < 100; i++) {
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
            Integer minScore = Integer.MAX_VALUE;
            Map<LocalDateTime, Integer> toVisualization = Collections.emptyMap();
            for (ScheduleIndividual individual : newPopulation) {
                Integer currentScore = fitness.getFitnessScore(individual, forecastByInterval);
                if (currentScore < minScore) {
                    minScore = currentScore;
                    toVisualization = individual.getFte();
                }
                newScoredPopulation.put(individual, currentScore);
            }
            bestIndividualsPerIteration.add(toVisualization);
            scoredPopulation = newScoredPopulation;
            
            // 7. выводим итерацию и текущую приспособленность индивида
            int sum = scoredPopulation.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
            System.out.println("Iteration: " + i + ", score: " + sum + ", best: " + minScore);
        }
        
        reportService.createBestReport(bestIndividualsPerIteration, interval);
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
