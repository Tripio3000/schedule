package edu.iate.ism22.schedule.entity.genetic;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimpleMutation implements Mutation {
    
    private static Random r;
    
    @Override
    public void apply(List<ScheduleIndividual> population) {
        Random rnd = r;
        if (rnd == null) {
            r = rnd = new Random();
        }
        Collections.shuffle(population);
        
        int mutationAmount = skipIndividuals(population.size());
        for (int i = 0; i < mutationAmount; i++) {
            List<ScheduleLine> scheduleLines = population.get(i).getScheduleLines();
            int mutIdx = rnd.nextInt(scheduleLines.size());
            
            // Удаляем данные, сохраненные в cache. При следующем запросе будет сгенерирован новый набор ScheduleActivity.
            scheduleLines.get(mutIdx).clearCache();
        }
    }
}
