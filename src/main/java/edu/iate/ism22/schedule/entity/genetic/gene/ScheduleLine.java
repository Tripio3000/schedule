package edu.iate.ism22.schedule.entity.genetic.gene;

import edu.iate.ism22.schedule.exception.EmptyScheduleContainer;
import edu.iate.ism22.schedule.exception.GenerationIntervalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ScheduleLine {
    private final User user;
    private final LocalDateTime start;
    private final LocalDateTime end;
    
    private List<ScheduleActivity> scheduleActivities = new ArrayList<>();
    
    public List<ScheduleActivity> getScheduleActivities() {
        if (scheduleActivities.isEmpty()) {
            scheduleActivities = generateScheduleLine();
        }
        return scheduleActivities;
    }
    
    private List<ScheduleActivity> generateScheduleLine() {
        if (start.isAfter(end)) {
            throw new GenerationIntervalException("Start date is after end date.");
        }
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        
        List<ScheduleActivity> scheduleLine = new ArrayList<>();
        
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            if (user.getScheduleContainer().getAllWorkShifts().isEmpty()) {
                throw new EmptyScheduleContainer("Schedule container is empty.");
            }
            
            WorkShift nextShift = user.getScheduleContainer().getRandomWorkShift();
            if (nextShift.isWorkShift()) {
                fillScheduleLineWithWorkShift(scheduleLine, nextShift, currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        fillEmptyActivities(scheduleLine);
        
        return scheduleLine;
    }
    
    private void fillScheduleLineWithWorkShift(List<ScheduleActivity> scheduleLine, WorkShift workShift, LocalDate currentDate) {
        for (ScheduleVariant variant : workShift.getActivities()) {
            LocalDateTime dateStart = LocalDateTime.of(currentDate, variant.getStart());
            if (!variant.getStart().isBefore(variant.getEnd())) {
                currentDate = currentDate.plusDays(1);
            }
            LocalDateTime dateEnd = LocalDateTime.of(currentDate, variant.getEnd());
            scheduleLine.add(new ScheduleActivity(variant.getWorkActivity(), user, dateStart, dateEnd));
        }
    }
    
    private void fillEmptyActivities(List<ScheduleActivity> shifts) {
        if (shifts.isEmpty()) {
            shifts.add(new EmptyScheduleActivity(user, start, end));
        }
        if (start.isBefore(shifts.getFirst().getStart())) {
            shifts.addFirst(new EmptyScheduleActivity(user, start, shifts.getFirst().getStart()));
        }
        if (end.isAfter(shifts.getLast().getEnd())) {
            shifts.addLast(new EmptyScheduleActivity(user, shifts.getLast().getEnd(), end));
        }
        
        for (int i = 0; i < shifts.size() - 1; i++) {
            LocalDateTime currentEnd = shifts.get(i).getEnd();
            LocalDateTime nextStart = shifts.get(i + 1).getStart();
            if (currentEnd.isBefore(nextStart)) {
                shifts.add(i + 1, new EmptyScheduleActivity(user, currentEnd, nextStart));
            }
        }
    }
}