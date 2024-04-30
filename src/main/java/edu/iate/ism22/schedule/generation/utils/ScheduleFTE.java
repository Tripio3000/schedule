package edu.iate.ism22.schedule.generation.utils;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ScheduleFTE {
    
    private static final long FIFTEEN_MIN = 900000L;
    
    /**
     * В мапу Map<Long, Integer> fifteenMinSum записывается сумма фте для каждой 15-минутки.
     */
    public Map<LocalDateTime, Integer> fteMatrix(ScheduleIndividual scheduleIndividual) {
        
        Map<Long, Integer> fifteenMinSum = new HashMap<>();
        scheduleIndividual.getScheduleLines().values().forEach(sl -> sumFte(sl, fifteenMinSum));
        
        Map<LocalDateTime, Integer> matrix = HashMap.newHashMap(fifteenMinSum.size());
        fifteenMinSum.forEach((k, v) -> {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(k), ZoneOffset.ofHours(3));
            matrix.put(dateTime, v);
            
        });
        
        return matrix;
    }
    
    private void sumFte(ScheduleLine sl, Map<Long, Integer> sumFte) {
        for (ScheduleActivity activity : sl.getScheduleActivities()) {
            int isWork = activity.isWork() ? 1 : 0;
            long startShift = activity.getStart().toInstant(ZoneOffset.ofHours(3)).toEpochMilli();
            long endShift = activity.getEnd().toInstant(ZoneOffset.ofHours(3)).toEpochMilli();
            for (long i = startShift; i <= endShift; i += FIFTEEN_MIN) {
                sumFte.merge(i, isWork, Integer::sum);
            }
        }
    }

//    private Integer[] calculateFte(List<Shift> shifts) {
//        List<Integer> currentFte = new ArrayList<>();
//        shifts.forEach(s -> {
//            int isWork = s.isWork() ? 1 : 0;
//            long fifteenMinutes = ChronoUnit.MINUTES.between(s.getStart(), s.getEnd()) / 15;
//            for (int i = 0; i < fifteenMinutes; i++) {
//                currentFte.add(isWork);
//            }
//        });
//        return currentFte.toArray(new Integer[0]);
//    }
}
