package edu.iate.ism22.schedule.entity.user;

import java.time.LocalDateTime;

public class EmptyShift extends Shift {
    public EmptyShift(LocalDateTime start, LocalDateTime end) {
        super(
            start.toLocalDate(),
            new WorkActivity(start.toLocalTime(), end.toLocalTime(), "empty", false)
        );
    }
}
