package edu.iate.ism22.schedule.generation;

import edu.iate.ism22.schedule.entity.user.ScheduleContainer;
import edu.iate.ism22.schedule.entity.user.ScheduleContainerDayOff;
import edu.iate.ism22.schedule.entity.user.ScheduleContainerWorkday;
import edu.iate.ism22.schedule.entity.user.ScheduleVariant;
import edu.iate.ism22.schedule.entity.user.Scheme;
import edu.iate.ism22.schedule.entity.user.SchemeImpl;
import edu.iate.ism22.schedule.entity.user.WorkActivity;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import edu.iate.ism22.schedule.utils.CircularLinkedList;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Random;

import static java.time.LocalTime.parse;

public class ScheduleContextTestInitializer {
    protected Scheme scheme2by2;
    protected Scheme scheme5by2;
    private WorkActivity work1;
    private WorkActivity work2;
    private WorkActivity meal;
    private Random rand;
    
    @BeforeEach
    void setUp() {
        rand = new Random();
        
        // Декларируем активности. Пока они не привязаны ко времени, а только отражают суть занятости.
        work1 = new WorkActivity("work1", true);
        work2 = new WorkActivity("work2", true);
        meal = new WorkActivity("meal", false);
        
        // Создаем схемы
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
    
    // на выбор смены по 9 часов: 07-16, 09-18, 11-20
    protected ScheduleContainer createWorkContainer5by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work1, parse("07:00:00"), parse("11:00:00")),
                        new ScheduleVariant(meal, parse("11:00:00"), parse("12:00:00")),
                        new ScheduleVariant(work1, parse("12:00:00"), parse("16:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work1, parse("08:00:00"), parse("12:00:00")),
                        new ScheduleVariant(meal, parse("12:00:00"), parse("13:00:00")),
                        new ScheduleVariant(work1, parse("13:00:00"), parse("17:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work1, parse("09:00:00"), parse("13:00:00")),
                        new ScheduleVariant(meal, parse("13:00:00"), parse("14:00:00")),
                        new ScheduleVariant(work1, parse("14:00:00"), parse("18:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work1, parse("10:00:00"), parse("14:00:00")),
                        new ScheduleVariant(meal, parse("14:00:00"), parse("15:00:00")),
                        new ScheduleVariant(work1, parse("15:00:00"), parse("19:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work1, parse("11:00:00"), parse("15:00:00")),
                        new ScheduleVariant(meal, parse("15:00:00"), parse("16:00:00")),
                        new ScheduleVariant(work1, parse("16:00:00"), parse("20:00:00"))
                    )
                )
            )
        );
    }
    
    // на выбор смены по 13 часов: 00-13, 04-17, 08-21, 12-01, 16-05, 20-09
    protected ScheduleContainer createWorkContainer2by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("00:00:00"), parse("06:00:00")),
                        new ScheduleVariant(meal, parse("06:00:00"), parse("07:00:00")),
                        new ScheduleVariant(work2, parse("07:00:00"), parse("13:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("02:00:00"), parse("08:00:00")),
                        new ScheduleVariant(meal, parse("08:00:00"), parse("09:00:00")),
                        new ScheduleVariant(work2, parse("09:00:00"), parse("15:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("04:00:00"), parse("10:00:00")),
                        new ScheduleVariant(meal, parse("10:00:00"), parse("11:00:00")),
                        new ScheduleVariant(work2, parse("11:00:00"), parse("17:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("06:00:00"), parse("12:00:00")),
                        new ScheduleVariant(meal, parse("12:00:00"), parse("13:00:00")),
                        new ScheduleVariant(work2, parse("13:00:00"), parse("19:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("08:00:00"), parse("14:00:00")),
                        new ScheduleVariant(meal, parse("14:00:00"), parse("15:00:00")),
                        new ScheduleVariant(work2, parse("15:00:00"), parse("21:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("10:00:00"), parse("16:00:00")),
                        new ScheduleVariant(meal, parse("16:00:00"), parse("17:00:00")),
                        new ScheduleVariant(work2, parse("17:00:00"), parse("23:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("12:00:00"), parse("18:00:00")),
                        new ScheduleVariant(meal, parse("18:00:00"), parse("19:00:00")),
                        new ScheduleVariant(work2, parse("19:00:00"), parse("01:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("14:00:00"), parse("20:00:00")),
                        new ScheduleVariant(meal, parse("20:00:00"), parse("21:00:00")),
                        new ScheduleVariant(work2, parse("21:00:00"), parse("03:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("16:00:00"), parse("22:00:00")),
                        new ScheduleVariant(meal, parse("22:00:00"), parse("23:00:00")),
                        new ScheduleVariant(work2, parse("23:00:00"), parse("05:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("18:00:00"), parse("00:00:00")),
                        new ScheduleVariant(meal, parse("00:00:00"), parse("01:00:00")),
                        new ScheduleVariant(work2, parse("01:00:00"), parse("07:00:00"))
                    )
                ),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("20:00:00"), parse("02:00:00")),
                        new ScheduleVariant(meal, parse("02:00:00"), parse("03:00:00")),
                        new ScheduleVariant(work2, parse("03:00:00"), parse("09:00:00"))
                    )
                )
            )
        );
    }
    
}
