package edu.iate.ism22.schedule.entity.user;

import edu.iate.ism22.schedule.utils.CircularLinkedList;

public interface Scheme {
    
    CircularLinkedList<ScheduleContainer> getContainers();
    
}
