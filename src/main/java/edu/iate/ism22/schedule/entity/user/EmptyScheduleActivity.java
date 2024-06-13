package edu.iate.ism22.schedule.entity.user;

import edu.iate.ism22.schedule.utils.LocalInterval;

import java.time.LocalDateTime;

public class EmptyScheduleActivity extends ScheduleActivity {
    
    public EmptyScheduleActivity(User user, LocalDateTime start, LocalDateTime end) {
        super(new WorkActivity("empty", false), user, start, end);
    }
    
    public EmptyScheduleActivity(User user, LocalInterval interval) {
        super(new WorkActivity("empty", false), user, interval.getStart(), interval.getEnd());
    }
}
