package edu.iate.ism22.schedule.entity.genetic.gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleContainerWorkday implements ScheduleContainer {
    
    private final Random rand;
    private final List<WorkShift> activities;
    
    public ScheduleContainerWorkday(Random rand, List<WorkShift> activities) {
        this.rand = rand;
        this.activities = activities;
    }
    
    public ScheduleContainerWorkday() {
        this.activities = new ArrayList<>();
        this.rand = new Random();
    }
    
    @Override
    public WorkShift getRandomWorkShift() {
        int randomInt = rand.nextInt(activities.size());
        return activities.get(randomInt);
    }
    
    @Override
    public List<WorkShift> getAllWorkShifts() {
        return activities;
    }
}
