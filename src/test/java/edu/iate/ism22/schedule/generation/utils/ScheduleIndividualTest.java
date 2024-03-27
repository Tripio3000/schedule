package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ScheduleIndividualTest extends ScheduleContextTestInitializer {
    
    private ScheduleIndividual scheduleIndividual;
    
    @Test
    void getScheduleLines() {
        User user1 = new Operator("user1", scheme2by2);
        User user2 = new Operator("user2", scheme5by2);
        
        scheduleIndividual = new ScheduleIndividual(
            List.of(user1, user2),
            LocalDateTime.parse("2024-01-01T00:00"),
            LocalDateTime.parse("2024-01-31T23:59")
        );
        
        List<List<ScheduleActivity>> scheduleLines =
            scheduleIndividual.getScheduleLines().stream().map(ScheduleLine::getScheduleLine).toList();
        System.out.println(scheduleLines);
    }
    
}
