package edu.iate.ism22.schedule.entity.dto;

import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserScheduleDTO {
    
    private Map<String, List<ScheduleActivityDTO>> userSchedules;
    
    public UserScheduleDTO(List<ScheduleLine> scheduleLines) {
        this.userSchedules = scheduleLines.stream().collect(Collectors.toMap(
            e -> e.getUser().getLogin(),
            e -> e.getScheduleActivities().stream().map(ScheduleActivityDTO::new).toList()
        ));
    }
    
    public Map<String, List<ScheduleActivityDTO>> getUserSchedules() {
        return userSchedules;
    }
    
    public void setUserSchedules(Map<String, List<ScheduleActivityDTO>> userSchedules) {
        this.userSchedules = userSchedules;
    }
}
