package edu.iate.ism22.schedule.entity.genetic.custom;

import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserScheduleChromosome extends AbstractListChromosome<ScheduleLine> {
    
    private final Forecast<Map<LocalDateTime, Integer>> forecast;
    private static final long TIME_QUANT = 60;
    
    public UserScheduleChromosome(List<ScheduleLine> representation, Forecast<Map<LocalDateTime, Integer>> forecast)
        throws InvalidRepresentationException {
        super(representation);
        this.forecast = forecast;
    }
    
    @Override
    protected void checkValidity(List<ScheduleLine> chromosomeRepresentation) throws InvalidRepresentationException {
    
    }
    
    @Override
    public AbstractListChromosome<ScheduleLine> newFixedLengthChromosome(List<ScheduleLine> chromosomeRepresentation) {
        return new UserScheduleChromosome(chromosomeRepresentation, forecast);
    }
    
    @Override
    protected List<ScheduleLine> getRepresentation() {
        return super.getRepresentation();
    }
    
    @Override
    public double fitness() {
        Map<LocalDateTime, Integer> actualFte = getActualFte();
        
        ScheduleLine firstLine = getRepresentation().getFirst();
        LocalInterval requestedInterval = new LocalInterval(firstLine.getStart(), firstLine.getEnd());
        
        Map<LocalDateTime, Integer> forecastFte = forecast.valueFor(requestedInterval);
        
        double fit = 0;
        for (LocalDateTime dateTime : actualFte.keySet()) {
            Integer currentActualFte = actualFte.get(dateTime);
            Integer currentForecastFte = forecastFte.get(dateTime);
            if (currentActualFte == null || currentForecastFte == null) {
                throw new IllegalArgumentException(currentActualFte + " - " + currentForecastFte);
            }
            fit += Math.abs(currentActualFte - currentForecastFte) / (double) currentForecastFte;
        }
        
        return fit;
    }
    
    public Map<LocalDateTime, Integer> getActualFte() {
        Map<LocalDateTime, Integer> actualFte = new HashMap<>();
        for (ScheduleLine line : getRepresentation()) {
            
            for (ScheduleActivity activity : line.getScheduleActivities()) {
                
                LocalDateTime current = activity.getStart();
                while (current.isBefore(activity.getEnd())) {
                    
                    actualFte.merge(current, activity.isWork() ? 1 : 0, Integer::sum);
                    current = current.plusMinutes(TIME_QUANT);
                }
            }
        }
        
        return actualFte;
    }
}
