package edu.iate.ism22.schedule;

import edu.iate.ism22.schedule.entity.user.Operator;
import edu.iate.ism22.schedule.entity.user.ScheduleContainer;
import edu.iate.ism22.schedule.entity.user.ScheduleContainerWorkday;
import edu.iate.ism22.schedule.entity.user.ScheduleVariant;
import edu.iate.ism22.schedule.entity.user.User;
import edu.iate.ism22.schedule.entity.user.WorkActivity;
import edu.iate.ism22.schedule.entity.user.WorkShift;
import edu.iate.ism22.schedule.generation.ScheduleContext;
import edu.iate.ism22.schedule.utils.LocalInterval;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.time.LocalTime.parse;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ScheduleApplication implements CommandLineRunner {
    
    private WorkActivity work1;
    private WorkActivity work2;
    private WorkActivity meal;
    private Random rand;
    
    protected ScheduleContainer container8h;
    protected ScheduleContainer container12h;
    
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ScheduleApplication.class, args)));
    }
    
    
    @Override
    public void run(String... args) {
        
        init();
        
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            users.add(new Operator("user" + i, container12h));
        }
//        for (int i = 10; i < 20; i++) {
//            users.add(new Operator("user" + i, container8h));
//        }
        
        ScheduleContext scheduleContext = new ScheduleContext();
        LocalInterval interval = new LocalInterval(
            LocalDateTime.parse("2024-08-01T00:00"),
            LocalDateTime.parse("2024-08-08T00:00")
        );
        
        scheduleContext.generate(users, interval);
    }
    
    private void init() {
        rand = new Random();
        
        // Декларируем активности. Пока они не привязаны ко времени, а только отражают суть занятости.
        work1 = new WorkActivity("work1", true);
        work2 = new WorkActivity("work2", true);
        meal = new WorkActivity("meal", false);
        
        container8h = createWorkContainer5by2();
        container12h = createWorkContainer2by2();
    }
    
    protected ScheduleContainer createWorkContainer2by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("00:00:00"), parse("06:00:00")),
                        new ScheduleVariant(meal, parse("06:00:00"), parse("07:00:00")),
                        new ScheduleVariant(work2, parse("07:00:00"), parse("13:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("02:00:00"), parse("08:00:00")),
                        new ScheduleVariant(meal, parse("08:00:00"), parse("09:00:00")),
                        new ScheduleVariant(work2, parse("09:00:00"), parse("15:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("04:00:00"), parse("10:00:00")),
                        new ScheduleVariant(meal, parse("10:00:00"), parse("11:00:00")),
                        new ScheduleVariant(work2, parse("11:00:00"), parse("17:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("06:00:00"), parse("12:00:00")),
                        new ScheduleVariant(meal, parse("12:00:00"), parse("13:00:00")),
                        new ScheduleVariant(work2, parse("13:00:00"), parse("19:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("08:00:00"), parse("14:00:00")),
                        new ScheduleVariant(meal, parse("14:00:00"), parse("15:00:00")),
                        new ScheduleVariant(work2, parse("15:00:00"), parse("21:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("10:00:00"), parse("16:00:00")),
                        new ScheduleVariant(meal, parse("16:00:00"), parse("17:00:00")),
                        new ScheduleVariant(work2, parse("17:00:00"), parse("23:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("12:00:00"), parse("18:00:00")),
                        new ScheduleVariant(meal, parse("18:00:00"), parse("19:00:00")),
                        new ScheduleVariant(work2, parse("19:00:00"), parse("01:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("14:00:00"), parse("20:00:00")),
                        new ScheduleVariant(meal, parse("20:00:00"), parse("21:00:00")),
                        new ScheduleVariant(work2, parse("21:00:00"), parse("03:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("16:00:00"), parse("22:00:00")),
                        new ScheduleVariant(meal, parse("22:00:00"), parse("23:00:00")),
                        new ScheduleVariant(work2, parse("23:00:00"), parse("05:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
                new WorkShift(
                    List.of(
                        new ScheduleVariant(work2, parse("18:00:00"), parse("00:00:00")),
                        new ScheduleVariant(meal, parse("00:00:00"), parse("01:00:00")),
                        new ScheduleVariant(work2, parse("01:00:00"), parse("07:00:00"))
                    )
                ),
                new WorkShift(Collections.emptyList()),
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
    
    protected ScheduleContainer createWorkContainer5by2() {
        return new ScheduleContainerWorkday(
            rand,
            List.of(
                new WorkShift(Collections.emptyList()),
                new WorkShift(Collections.emptyList()),
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
}
