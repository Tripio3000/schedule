package edu.iate.ism22.schedule.entity.user;

import java.util.List;

import static java.time.LocalTime.parse;

public class ScheduleContainerDayOff implements ScheduleContainer {
    
    private WorkActivity dayOffActivity;
    
    public ScheduleContainerDayOff() {
        this.dayOffActivity = new WorkActivity(parse("00:00:00"), parse("00:00:00"), "dayOff", false);
    }
    
    @Override
    public WorkActivity getRandomWorkActivity() {
        return dayOffActivity;
    }
    
    @Override
    public List<WorkActivity> getAllWorkActivities() {
        return List.of(dayOffActivity);
    }
}
