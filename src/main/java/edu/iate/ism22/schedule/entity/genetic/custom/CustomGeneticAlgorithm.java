package edu.iate.ism22.schedule.entity.genetic.custom;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public class CustomGeneticAlgorithm {
    private static RandomGenerator randomGenerator = new JDKRandomGenerator();
    
    /**
     * the crossover policy used by the algorithm.
     */
    private final CrossoverPolicy crossoverPolicy;
    
    /**
     * the rate of crossover for the algorithm.
     */
    private final double crossoverRate;
    
    /**
     * the mutation policy used by the algorithm.
     */
    private final MutationPolicy mutationPolicy;
    
    /**
     * the rate of mutation for the algorithm.
     */
    private final double mutationRate;
    
    /**
     * the selection policy used by the algorithm.
     */
    private final SelectionPolicy selectionPolicy;
    
    /**
     * the number of generations evolved to reach {@link StoppingCondition} in the last run.
     */
    private int generationsEvolved = 0;
    
    public CustomGeneticAlgorithm(final CrossoverPolicy crossoverPolicy,
                                  final double crossoverRate,
                                  final MutationPolicy mutationPolicy,
                                  final double mutationRate,
                                  final SelectionPolicy selectionPolicy) throws OutOfRangeException {
        
        if (crossoverRate < 0 || crossoverRate > 1) {
            throw new OutOfRangeException(LocalizedFormats.CROSSOVER_RATE,
                crossoverRate, 0, 1);
        }
        if (mutationRate < 0 || mutationRate > 1) {
            throw new OutOfRangeException(LocalizedFormats.MUTATION_RATE,
                mutationRate, 0, 1);
        }
        this.crossoverPolicy = crossoverPolicy;
        this.crossoverRate = crossoverRate;
        this.mutationPolicy = mutationPolicy;
        this.mutationRate = mutationRate;
        this.selectionPolicy = selectionPolicy;
    }
    
    public static synchronized void setRandomGenerator(final RandomGenerator random) {
        randomGenerator = random;
    }
    
    public static synchronized RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }
    
    public Population evolve(final Population initial, final StoppingCondition condition) {
        Population current = initial;
        generationsEvolved = 0;
        while (!condition.isSatisfied(current)) {
            current = nextGeneration(current);
            generationsEvolved++;
        }
        return current;
    }
    
    public Population nextGeneration(final Population current) {
        // создаем следующую популяцию
        Population nextGeneration = current.nextGeneration();
        
        RandomGenerator randGen = getRandomGenerator();
        while (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
            
            // производим отбор
            ChromosomePair pair = getSelectionPolicy().select(current);
            
            // применяем кроссовер (скрещивание)
            if (randGen.nextDouble() < getCrossoverRate()) {
                pair = getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
            }
            
            // применяем мутацию
            if (randGen.nextDouble() < getMutationRate()) {
                pair = new ChromosomePair(
                    getMutationPolicy().mutate(pair.getFirst()),
                    getMutationPolicy().mutate(pair.getSecond())
                );
            }
            
            // добавляем полученные хромосомы в новую популяцию
            nextGeneration.addChromosome(pair.getFirst());
            if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
                nextGeneration.addChromosome(pair.getSecond());
            }
        }
        return nextGeneration;
    }
    
    public CrossoverPolicy getCrossoverPolicy() {
        return crossoverPolicy;
    }
    
    public double getCrossoverRate() {
        return crossoverRate;
    }
    
    public MutationPolicy getMutationPolicy() {
        return mutationPolicy;
    }
    
    public double getMutationRate() {
        return mutationRate;
    }
    
    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }
    
    public int getGenerationsEvolved() {
        return generationsEvolved;
    }
}
