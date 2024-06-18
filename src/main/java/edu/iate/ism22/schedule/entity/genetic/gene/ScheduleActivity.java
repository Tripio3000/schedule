package edu.iate.ism22.schedule.entity.genetic.gene;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleActivity {

    private final WorkActivity workActivity;
    private final User user;
    private final LocalDateTime start;
    private final LocalDateTime end;
    
    public boolean isWork() {
        return workActivity.isWork();
    }
    
    @Override
    public String toString() {
        return "ScheduleActivity{" +
               "wa-name=" + workActivity.getName() +
               ", start=" + start +
               ", end=" + end +
               '}';
    }
}
