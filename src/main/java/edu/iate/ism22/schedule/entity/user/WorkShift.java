package edu.iate.ism22.schedule.entity.user;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 * Абстрактная рабочая смена, без привязки к дате.
 * Определяет количество и тип активностей, из которых состоит смена.
 */
@Data
public class WorkShift {
    
    private final List<ScheduleVariant> activities;
    
    public boolean isWorkShift() {
        return !activities.isEmpty();
    }
    
    public LocalTime getStartTime() {
        return activities.getFirst().getStart();
    }
    
    public LocalTime getEndTime() {
        return activities.getLast().getEnd();
    }
}
