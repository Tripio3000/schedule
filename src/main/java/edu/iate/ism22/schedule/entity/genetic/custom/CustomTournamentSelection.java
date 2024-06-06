package edu.iate.ism22.schedule.entity.genetic.custom;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;

import java.util.ArrayList;
import java.util.List;

public class CustomTournamentSelection implements SelectionPolicy {
    
    private int arity;
    
    public CustomTournamentSelection(final int arity) {
        this.arity = arity;
    }
    
    public ChromosomePair select(final Population population) throws MathIllegalArgumentException {
        return new ChromosomePair(tournament((CustomListPopulation) population),
            tournament((CustomListPopulation) population));
    }
    
    private Chromosome tournament(final CustomListPopulation population) throws MathIllegalArgumentException {
        if (population.getPopulationSize() < this.arity) {
            throw new MathIllegalArgumentException(LocalizedFormats.TOO_LARGE_TOURNAMENT_ARITY,
                arity, population.getPopulationSize());
        }
        
        CustomListPopulation tournamentPopulation = new CustomListPopulation(this.arity) {
            public Population nextGeneration() {
                return null;
            }
        };
        
        // create a copy of the chromosome list
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(population.getChromosomes());
        for (int i = 0; i < this.arity; i++) {
            // select a random individual and add it to the tournament
            int rind = GeneticAlgorithm.getRandomGenerator().nextInt(chromosomes.size());
            tournamentPopulation.addChromosome(chromosomes.get(rind));
            // do not select it again
            chromosomes.remove(rind);
        }
        // the winner takes it all
        return tournamentPopulation.getFittestChromosome();
    }
    
    public int getArity() {
        return arity;
    }
    
    public void setArity(final int arity) {
        this.arity = arity;
    }
}
