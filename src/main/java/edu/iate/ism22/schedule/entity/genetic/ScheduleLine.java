package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.EmptyScheduleActivity;
import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.ScheduleContainer;
import edu.iate.ism22.schedule.entity.user.ScheduleVariant;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import edu.iate.ism22.schedule.exception.GenerationIntervalException;
import edu.iate.ism22.schedule.exception.ScheduleContainerNotFound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ScheduleLine {
    private final User user;
    private final LocalDateTime start;
    private final LocalDateTime end;
    
    private List<ScheduleActivity> cached = new ArrayList<>();
    
    public List<ScheduleActivity> getScheduleLine() {
        if (cached.isEmpty()) {
            cached = generateScheduleLine();
        }
        return cached;
    }
    
    public void clearCache() {
        cached = Collections.emptyList();
    }
    
    /**
     * В соответствии со схемой пользователя достаем контейнер смен пользователя из контейнера смен
     * Берем случайную смену на каждый день
     * Дополняем промежутки между сменами пустой активностью.
     */
    private List<ScheduleActivity> generateScheduleLine() {
        if (start.isAfter(end)) {
            throw new GenerationIntervalException("Start date is after end date.");
        }
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        
        List<ScheduleActivity> scheduleLine = new ArrayList<>();
        
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            if (user.getScheme().getContainers().isEmpty()) {
                throw new ScheduleContainerNotFound("Schedule container not found.");
            }
            for (ScheduleContainer container : user.getScheme().getContainers().getList()) {
                
                WorkShift nextShift = container.getRandomWorkShift();
                if (nextShift.isWorkShift()) {
                    fillScheduleLineWithWorkShift(scheduleLine, nextShift, currentDate);
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        fillEmptyActivities(scheduleLine);
        
        return scheduleLine;
    }
    
    private void fillScheduleLineWithWorkShift(List<ScheduleActivity> scheduleLine, WorkShift workShift, LocalDate currentDate) {
        for (ScheduleVariant variant : workShift.getActivities()) {
            LocalDateTime dateStart = LocalDateTime.of(currentDate, variant.getStart());
            if (!variant.getStart().isBefore(variant.getEnd())) {
                currentDate = currentDate.plusDays(1);
            }
            LocalDateTime dateEnd = LocalDateTime.of(currentDate, variant.getEnd());
            scheduleLine.add(new ScheduleActivity(variant.getWorkActivity(), user, dateStart, dateEnd));
        }
    }
    
    private void fillEmptyActivities(List<ScheduleActivity> shifts) {
        if (start.isBefore(shifts.getFirst().getStart())) {
            shifts.addFirst(new EmptyScheduleActivity(user, start, shifts.getFirst().getStart()));
        }
        if (end.isAfter(shifts.getLast().getEnd())) {
            shifts.addLast(new EmptyScheduleActivity(user, shifts.getLast().getEnd(), end));
        }
        
        for (int i = 0; i < shifts.size() - 1; i++) {
            LocalDateTime currentEnd = shifts.get(i).getEnd();
            LocalDateTime nextStart = shifts.get(i + 1).getStart();
            if (currentEnd.isBefore(nextStart)) {
                shifts.add(i + 1, new EmptyScheduleActivity(user, currentEnd, nextStart));
            }
        }
    }
}

/**
 * 1. Сделать основой расписания ScheduleActivity, вместо Shift. Расписание - упорядоченный список ScheduleActivity.
 * 2. Смена - непрерывный набор ScheduleActivity.
 * 3. Shift используется в контейнере смен. Один вариант в контейрене смен - смена, а не WorkActivity как сейчас.
 * 4. Смена подставляется без изменений в расписание без изменений. В промежутки между сменами из контейнеров
 * смен подставляются пустые смены. То же самое с выходными днями.
 */
