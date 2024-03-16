package edu.iate.ism22.schedule.entity.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleContainerWorkday implements ScheduleContainer {
    
    private final Random rand;
    private final List<WorkActivity> activities;
    
    public ScheduleContainerWorkday(Random rand, List<WorkActivity> activities) {
        this.rand = rand;
        this.activities = activities;
    }
    
    public ScheduleContainerWorkday() {
        this.activities = new ArrayList<>();
        this.rand = new Random();
    }
    
    @Override
    public WorkActivity getRandomWorkActivity() {
        int randomInt = rand.nextInt(activities.size());
        return activities.get(randomInt);
    }
    
    @Override
    public List<WorkActivity> getAllWorkActivities() {
        return activities;
    }
    
    public void addWorkActivity(WorkActivity wa) {
        activities.add(wa);
    }
    
}
