package edu.iate.ism22.schedule.entity.genetic.gene;

import java.time.LocalDateTime;

public class EmptyScheduleActivity extends ScheduleActivity {
    
    public EmptyScheduleActivity(User user, LocalDateTime start, LocalDateTime end) {
        super(
            new WorkActivity("empty", false),
            user,
            start,
            end
        );
    }
}
