package edu.iate.ism22.schedule.entity.genetic.custom;

import edu.iate.ism22.schedule.entity.genetic.ScheduleLine;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;

import java.util.List;

public class MutationImpl implements MutationPolicy {
    
    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
         if (!(original instanceof UserScheduleChromosome origChrom)) {
            throw new IllegalArgumentException("expected.UserScheduleChromosome");
        }
        
        List<ScheduleLine> representation = origChrom.getRepresentation();
        int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(representation.size());
        representation.get(geneIndex).clearCache();
        
        return origChrom;
    }
}
