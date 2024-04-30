package edu.iate.ism22.schedule.entity.genetic.crossing;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;

public class SaveBestIndividsCrossing implements Crossing {
    
    private static Random r;
    
    @Override
    public List<ScheduleIndividual> cross(List<ScheduleIndividual> population) {
        
        Random rnd = r;
        if (rnd == null) {
            r = rnd = new Random();
        }
        
        LocalDateTime start = population.getFirst().getStart();
        LocalDateTime end = population.getFirst().getEnd();
        
        int max = population.size();
        while (population.size() < POPULATION_CAPACITY) {
            int indNum1 = rnd.nextInt(max);
            int indNum2 = rnd.nextInt(max);
            
            if (indNum1 == indNum2) {
                continue;
            }
            
            Map<User, ScheduleLine> individ1 = population.get(indNum1).getScheduleLines();
            Map<User, ScheduleLine> individ2 = population.get(indNum2).getScheduleLines();
            
            List<User> users = individ1.keySet().stream().toList();
            int point = 50;
            
            Map<User, ScheduleLine> newScheduleLines = new HashMap<>(users.size());
            User currentUser;
            for (int i = 0; i < users.size(); i++) {
                currentUser = users.get(i);
                if (i <= point) {
                    newScheduleLines.put(currentUser, individ1.get(currentUser));
                } else {
                    newScheduleLines.put(currentUser, individ2.get(currentUser));
                }
            }
            
            population.add(new ScheduleIndividual(users, start, end, newScheduleLines));
            max++;
        }
        
        return population;
    }
}
