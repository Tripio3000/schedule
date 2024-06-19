package edu.iate.ism22.schedule.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.iate.ism22.schedule.dto.FteDTO;
import edu.iate.ism22.schedule.dto.UserScheduleDTO;
import edu.iate.ism22.schedule.entity.forecast.CachedForecast;
import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.entity.genetic.DaySchedule;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomElitisticListPopulation;
import edu.iate.ism22.schedule.entity.genetic.custom.CustomGeneticAlgorithm;
import edu.iate.ism22.schedule.entity.genetic.custom.MutationImpl;
import edu.iate.ism22.schedule.entity.genetic.custom.RouletteWheelSelection;
import edu.iate.ism22.schedule.entity.genetic.custom.UserScheduleChromosome;
import edu.iate.ism22.schedule.entity.user.EmptyScheduleActivity;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.ScheduleVariant;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.NPointCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleContext {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final Integer POPULATION_LIMIT = 200;
    private static final Integer CYCLES = 1000;
    private static final Double ELITISM_RATE = 0.01;
    private static final Double MUTATION_RATE = 0.25;
    
    public ScheduleContext() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    public void generate(List<User> users, LocalInterval interval) throws IOException {
        
        CustomGeneticAlgorithm ga = new CustomGeneticAlgorithm(
            new NPointCrossover<>(2),
            0.97,
            new MutationImpl(),
            MUTATION_RATE,
            new RouletteWheelSelection()
        );
        
        // получение прогноза
        Forecast<Map<LocalDateTime, Integer>> forecast = new CachedForecast(new ForecastFTE());
        Map<LocalDateTime, Integer> requestedForecast = forecast.valueFor(interval);
        
        Population initialPopulation = getInitialPopulation(users, interval, forecast);
        StoppingCondition stopCond = new FixedGenerationCount(CYCLES);
        
        // начало генетического алгоритма
        long startTime = System.currentTimeMillis();
        Population finalPopulation = ga.evolve(initialPopulation, stopCond);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // получаем лучший вариант расписания, записываем в файл данные для построения графиков.
        UserScheduleChromosome bestChromosome = (UserScheduleChromosome) finalPopulation.getFittestChromosome();
        Map<LocalDateTime, Integer> bestChromosomeActualFte = bestChromosome.getActualFte(interval);
        fteChart(bestChromosomeActualFte, requestedForecast, interval);
        scheduleChart(bestChromosome.getRepresentation(), interval);
        
        System.out.println("Actual best fit: " + bestChromosome.fitness());
        System.out.println("time : " + elapsedTime);
    }
    
    private Population getInitialPopulation(List<User> users, LocalInterval interval, Forecast<Map<LocalDateTime, Integer>> forecast) {
        List<Chromosome> chromosomes = new ArrayList<>();
        
        LocalDate startDay = interval.getStart().toLocalDate();
        LocalDate endDate = interval.getEnd().toLocalDate();
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            
            List<DaySchedule> representation = new ArrayList<>(
                (int) ChronoUnit.DAYS.between(startDay, endDate)
            );
            
            LocalDate currentDay = startDay;
            while (currentDay.isBefore(endDate)) {
                representation.add(new DaySchedule(currentDay, users));
                currentDay = currentDay.plusDays(1);
            }
            
            chromosomes.add(new UserScheduleChromosome(representation, forecast));
        }
        
        return new CustomElitisticListPopulation(chromosomes, POPULATION_LIMIT, ELITISM_RATE);
    }
    
    private void scheduleChart(List<DaySchedule> representation, LocalInterval interval) {
        
        // формируем итоговый вариант расписания (из List<DaySchedule> в List<ScheduleActivity>).
        List<ScheduleActivity> schedule = new ArrayList<>();
        for (DaySchedule daySchedule : representation) {
            LocalDate currentDate = daySchedule.getDate();
            List<WorkShift> workShifts = daySchedule.getWorkShifts();
            List<User> users = daySchedule.getUsers();
            
            for (int i = 0; i < workShifts.size(); i++) {
                
                WorkShift workShift = workShifts.get(i);
                if (!workShift.isWorkShift()) {
                    continue;
                }
                
                for (ScheduleVariant activity : workShift.getActivities()) {
                    LocalDateTime startDateTime = LocalDateTime.of(currentDate, activity.getStart());
                    LocalDateTime endDateTime;
                    if (!activity.getStart().isBefore(activity.getEnd())) {
                        endDateTime = LocalDateTime.of(currentDate.plusDays(1), activity.getEnd());
                    } else {
                        endDateTime = LocalDateTime.of(currentDate, activity.getEnd());
                    }
                    schedule.add(new ScheduleActivity(activity.getWorkActivity(), users.get(i), startDateTime, endDateTime));
                }
            }
        }
        
        // группировка расписания по пользователям и заполнение всех пропусков пустыми активностями.
        Map<User, List<ScheduleActivity>> scheduleByUser = schedule.stream()
            .collect(
                Collectors.groupingBy(
                    ScheduleActivity::getUser,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> fillEmptyActivities(list, interval)
                    )
                )
            );
        
        // запись данных в файл
        UserScheduleDTO dto = new UserScheduleDTO(scheduleByUser);
        try {
            objectMapper.writeValue(new File("schedule.json"), dto.getUserSchedules());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    
    private List<ScheduleActivity> fillEmptyActivities(List<ScheduleActivity> activities, LocalInterval interval) {
        
        // метод вызывается во время группировки по User, поэтому у всех элементов в списке будет один и тот же пользователь.
        User user = activities.getFirst().getUser();
        LocalDateTime start = interval.getStart();
        LocalDateTime end = interval.getEnd();
        
        if (activities.isEmpty()) {
            activities.add(new EmptyScheduleActivity(user, interval));
        }
        if (start.isBefore(activities.getFirst().getStart())) {
            activities.addFirst(new EmptyScheduleActivity(user, start, activities.getFirst().getStart()));
        }
        if (end.isAfter(activities.getLast().getEnd())) {
            activities.addLast(new EmptyScheduleActivity(user, activities.getLast().getEnd(), end));
        }
        
        for (int i = 0; i < activities.size() - 1; i++) {
            LocalDateTime currentEnd = activities.get(i).getEnd();
            LocalDateTime nextStart = activities.get(i + 1).getStart();
            if (currentEnd.isBefore(nextStart)) {
                activities.add(i + 1, new EmptyScheduleActivity(user, currentEnd, nextStart));
            }
        }
        
        return activities;
    }
}
