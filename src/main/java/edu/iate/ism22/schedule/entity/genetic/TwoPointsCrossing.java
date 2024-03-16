package edu.iate.ism22.schedule.entity.genetic;

import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * При двухточечном скрещивании случайным образом выбираются по две точки скрещивания в каждой хромосоме.
 * Гены одной хромосомы, расположенные между этими точками, обмениваются с точно так же расположенными генами другой хромосомы.
 * В данном случае хромосомой является ScheduleIndividual, а генами - List<ScheduleLine>.
 */

public class TwoPointsCrossing implements Crossing {
    
    private static Random r;
    
    @Override
    public void cross(List<ScheduleIndividual> population) {
        Random rnd = r;
        if (rnd == null) {
            r = rnd = new Random();
        }
        Collections.shuffle(population);
        
        int start = skipIndividuals(population.size()) + 1;
        for (int i = start; i < population.size(); i++) {
            population.get(i).getScheduleLines();
        }
        
    }
}
