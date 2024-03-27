package edu.iate.ism22.schedule.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WorkActivity {
    private String name;
    private boolean isWork;
    
    @Override
    public String toString() {
        return "WorkActivity{" +
               "name='" + name + '\'' +
               ", isWork=" + isWork +
               '}';
    }
}
