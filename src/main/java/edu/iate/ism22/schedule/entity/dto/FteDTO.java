package edu.iate.ism22.schedule.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FteDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private Integer actualFte;
    private Integer forecastFte;
    
    public FteDTO(LocalDateTime dateTime, Integer actualFte, Integer forecastFte) {
        this.dateTime = dateTime;
        this.actualFte = actualFte;
        this.forecastFte = forecastFte;
    }
}
