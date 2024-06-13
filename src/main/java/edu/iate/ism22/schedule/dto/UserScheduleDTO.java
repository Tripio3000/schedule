package edu.iate.ism22.schedule.dto;

import edu.iate.ism22.schedule.entity.user.ScheduleActivity;
import edu.iate.ism22.schedule.entity.user.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserScheduleDTO {
    
    private Map<String, List<ScheduleActivityDTO>> userSchedules;
    
    public UserScheduleDTO(Map<User, List<ScheduleActivity>> userScheduleMap) {
        this.userSchedules = userScheduleMap.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getLogin(),
                entry -> entry.getValue().stream().map(ScheduleActivityDTO::new).collect(Collectors.toList())
            ));
    }
    
    public Map<String, List<ScheduleActivityDTO>> getUserSchedules() {
        return userSchedules;
    }
    
    public void setUserSchedules(Map<String, List<ScheduleActivityDTO>> userSchedules) {
        this.userSchedules = userSchedules;
    }
}
