package edu.iate.ism22.schedule.entity.genetic.crossing;

import edu.iate.ism22.schedule.entity.genetic.ScheduleIndividual;
import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static edu.iate.ism22.schedule.generation.Constants.POPULATION_CAPACITY;


/**
 * При двухточечном скрещивании случайным образом выбираются по две точки скрещивания в каждой хромосоме.
 * Гены одной хромосомы, расположенные между этими точками, обмениваются с точно так же расположенными генами другой хромосомы.
 * В данном случае хромосомой является ScheduleIndividual, а генами - List<ScheduleLine>.
 * <p>
 * Как правило, оператор скрещивания применяется не всегда, а с некоторой (высокой) вероятностью.
 * В данном случае за вероятность скрещивания отвечает параметр CROSSING_SKIP.
 */

public class OnePointsCrossing implements Crossing {
    
    private static Random r;
    
    @Override
    public List<ScheduleIndividual> cross(List<ScheduleIndividual> population) {
        Random rnd = r;
        if (rnd == null) {
            r = rnd = new Random();
        }
        
        /**
         * Надо переделать механизм скрещивания таким образом, чтоб после отбора размер популяции вернулся к исходной
         * величине.
         * 1. При скрещивании создаются новые индивиды. Гены для них берется из индивидов, прошедших отбор.
         * 2. Для них просчитываются значения fte заново (!)
         */
        
        LocalDateTime start = population.getFirst().getStart();
        LocalDateTime end = population.getFirst().getEnd();
        
        List<ScheduleIndividual> crossedPopulation = new ArrayList<>(POPULATION_CAPACITY);
        
        int max = population.size();
        while (crossedPopulation.size() < POPULATION_CAPACITY) {
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
            
            crossedPopulation.add(new ScheduleIndividual(users, start, end, newScheduleLines));
        }
        
        return crossedPopulation;
    }
}
