package edu.iate.ism22.schedule.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class WorkActivity {
    private LocalTime start;
    private LocalTime end;
    private String name;
    private boolean isWork;
    
    @Override
    public String toString() {
        return "WorkActivity{" +
               "start=" + start +
               ", end=" + end +
               ", name='" + name + '\'' +
               ", isWork=" + isWork +
               '}';
    }
}
