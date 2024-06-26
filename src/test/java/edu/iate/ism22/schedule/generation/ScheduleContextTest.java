package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ScheduleContextTest extends ScheduleContextTestInitializer {
    
    @Test
    void generate() {
        
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            users.add(new Operator("user" + i, scheme2by2));
        }
        for (int i = 0; i < 150; i++) {
            users.add(new Operator("user" + i, scheme5by2));
        }
        
        ScheduleContext scheduleContext = new ScheduleContext();
        
        LocalInterval interval = new LocalInterval(
            LocalDateTime.parse("2024-08-01T00:00"),
            LocalDateTime.parse("2024-08-14T00:00")
        );
        scheduleContext.generate(users, interval);
        
        assertEquals()
    }
    
}