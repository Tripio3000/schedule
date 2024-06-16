package edu.iate.ism22.schedule.entity.genetic.custom;

import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import edu.iate.ism22.schedule.entity.user.User;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MutationImpl implements MutationPolicy {
    
    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof UserScheduleChromosome)) {
            throw new IllegalArgumentException("expected.UserScheduleChromosome");
        }
        
        UserScheduleChromosome origChrom = (UserScheduleChromosome) original;
        List<ScheduleLine> newRepr = new ArrayList<>(origChrom.getRepresentation());
        
        int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        LocalDateTime start = newRepr.get(geneIndex).getStart();
        LocalDateTime end = newRepr.get(geneIndex).getEnd();
        User user = newRepr.get(geneIndex).getUser();
        
        newRepr.set(geneIndex, new ScheduleLine(user, start, end));
        
        return origChrom.newFixedLengthChromosome(newRepr);
    }
}
