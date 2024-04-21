package edu.iate.ism22.schedule.entity.genetic;

import java.util.List;

import static edu.iate.ism22.schedule.generation.Constants.CROSSING_SKIP;

/**
 * Оператор скрещивания (или рекомбинации) соответствует биологическому скрещиванию при половом размножении.
 * Он используется для комбинирования генетической информации двух индивидуумов, выступающих в роли родителей,
 * в процессе порождения потомков (обычно двух).
 * <p>
 * Как правило, оператор скрещивания применяется не всегда, а с некоторой (высокой) вероятностью.
 * Если скрещивание не применяется, то копии обоих родителей переходят в следующее поколение без изменения.
 */
public interface Crossing {
    
    List<ScheduleIndividual> cross(List<ScheduleIndividual> population);
    
    default int skipIndividuals(int populationSize) {
        return (int) (populationSize * CROSSING_SKIP);
    }
}
