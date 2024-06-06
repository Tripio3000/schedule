package edu.iate.ism22.schedule.entity.genetic.custom;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;

import java.util.Collections;
import java.util.List;

public class CustomElitisticListPopulation extends CustomListPopulation {
    
    private double elitismRate = 0.9;
    
    public CustomElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit,
                                         final double elitismRate)
        throws NullArgumentException, NotPositiveException, NumberIsTooLargeException, OutOfRangeException {
        
        super(chromosomes, populationLimit);
        setElitismRate(elitismRate);
    }
    
    public CustomElitisticListPopulation(final int populationLimit, final double elitismRate)
        throws NotPositiveException, OutOfRangeException {
        
        super(populationLimit);
        setElitismRate(elitismRate);
    }
    
    @Override
    public Population nextGeneration() {
        // initialize a new generation with the same parameters
        CustomElitisticListPopulation nextGeneration =
            new CustomElitisticListPopulation(getPopulationLimit(), getElitismRate());
        
        final List<Chromosome> oldChromosomes = getChromosomeList();
        Collections.sort(oldChromosomes);
        
        // index of the last "not good enough" chromosome
        int boundIndex = (int) FastMath.ceil((1.0 - getElitismRate()) * oldChromosomes.size());
        for (int i = 0; i < oldChromosomes.size() - boundIndex; i++) {
            nextGeneration.addChromosome(oldChromosomes.get(i));
        }
        return nextGeneration;
    }
    
    public void setElitismRate(final double elitismRate) throws OutOfRangeException {
        if (elitismRate < 0 || elitismRate > 1) {
            throw new OutOfRangeException(LocalizedFormats.ELITISM_RATE, elitismRate, 0, 1);
        }
        this.elitismRate = elitismRate;
    }
    
    public double getElitismRate() {
        return this.elitismRate;
    }
}
