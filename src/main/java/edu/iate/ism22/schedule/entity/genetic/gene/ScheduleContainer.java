package edu.iate.ism22.schedule.entity.genetic.gene;

import java.util.List;

/**
 * Варианты смен, которые может работать сотрудник.
 */
public interface ScheduleContainer {
    
    WorkShift getRandomWorkShift();
    
    List<WorkShift> getAllWorkShifts();
}
