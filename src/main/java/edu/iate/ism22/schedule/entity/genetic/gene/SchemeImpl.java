package edu.iate.ism22.schedule.entity.genetic.gene;

import edu.iate.ism22.schedule.utils.CircularLinkedList;

public class SchemeImpl implements Scheme {
    
    CircularLinkedList<ScheduleContainer> containers;
//    List<ScheduleContainer> containers;
    
    public SchemeImpl(CircularLinkedList<ScheduleContainer> containers) {
        this.containers = containers;
    }
    
//    public SchemeImpl() {
//        containers = new ArrayList<>();
//    }
    
    public void addContainer(ScheduleContainer container) {
        containers.add(container);
    }
    
    @Override
    public CircularLinkedList<ScheduleContainer> getContainers() {
        return containers;
    }
}
