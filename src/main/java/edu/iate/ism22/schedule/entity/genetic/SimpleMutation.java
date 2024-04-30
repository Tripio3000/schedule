package edu.iate.ism22.schedule.entity.genetic;

import edu.iate.ism22.schedule.entity.user.User;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimpleMutation implements Mutation {
    
    private static Random r;
    
    @Override
    public void apply(List<ScheduleIndividual> population) {
        Random rnd = r;
        if (rnd == null) {
            r = rnd = new Random();
        }
        
        int mutationAmount = skipIndividuals(population.size());
        for (int i = 0; i < mutationAmount; i++) {
            Map<User, ScheduleLine> scheduleLines = population.get(i).getScheduleLines();
            int mutIdx = rnd.nextInt(scheduleLines.size());
            
            // Удаляем данные, сохраненные в cache. При следующем запросе будет сгенерирован новый набор ScheduleActivity.
            scheduleLines.values().stream()
                .toList()
                .get(mutIdx)
                .clearCache();
        }
    }
}
