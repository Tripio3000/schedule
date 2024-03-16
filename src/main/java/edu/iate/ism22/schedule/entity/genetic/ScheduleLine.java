package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.EmptyShift;
import edu.iate.ism22.schedule.entity.user.ScheduleContainer;
import edu.iate.ism22.schedule.entity.user.Shift;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.entity.user.WorkActivity;
import edu.iate.ism22.schedule.exception.GenerationIntervalException;
import edu.iate.ism22.schedule.exception.ScheduleContainerNotFound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ScheduleLine {
    private final User user;
    private final LocalDateTime start;
    private final LocalDateTime end;
    
    private List<Shift> cached = new ArrayList<>();
    
    public List<Shift> getScheduleLine() {
        if (cached.isEmpty()) {
            cached = generateScheduleLine();
        }
        return cached;
    }
    
    /**
     * В соответствии со схемой пользователя достаем контейнер смен пользователя из контейнера смен
     * Берем случайную смену на каждый день
     * Дополняем промежутки между сменами пустой активностью.
     */
    private List<Shift> generateScheduleLine() {
        if (start.isAfter(end)) {
            throw new GenerationIntervalException("Start date is after end date.");
        }
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        
        List<Shift> scheduleLine = new ArrayList<>();
        
        LocalDate current = startDate;
        while (current.isBefore(endDate)) {
            if (user.getScheme().getContainers().isEmpty()) {
                throw new ScheduleContainerNotFound("Schedule container not found.");
            }
            for (ScheduleContainer container : user.getScheme().getContainers().getList()) {
                
                WorkActivity nextWorkActivity = container.getRandomWorkActivity();
                if (nextWorkActivity.isWork()) {
                    scheduleLine.add(new Shift(current, nextWorkActivity));
                }
                current = current.plusDays(1);
            }
        }
        fillEmptyActivities(scheduleLine);
        
        return scheduleLine;
    }
    
    private void fillEmptyActivities(List<Shift> shifts) {
        if (start.isBefore(shifts.getFirst().getStart())) {
            shifts.addFirst(new EmptyShift(start, shifts.getFirst().getStart()));
        }
        
        for (int i = 0; i < shifts.size() - 1; i++) {
            LocalDateTime currentEnd = shifts.get(i).getEnd();
            LocalDateTime nextStart = shifts.get(i + 1).getStart();
            if (currentEnd.isBefore(nextStart)) {
                shifts.add(i + 1, new EmptyShift(currentEnd, nextStart));
            }
        }
    }
}
