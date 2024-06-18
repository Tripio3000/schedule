package edu.iate.ism22.schedule.entity.genetic.gene;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleVariant {
    
    private final WorkActivity workActivity;
    private final LocalTime start;
    private final LocalTime end;
    
}
