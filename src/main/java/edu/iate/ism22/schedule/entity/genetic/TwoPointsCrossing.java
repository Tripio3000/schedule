package edu.iate.ism22.schedule.entity.genetic;

import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * При двухточечном скрещивании случайным образом выбираются по две точки скрещивания в каждой хромосоме.
 * Гены одной хромосомы, расположенные между этими точками, обмениваются с точно так же расположенными генами другой хромосомы.
 * В данном случае хромосомой является ScheduleIndividual, а генами - List<ScheduleLine>.
 * <p>
 * Как правило, оператор скрещивания применяется не всегда, а с некоторой (высокой) вероятностью.
 * В данном случае за вероятность скрещивания отвечает параметр CROSSING_SKIP.
 */

public class TwoPointsCrossing implements Crossing {
    
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
        
        
        Collections.shuffle(population);
        int start = skipIndividuals(population.size());
        for (int i = start; i < population.size() - 1; i+=2) {
            List<ScheduleLine> individ1 = population.get(i).getScheduleLines();
            List<ScheduleLine> individ2 = population.get(i+1).getScheduleLines();
            
            int min = 1;
            int max = individ1.size();
            int idx1 = rnd.nextInt(max - min) + min;
            int idx2 = rnd.nextInt(max - idx1) + idx1 + 1;
            
            for (int j = idx2; j < idx1; j++) {
                ScheduleLine tmp = individ1.get(j);
                individ1.set(j, individ2.get(j));
                individ2.set(j, tmp);
            }
            
        }
        
        return null;
    }
}
