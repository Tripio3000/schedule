package edu.iate.ism22.schedule.entity.genetic.custom;

import edu.iate.ism22.schedule.entity.forecast.Forecast;
import edu.iate.ism22.schedule.entity.genetic.DaySchedule;
import edu.iate.ism22.schedule.entity.user.ScheduleVariant;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserScheduleChromosome extends AbstractListChromosome<DaySchedule> {
    
    private final Forecast<Map<LocalDateTime, Integer>> forecast;
    private static final long TIME_QUANT = 60;
    
    public UserScheduleChromosome(List<DaySchedule> representation, Forecast<Map<LocalDateTime, Integer>> forecast)
        throws InvalidRepresentationException {
        super(representation);
        this.forecast = forecast;
    }
    
    @Override
    protected void checkValidity(List<DaySchedule> chromosomeRepresentation) throws InvalidRepresentationException {
    
    }
    
    @Override
    public AbstractListChromosome<DaySchedule> newFixedLengthChromosome(List<DaySchedule> chromosomeRepresentation) {
        return new UserScheduleChromosome(chromosomeRepresentation, forecast);
    }
    
    @Override
    public List<DaySchedule> getRepresentation() {
        return super.getRepresentation();
    }
    
    @Override
    protected boolean isSame(Chromosome another) {
        return super.isSame(another);
    }
    
    private Map<LocalDateTime, Integer> initFteMap(LocalInterval interval) {
        Map<LocalDateTime, Integer> fteMap =
            new HashMap<>((int) ChronoUnit.DAYS.between(interval.getStart().toLocalDate(), interval.getEnd().toLocalDate()));
        LocalDateTime current = interval.getStart();
        LocalDateTime endDateTime = interval.getEnd();
        while (current.isBefore(endDateTime)) {
            fteMap.put(current, 0);
            current = current.plusMinutes(TIME_QUANT);
        }
        
        return fteMap;
    }
    
    public Map<LocalDateTime, Integer> getActualFte(LocalInterval requestedInterval) {
        Map<LocalDateTime, Integer> fteMap = initFteMap(requestedInterval);
        for (DaySchedule daySchedule : getRepresentation()) {
            LocalDate currentDate = daySchedule.getDate();
            List<WorkShift> workShifts = daySchedule.getWorkShifts();
            for (WorkShift workShift : workShifts) {
                if (!workShift.isWorkShift()) {
                    continue;
                }
                
                for (ScheduleVariant activity : workShift.getActivities()) {
                    if (!activity.getWorkActivity().isWork()) {
                        continue;
                    }
                    LocalDateTime currentDateTime = LocalDateTime.of(currentDate, activity.getStart());
                    LocalDateTime endDateTime;
                    if (!activity.getStart().isBefore(activity.getEnd())) {
                        endDateTime = LocalDateTime.of(currentDate.plusDays(1), activity.getEnd());
                    } else {
                        endDateTime = LocalDateTime.of(currentDate, activity.getEnd());
                    }
                    
                    while (currentDateTime.isBefore(endDateTime)) {
                        fteMap.merge(currentDateTime, 1, Integer::sum);
                        currentDateTime = currentDateTime.plusMinutes(TIME_QUANT);
                    }
                }
            }
        }
        
        return fteMap;
    }
    
    @Override
    public double fitness() {
        List<DaySchedule> representation = getRepresentation();
        LocalDate firstDate = representation.getFirst().getDate();
        LocalDate lastDate = representation.getLast().getDate();
        
        Map<LocalDateTime, Integer> actualFte = getActualFte(new LocalInterval(firstDate.atStartOfDay(), lastDate.atStartOfDay().plusDays(1)));
        Map<LocalDateTime, Integer> forecastFte = forecast.valueFor(new LocalInterval(firstDate.atStartOfDay(), lastDate.atStartOfDay().plusDays(2)));
        
        double fit = 0;
        for (LocalDateTime dateTime : actualFte.keySet()) {
            Integer currentActualFte = actualFte.get(dateTime);
            Integer currentForecastFte = forecastFte.get(dateTime);
            if (currentActualFte == null || currentForecastFte == null) {
                throw new IllegalArgumentException(currentActualFte + " - " + currentForecastFte);
            }
//            fit += Math.pow((currentActualFte - currentForecastFte), 2);
//            fit += Math.abs(currentActualFte - currentForecastFte);
            fit += Math.abs(currentActualFte - currentForecastFte) / (double) currentForecastFte;
        }
//        fit = fit / actualFte.size();
        
        // увеличивает фит, если в гене есть смены, которые пересекаются между собой.
        for ( int i = 0; i < representation.size() - 1; i++ ) {
            List<WorkShift> currentDayWorkShifts = representation.get(i).getWorkShifts();
            List<WorkShift> nextDayWorkShifts = representation.get(i + 1).getWorkShifts();
            for ( int j = 0; j < nextDayWorkShifts.size(); j++ ) {
                if (!currentDayWorkShifts.get(j).isWorkShift() || !nextDayWorkShifts.get(j).isWorkShift()) {
                    continue;
                }
                if (!currentDayWorkShifts.get(j).getEndTime().isBefore(nextDayWorkShifts.get(j).getStartTime())) {
                    fit += 1;
                }
            }
        }
        
        return fit;
    }
}
