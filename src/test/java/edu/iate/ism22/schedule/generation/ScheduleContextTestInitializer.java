package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.user.ScheduleContainer;
import edu.iate.ism22.schedule.entity.user.ScheduleContainerDayOff;
import edu.iate.ism22.schedule.entity.user.ScheduleContainerWorkday;
import edu.iate.ism22.schedule.entity.user.Scheme;
import edu.iate.ism22.schedule.entity.user.SchemeImpl;
import edu.iate.ism22.schedule.entity.user.WorkActivity;
import edu.iate.ism22.schedule.utils.CircularLinkedList;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Random;

import static java.time.LocalTime.parse;

public class ScheduleContextTestInitializer {
    
    protected Scheme scheme1by1;
    protected Scheme scheme2by2;
    protected Scheme scheme5by2;
    private Random rand;
    
    @BeforeEach
    void setUp() {
        rand = new Random();
        
        CircularLinkedList<ScheduleContainer> circ1by1 = new CircularLinkedList<>(rand);
        circ1by1.add(createWorkContainer1by1());
        circ1by1.add(new ScheduleContainerDayOff());
        scheme1by1 = new SchemeImpl(circ1by1);
        
        CircularLinkedList<ScheduleContainer> circ2by2 = new CircularLinkedList<>(rand);
        circ2by2.add(createWorkContainer2by2());
        circ2by2.add(createWorkContainer2by2());
        circ2by2.add(new ScheduleContainerDayOff());
        circ2by2.add(new ScheduleContainerDayOff());
        scheme2by2 = new SchemeImpl(circ2by2);
        
        CircularLinkedList<ScheduleContainer> circ5by2 = new CircularLinkedList<>(rand);
        circ5by2.add(createWorkContainer5by2());
        circ5by2.add(createWorkContainer5by2());
        circ5by2.add(createWorkContainer5by2());
        circ5by2.add(createWorkContainer5by2());
        circ5by2.add(createWorkContainer5by2());
        circ5by2.add(new ScheduleContainerDayOff());
        circ5by2.add(new ScheduleContainerDayOff());
        scheme5by2 = new SchemeImpl(circ5by2);
    }
    
    protected ScheduleContainer createWorkContainer5by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkActivity(parse("07:00:00"), parse("15:00:00"), "work5", true),
                new WorkActivity(parse("08:00:00"), parse("16:00:00"), "work5", true),
                new WorkActivity(parse("09:00:00"), parse("17:00:00"), "work5", true),
                new WorkActivity(parse("10:00:00"), parse("18:00:00"), "work5", true),
                new WorkActivity(parse("11:00:00"), parse("19:00:00"), "work5", true)
            )
        );
    }
    
    protected ScheduleContainer createWorkContainer1by1() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkActivity(parse("00:00:00"), parse("12:00:00"), "work1", true),
                new WorkActivity(parse("02:00:00"), parse("14:00:00"), "work1", true),
                new WorkActivity(parse("04:00:00"), parse("16:00:00"), "work1", true),
                new WorkActivity(parse("06:00:00"), parse("18:00:00"), "work1", true),
                new WorkActivity(parse("08:00:00"), parse("20:00:00"), "work1", true),
                new WorkActivity(parse("10:00:00"), parse("22:00:00"), "work1", true),
                new WorkActivity(parse("12:00:00"), parse("00:00:00"), "work1", true),
                
                new WorkActivity(parse("14:00:00"), parse("02:00:00"), "work1", true),
                new WorkActivity(parse("16:00:00"), parse("04:00:00"), "work1", true),
                new WorkActivity(parse("18:00:00"), parse("06:00:00"), "work1", true),
                new WorkActivity(parse("20:00:00"), parse("08:00:00"), "work1", true),
                new WorkActivity(parse("22:00:00"), parse("10:00:00"), "work1", true)
            )
        );
    }
    
    protected ScheduleContainer createWorkContainer2by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkActivity(parse("06:00:00"), parse("18:00:00"), "work2", true),
                new WorkActivity(parse("07:00:00"), parse("17:00:00"), "work2", true),
                new WorkActivity(parse("08:00:00"), parse("20:00:00"), "work2", true),
                new WorkActivity(parse("09:00:00"), parse("21:00:00"), "work2", true),
                new WorkActivity(parse("10:00:00"), parse("22:00:00"), "work2", true)
            )
        );
    }
}
