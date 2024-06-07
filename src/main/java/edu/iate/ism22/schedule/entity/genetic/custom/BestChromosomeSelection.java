//package edu.iate.ism22.schedule.entity.genetic.custom;
//
//import org.apache.commons.math3.exception.MathIllegalArgumentException;
//import org.apache.commons.math3.genetics.Chromosome;
//import org.apache.commons.math3.genetics.ChromosomePair;
//import org.apache.commons.math3.genetics.Population;
//import org.apache.commons.math3.genetics.SelectionPolicy;
//
//public class BestChromosomeSelection implements SelectionPolicy {
//
//    @Override
//    public ChromosomePair select(Population population) throws MathIllegalArgumentException {
//        return selecting((CustomListPopulation) population);
//    }
//
//    private ChromosomePair selecting(CustomListPopulation population) {
//
//        Chromosome first = population.getSortedChromosomes().getFirst();
//        population.getSortedChromosomes().removeFirst();
//        Chromosome second = population.getSortedChromosomes().getFirst();
//        population.getSortedChromosomes().removeFirst();
//
//        return new ChromosomePair(first, second);
//    }
//}
