package edu.iate.ism22.schedule.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.iate.ism22.schedule.entity.dto.FteDTO;
import edu.iate.ism22.schedule.entity.dto.UserScheduleDTO;
import edu.iate.ism22.schedule.entity.forecast.CachedForecast;
import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomElitisticListPopulation;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomGeneticAlgorithm;
import edu.iate.ism22.schedule.entity.genetic.custom.MutationImpl;
import edu.iate.ism22.schedule.entity.genetic.custom.RouletteWheelSelection;
import edu.iate.ism22.schedule.entity.genetic.custom.UserScheduleChromosome;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.NPointCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleContext {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final Integer POPULATION_LIMIT = 200;
    private static final Integer CYCLES = 1000;
    private static final Double MUTATION_RATE = 0.2;
    private static final Double ELITISM_RATE = 0.01;
    
    public ScheduleContext() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    public void generate(List<User> users, LocalInterval interval) {
        
        CustomGeneticAlgorithm ga = new CustomGeneticAlgorithm(
            new NPointCrossover<List<ScheduleLine>>(2),
            0.97,
            new MutationImpl(),
            MUTATION_RATE,
            new RouletteWheelSelection()
        );
        
        Forecast<Map<LocalDateTime, Integer>> forecast = new CachedForecast(new ForecastFTE());
        Map<LocalDateTime, Integer> requestedForecast = forecast.valueFor(interval);
        
        Population initialPopulation = getInitialPopulation(users, interval, forecast);
        StoppingCondition stopCond = new FixedGenerationCount(CYCLES);
        
        // Начало гененетического алгоритма
        long startTime = System.currentTimeMillis();
        Population finalPopulation = ga.evolve(initialPopulation, stopCond);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Получаем лучший вариант расписания, записываем в файл данные для построения графиков.
        UserScheduleChromosome bestChromosome = (UserScheduleChromosome) finalPopulation.getFittestChromosome();
        fteChart(bestChromosome.getActualFte(), requestedForecast, interval);
        scheduleChart(bestChromosome.getRepresentation());
        
        System.out.println("Actual best fit: " + bestChromosome.fitness());
        System.out.println("time : " + elapsedTime);
    }
    
    private void scheduleChart(List<ScheduleLine> scheduleLines) {
        UserScheduleDTO scheduleDTO = new UserScheduleDTO(scheduleLines);
        
        try {
            objectMapper.writeValue(new File("scheduleExp1.json"), scheduleDTO.getUserSchedules());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Population getInitialPopulation(List<User> users, LocalInterval interval, Forecast<Map<LocalDateTime, Integer>> forecast) {
        List<Chromosome> chromosomes = new ArrayList<>();
        
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            List<ScheduleLine> representation = new ArrayList<>(users.size());
            for (User user : users) {
                representation.add(new ScheduleLine(user, interval.getStart(), interval.getEnd()));
            }
            chromosomes.add(new UserScheduleChromosome(representation, forecast));
        }
        
        return new CustomElitisticListPopulation(chromosomes, POPULATION_LIMIT, ELITISM_RATE);
    }
    
    private void fteChart(Map<LocalDateTime, Integer> actualFte, Map<LocalDateTime, Integer> forecastFte, LocalInterval interval) {
        
        // исключаем точки, не входящие в запрошенный интервал
        actualFte.entrySet().removeIf(entry -> entry.getKey().isBefore(interval.getStart()) || !entry.getKey().isBefore(interval.getEnd()));
        
        List<FteDTO> dtos = new ArrayList<>();
        for (Map.Entry<LocalDateTime, Integer> entry : actualFte.entrySet()) {
            dtos.add(
                new FteDTO(
                    entry.getKey(),
                    entry.getValue(),
                    forecastFte.getOrDefault(entry.getKey(), 0)
                )
            );
        }
        
        try {
            objectMapper.writeValue(new File("fte_chart.json"), dtos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
