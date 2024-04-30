package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.forecast.ForecastFTE;
import edu.iate.ism22.schedule.entity.genetic.FitnessByFTE;
import edu.iate.ism22.schedule.entity.genetic.SchedulePopulationProvider;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.utils.ScheduleFTE;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ScheduleContextTest extends ScheduleContextTestInitializer {
    
    @Test
    void generate() {
        
        ScheduleFTE fteService = new ScheduleFTE();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            users.add(new Operator("user" + i, scheme2by2));
        }
        for (int i = 0; i < 60; i++) {
            users.add(new Operator("user" + i, scheme5by2));
        }
        SchedulePopulationProvider provider = new SchedulePopulationProvider(
            users,
            LocalDateTime.parse("2024-01-01T00:00"),
            LocalDateTime.parse("2024-01-14T00:00"),
            fteService
        );
        
        ScheduleContext scheduleContext = new ScheduleContext(provider, new FitnessByFTE(new ForecastFTE()));
        
        scheduleContext.generate();
    }
    
}