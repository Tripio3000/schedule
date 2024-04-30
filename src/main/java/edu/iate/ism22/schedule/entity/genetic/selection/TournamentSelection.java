package edu.iate.ism22.schedule.entity.genetic.selection;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static edu.iate.ism22.schedule.generation.Constants.TOUR_SIZE;

/**
 * В каждом раунде турнирного отбора из популяции выбираются два или более индивидуумов (определяется переменной TOUR_SIZE),
 * и тот, у кого приспособленность больше, выигрывает и отбирается в следующее поколение.
 */
public class TournamentSelection implements Selection {
    @Override
    public List<ScheduleIndividual> apply(Map<ScheduleIndividual, Integer> scoredPopulation) {
        
        List<ScheduleIndividual> result = new ArrayList<>();
        
        List<ScheduleIndividual> population = new ArrayList<>(scoredPopulation.keySet());
        Collections.shuffle(population);
        
        for (int i = 0; i < scoredPopulation.size(); i += TOUR_SIZE) {
            List<ScheduleIndividual> currentTour = new ArrayList<>(TOUR_SIZE);
            for (int j = i; j < i + TOUR_SIZE && j < scoredPopulation.size(); j++) {
                currentTour.add(population.get(j));
            }
            ScheduleIndividual tourWinner = Collections.max(currentTour, Comparator.comparing(scoredPopulation::get));
            result.add(tourWinner);
        }
        
        return result;
    }
}
