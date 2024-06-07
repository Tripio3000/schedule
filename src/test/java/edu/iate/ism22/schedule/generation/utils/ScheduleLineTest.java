package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ScheduleLineTest extends ScheduleContextTestInitializer {
    
    private ScheduleLine scheduleLine;
    
    @Test
    void getScheduleLine() {
        User user = new Operator("user1", container12h);
        scheduleLine = new ScheduleLine(user, LocalDateTime.parse("2024-01-01T00:00"), LocalDateTime.parse("2024-02-01T00:00"));
        
        List<ScheduleActivity> activities = scheduleLine.getScheduleActivities();
        activities.forEach(System.out::println);
    }
}
