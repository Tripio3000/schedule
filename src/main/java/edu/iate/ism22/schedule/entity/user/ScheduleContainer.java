package edu.iate.ism22.schedule.entity.user;

import java.util.List;

public interface ScheduleContainer {
    
    WorkActivity getRandomWorkActivity();
    List<WorkActivity> getAllWorkActivities();
}
