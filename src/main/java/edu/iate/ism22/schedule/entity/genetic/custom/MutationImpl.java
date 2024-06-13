package edu.iate.ism22.schedule.entity.genetic.custom;

import edu.iate.ism22.schedule.entity.genetic.DaySchedule;
import edu.iate.ism22.schedule.entity.user.User;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MutationImpl implements MutationPolicy {
    
    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof UserScheduleChromosome)) {
            throw new IllegalArgumentException("expected.UserScheduleChromosome");
        }
        
        UserScheduleChromosome origChrom = (UserScheduleChromosome) original;
        List<DaySchedule> newRepr = new ArrayList<>(origChrom.getRepresentation());
        
        int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        LocalDate date = newRepr.get(geneIndex).getDate();
        List<User> users = newRepr.get(geneIndex).getUsers();
        newRepr.set(geneIndex, new DaySchedule(date, users));
        
        return origChrom.newFixedLengthChromosome(newRepr);
    }
}
