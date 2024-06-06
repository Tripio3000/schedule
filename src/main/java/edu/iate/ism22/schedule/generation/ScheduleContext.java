package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.forecast.CachedForecast;
import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomElitisticListPopulation;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomGeneticAlgorithm;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomTournamentSelection;
import edu.iate.ism22.schedule.entity.genetic.custom.MutationImpl;
import edu.iate.ism22.schedule.entity.genetic.custom.UserScheduleChromosome;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.NPointCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleContext {
    
    private static final Integer POPULATION_LIMIT = 20;
    private static final Integer CYCLES = 30;
    private static final Double ELITISM_RATE = 0.02;
    
    public ScheduleContext() {
    
    }
    
    public void generate(List<User> users, LocalInterval interval) {
        
        CustomGeneticAlgorithm ga = new CustomGeneticAlgorithm(
            new NPointCrossover<List<ScheduleLine>>(2),
            0.97,
            new MutationImpl(),
            0.15,
            new CustomTournamentSelection(5)
        );
        
        Forecast<Map<LocalDateTime, Integer>> forecast = new CachedForecast(new ForecastFTE());
        
//        int sum1 = forecast.valueFor(interval).entrySet().stream()
//            .filter(entry -> entry.getKey().isAfter(interval.getStart()) && entry.getKey().isBefore(interval.getEnd()))
//            .mapToInt(Map.Entry::getValue)
//            .sum();
//        System.out.println(sum1);
        Population initialPopulation = getInitialPopulation(users, interval, forecast);
        UserScheduleChromosome initialChromosome = (UserScheduleChromosome) initialPopulation.getFittestChromosome();
        initialChromosome.getActualFte().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
        System.out.println("Initial best fit: " + initialChromosome.fitness());

        StoppingCondition stopCond = new FixedGenerationCount(CYCLES);

        System.out.println("Начало ген.алгоритма: ");
        Population finalPopulation = ga.evolve(initialPopulation, stopCond);

        UserScheduleChromosome bestChromosome = (UserScheduleChromosome) finalPopulation.getFittestChromosome();


        bestChromosome.getActualFte().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));

//        List<Integer> sortedValues = bestChromosome.getActualFte().entrySet().stream()
//            .sorted(Map.Entry.comparingByKey())
//            .map(Map.Entry::getValue)
//            .toList();
//        System.out.println("Actual best fte: " + sortedValues);
        System.out.println("Actual best fit: " + bestChromosome.fitness());
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
    
}
