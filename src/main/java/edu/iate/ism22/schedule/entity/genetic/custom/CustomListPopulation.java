package edu.iate.ism22.schedule.entity.genetic.custom;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class CustomListPopulation implements Population {
    
//    private TreeSet<Chromosome> chromosomes;
    private List<Chromosome> chromosomes;
    
    private int populationLimit;
    
    public CustomListPopulation(final int populationLimit) throws NotPositiveException {
        this(Collections.<Chromosome> emptyList(), populationLimit);
    }
    
    public CustomListPopulation(final List<Chromosome> chromosomes, final int populationLimit)
        throws NullArgumentException, NotPositiveException, NumberIsTooLargeException {
        
        if (chromosomes == null) {
            throw new NullArgumentException();
        }
        if (populationLimit <= 0) {
            throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, populationLimit);
        }
        if (chromosomes.size() > populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE,
                chromosomes.size(), populationLimit, false);
        }
        this.populationLimit = populationLimit;
//        this.chromosomes = new TreeSet<>(Comparator.comparingDouble(Chromosome::getFitness));
        this.chromosomes = new ArrayList<Chromosome>(populationLimit);
        this.chromosomes.addAll(chromosomes);
    }
    
    public void addChromosomes(final Collection<Chromosome> chromosomeColl) throws NumberIsTooLargeException {
        if (chromosomes.size() + chromosomeColl.size() > populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE,
                chromosomes.size(), populationLimit, false);
        }
        this.chromosomes.addAll(chromosomeColl);
    }
    
    public List<Chromosome> getChromosomes() {
        return chromosomes.stream().toList();
    }
    
    protected List<Chromosome> getChromosomeList() {
        return new ArrayList<>(chromosomes);
    }
    
//    public TreeSet<Chromosome> getSortedChromosomes() {
//        return chromosomes;
//    }
    
    public void addChromosome(final Chromosome chromosome) throws NumberIsTooLargeException {
        if (chromosomes.size() >= populationLimit) {
            throw new NumberIsTooLargeException(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE,
                chromosomes.size(), populationLimit, false);
        }
        this.chromosomes.add(chromosome);
    }
    
    @Override
    public Chromosome getFittestChromosome() {
        Chromosome bestChromosome = getChromosomeList().getFirst();
        for (Chromosome chromosome : getChromosomeList()) {
            if (chromosome.compareTo(bestChromosome) < 0) {
                bestChromosome = chromosome;
            }
        }
        
        return bestChromosome;
    }
    
    public int getPopulationLimit() {
        return this.populationLimit;
    }
    
    public void setPopulationLimit(final int populationLimit) throws NotPositiveException, NumberIsTooSmallException {
        if (populationLimit <= 0) {
            throw new NotPositiveException(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, populationLimit);
        }
        if (populationLimit < chromosomes.size()) {
            throw new NumberIsTooSmallException(populationLimit, chromosomes.size(), true);
        }
        this.populationLimit = populationLimit;
    }
    
    public int getPopulationSize() {
        return this.chromosomes.size();
    }
    
    @Override
    public String toString() {
        return this.chromosomes.toString();
    }
    
    public Iterator<Chromosome> iterator() {
        return getChromosomes().iterator();
    }
}
