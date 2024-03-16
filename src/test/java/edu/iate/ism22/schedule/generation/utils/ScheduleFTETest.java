package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.generation.ScheduleContextTestInitializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ScheduleFTETest extends ScheduleContextTestInitializer {
    
//    private ScheduleFTE scheduleFTE;
    
    @Test
    void fteMatrix() {
        ScheduleFTE scheduleFTE = new ScheduleFTE();
        
        User user1 = new Operator("user1", scheme2by2);
        User user2 = new Operator("user2", scheme5by2);
        ScheduleIndividual scheduleIndividual = new ScheduleIndividual(
            List.of(user1, user2),
            LocalDateTime.parse("2024-01-01T00:00"),
            LocalDateTime.parse("2024-02-01T00:00")
        );
//        scheduleFTE = new ScheduleFTE(scheduleIndividual);
        
        Map<LocalDateTime, Integer> actual = scheduleFTE.fteMatrix(scheduleIndividual);
        
        System.out.println(actual);
    }
    
    
    @Test
    void name() {
        LocalDateTime dt1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        
        long start = dt1.toInstant(ZoneOffset.ofHours(3)).toEpochMilli();
        long end = dt2.toInstant(ZoneOffset.ofHours(3)).toEpochMilli();
        long step = 900000l;
        
        System.out.println((int) ((end - start) / step));

//        Map<LocalDateTime, Integer> sumFte = new HashMap<>((int) ((end - start) / step));
        
    }
    
    @Test
    void maxValueInMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 5);
        map.put("B", 3);
        map.put("C", 7);
        map.put("D", 20);
        map.put("E", 4);
        map.put("F", 6);
        
        
        
        String max = Collections.max(map.keySet(), Comparator.comparing(map::get));
        System.out.println(max);
        
        
    }
}