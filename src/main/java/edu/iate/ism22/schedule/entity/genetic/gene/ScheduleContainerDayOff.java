package edu.iate.ism22.schedule.entity.genetic.gene;

import java.util.Collections;
import java.util.List;

/**
 * Смена для выходного дня. Содержит в себе смену с пустым списком активностей.
 */
public class ScheduleContainerDayOff implements ScheduleContainer {
    
    private final WorkShift dayOffWorkShift;
    
    public ScheduleContainerDayOff() {
        this.dayOffWorkShift = new WorkShift(Collections.emptyList());
    }
    
    @Override
    public WorkShift getRandomWorkShift() {
        return dayOffWorkShift;
    }
    
    @Override
    public List<WorkShift> getAllWorkShifts() {
        return List.of(dayOffWorkShift);
    }
}
