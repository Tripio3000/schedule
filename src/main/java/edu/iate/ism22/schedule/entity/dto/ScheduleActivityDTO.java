package edu.iate.ism22.schedule.entity.dto;

import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleActivityDTO {
    private String waName;
    private LocalDateTime start;
    private LocalDateTime end;
    
    public ScheduleActivityDTO(String waName, LocalDateTime start, LocalDateTime end) {
        this.waName = waName;
        this.start = start;
        this.end = end;
    }
    
    public ScheduleActivityDTO(ScheduleActivity activities) {
        this.waName = activities.getWorkActivity().getName();
        this.start = activities.getStart();
        this.end = activities.getEnd();
    }
}
