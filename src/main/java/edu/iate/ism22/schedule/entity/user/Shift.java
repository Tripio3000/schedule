package edu.iate.ism22.schedule.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Shift {
    private LocalDate day;
    private WorkActivity wa;
    
    public Shift(LocalDate day, WorkActivity wa) {
        this.day = day;
        this.wa = wa;
    }
    
    public LocalDateTime getStart() {
        return LocalDateTime.of(day, wa.getStart());
    }
    
    public LocalDateTime getEnd() {
        return LocalDateTime.of(
            wa.getStart().isBefore(wa.getEnd()) ? day : day.plusDays(1), wa.getEnd()
        );
    }
    
    public boolean isWork() {
        return wa.isWork();
    }
    
    @Override
    public String toString() {
        return "Shift{" +
               "start=" + getStart() +
               ", end=" + getEnd() +
               ", wa=" + wa.getName() +
               '}';
    }
}
