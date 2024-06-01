package edu.iate.ism22.schedule.utils;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Getter
public class LocalInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;
    
    public LocalInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
    
    public long getStartMillis() {
        return start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    public long getEndMillis() {
        return end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalInterval that = (LocalInterval) o;
        
        return Objects.equals(getStartMillis(), that.getStartMillis())
               && Objects.equals(getEndMillis(), that.getEndMillis());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getStartMillis(), getEndMillis());
    }
}
