package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.genetic.gene.Operator;
import edu.iate.ism22.schedule.entity.genetic.gene.User;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ScheduleContextTest extends ScheduleContextTestInitializer {
    
    @Test
    void generate() {
        
        List<User> users = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            users.add(new Operator("user" + i, container8h));
//        }
        for (int i = 0; i < 19; i++) {
            users.add(new Operator("user" + i, container12h));
        }
        
        ScheduleContext scheduleContext = new ScheduleContext();
        
        LocalInterval interval = new LocalInterval(
            LocalDateTime.parse("2024-07-01T00:00"),
            LocalDateTime.parse("2024-07-15T00:00")
        );
        scheduleContext.generate(users, interval);
    }
    
}