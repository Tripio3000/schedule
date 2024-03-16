package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ScheduleResolver {
    private List<ScheduleLine> scheduleLines;
    private Map<LocalDateTime, Integer> ftePredict;
    private Map<User, Integer[]> fteFact;
    
    public ScheduleResolver(
        List<ScheduleLine> scheduleLines,
        Map<LocalDateTime, Integer> ftePredict,
        Map<User, Integer[]> fteFact
    ) {
        this.scheduleLines = scheduleLines;
        this.ftePredict = ftePredict;
        this.fteFact = fteFact;
    }
    
    public void resolve() {
        
        // тут генетический алгоритм
        return;
    }
}
