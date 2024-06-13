package edu.iate.ism22.schedule.entity.genetic.custom;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;

import java.util.ArrayList;
import java.util.List;

public class RouletteWheelSelection implements SelectionPolicy {
    
    @Override
    public ChromosomePair select(Population population) throws MathIllegalArgumentException {
        return new ChromosomePair(tournament((CustomListPopulation) population),
            tournament((CustomListPopulation) population));
    }
    
    private Chromosome tournament(CustomListPopulation population) {
        
        // Calculate the inverse fitness for each chromosome
        List<Double> inverseFitness = new ArrayList<>();
        double totalInverseFitness = 0.0;
        for (Chromosome chromosome : population) {
            double inverseFit = 1.0 / chromosome.getFitness();
            inverseFitness.add(inverseFit);
            totalInverseFitness += inverseFit;
        }
        
        double randomValue = CustomGeneticAlgorithm.getRandomGenerator().nextDouble() * totalInverseFitness;
        double cumulativeInverseFitness = 0.0;
        
        for (int j = 0; j < population.getPopulationSize(); j++) {
            cumulativeInverseFitness += inverseFitness.get(j);
            if (cumulativeInverseFitness >= randomValue) {
                return population.getChromosomeList().get(j);
            }
        }
        
        // Fallback in case of rounding errors
        return population.getChromosomeList().getLast();
    }
}
