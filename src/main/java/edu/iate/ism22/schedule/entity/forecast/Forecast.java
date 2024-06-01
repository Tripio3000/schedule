package edu.iate.ism22.schedule.entity.forecast;

import edu.iate.ism22.schedule.utils.LocalInterval;

public interface Forecast<T> {
    
    T valueFor(LocalInterval interval);
}
