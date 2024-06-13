package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DaySchedule {
    
    private LocalDate date;
    private List<User> users;
    private List<WorkShift> workShifts = new ArrayList<>();
    
    public DaySchedule(LocalDate date, List<User> users) {
        this.date = date;
        this.users = users;
    }
    
    public List<WorkShift> getWorkShifts() {
        if (workShifts.isEmpty()) {
            for (User user : users) {
                workShifts.add(user.getScheduleContainer().getRandomWorkShift());
            }
        }
        return workShifts;
    }
    
    public void clearCache() {
        workShifts.clear();
    }
    
}
