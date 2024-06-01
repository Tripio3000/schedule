package edu.iate.ism22.schedule.entity.forecast;

import edu.iate.ism22.schedule.utils.LocalInterval;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CachedForecast implements Forecast<Map<LocalDateTime, Integer>> {
    
    private final ForecastFTE forecastFTE;
    
    private final Map<LocalInterval, Map<LocalDateTime, Integer>> cachedByInterval = new HashMap<>();
    
    @Override
    public Map<LocalDateTime, Integer> valueFor(LocalInterval interval) {
        return cachedByInterval.computeIfAbsent(interval, forecastFTE::valueFor);
    }
}
