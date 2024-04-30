package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ScheduleIndividual {
    private final List<User> users;
    private final LocalDateTime start;
    private final LocalDateTime end;
    @Setter
    private Map<LocalDateTime, Integer> fte;
    private Map<User, ScheduleLine> cached;
    
    public ScheduleIndividual(List<User> users, LocalDateTime start, LocalDateTime end) {
        this.users = users;
        this.start = start;
        this.end = end;
        this.fte = new HashMap<>();
        this.cached = Collections.emptyMap();
    }
    
    public ScheduleIndividual(List<User> users, LocalDateTime start, LocalDateTime end, Map<User, ScheduleLine> cached) {
        this.users = users;
        this.start = start;
        this.end = end;
        this.fte = new HashMap<>();
        this.cached = cached;
    }
    
    public Map<User, ScheduleLine> getScheduleLines() {
        if (cached.isEmpty()) {
            cached = users.stream()
                .map(user -> new ScheduleLine(user, start, end))
                .collect(Collectors.toMap(ScheduleLine::getUser, Function.identity()));
        }
        return cached;
    }
}
