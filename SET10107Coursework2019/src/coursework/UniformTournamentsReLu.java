package coursework;

import model.Fitness;
import model.Individual;
import model.NeuralNetwork;

import java.util.ArrayList;

public class UniformTournamentsReLu extends NeuralNetwork {


    /**
     * The Main Evolutionary Loop
     */
    @Override
    public void run() {
        //Initialise a population of Individuals with random weights
        population = initialise();

        //Record a copy of the best Individual in the population
        best = getBest();
        System.out.println("Best From Initialisation " + best);

        /**
         * main EA processing loop
         */

        while (evaluations < Parameters.maxEvaluations) {

            /**
             * this is a skeleton EA - you need to add the methods.
             * You can also change the EA if you want
             * You must set the best Individual at the end of a run
             *
             */

            // Select 2 Individuals from the current population. Currently returns random Individual
            Individual parent1 = select();
            Individual parent2 = select();

            // Generate a child by crossover. Not Implemented
            ArrayList<Individual> children = reproduce(parent1, parent2);

            //mutate the offspring
            mutate(children);

            // Evaluate the children
            evaluateIndividuals(children);

            // Replace children in population
            replace(children);

            // check to see if the best has improved
            best = getBest();

            // Implemented in NN class.
            outputStats();

            //Increment number of completed generations
        }

        //save the trained network to disk
        saveNeuralNetwork();
    }



    /**
     * Sets the fitness of the individuals passed as parameters (whole population)
     *
     */
    private void evaluateIndividuals(ArrayList<Individual> individuals) {
        for (Individual individual : individuals) {
            individual.fitness = Fitness.evaluate(individual, this);
        }
    }

    /**
     * Returns a copy of the best individual in the population
     *
     */
    private Individual getBest() {
        best = null;;
        for (Individual individual : population) {
            if (best == null) {
                best = individual.copy();
            } else if (individual.fitness < best.fitness) {
                best = individual.copy();
            }
        }
        return best;
    }

    /**
     * Generates a randomly initialised population
     *
     */
    private ArrayList<Individual> initialise() {
        population = new ArrayList<>();
        for (int i = 0; i < Parameters.popSize; ++i) {
            //chromosome weights are initialised randomly in the constructor
            Individual individual = new Individual();
            population.add(individual);
        }
        evaluateIndividuals(population);
        return population;
    }

    private Individual select() {

        // tournament selection picks x random chromosomes and returns the fittest one

        // pick a fighter
        Individual bestFighter = getRandomIndividual();

        // pick tnSize-1 more and then see which is the best
        for (int i = 0; i < population.size()/3; i++)
        {
            Individual challenger = getRandomIndividual();
            if (challenger.fitness < bestFighter.fitness)
            {
                bestFighter = challenger;
            }
        }
        return (bestFighter); //return the winner
    }

    private Individual getRandomIndividual() {
        int randomNum = Parameters.random.nextInt(Parameters.popSize);

        return population.get(randomNum);
    }


    /**
     * Crossover / Reproduction
     *
     * NEEDS REPLACED with proper method this code just returns exact copies of the
     * parents.
     */
    private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList<>();

        Individual child1 = new Individual();
        Individual child2 = new Individual();

        for (int i = 0; i < Parameters.getNumGenes(); i++) {
            if (Parameters.random.nextFloat() < 0.5) {
                child1.chromosome[i] += parent1.chromosome[i];
                child2.chromosome[i] += parent2.chromosome[i];
            } else {
                child1.chromosome[i] += parent2.chromosome[i];
                child2.chromosome[i] += parent1.chromosome[i];
            }
        }

        children.add(child1);
        children.add(child2);

        return children;
    }

    /**
     * Mutation
     *
     *
     */
    private void mutate(ArrayList<Individual> individuals) {
/*
        Parameters.setMutateRate(0.1);
        Parameters.setMutateChange(0.15);
*/

        for(Individual individual : individuals) {
            /*if (evaluations > 15000) {
                Parameters.setMutateRate(0.3);
                Parameters.setMutateChange(0.35);
            }*/
            for (int i = 0; i < individual.chromosome.length; i++) {
                if (Parameters.random.nextDouble() < Parameters.mutateRate) {
                    if (Parameters.random.nextBoolean()) {
                        individual.chromosome[i] += (Parameters.mutateChange);
                    } else {
                        individual.chromosome[i] -= (Parameters.mutateChange);
                    }
                }
            }
        }
    }

    private void replace(ArrayList<Individual> individuals) {

        for(Individual individual : individuals) {

            Individual worstFighter = getRandomIndividual();

            // pick tnSize-1 more and then see which is the best
            for (int i = 0; i < population.size()/3; i++)
            {
                Individual challenger = getRandomIndividual();
                if (challenger.fitness > worstFighter.fitness)
                {
                    worstFighter = challenger;
                }
            }

            int index = population.indexOf(worstFighter);
            population.set(index, individual);
        }
    }



    /**
     * Returns the index of the worst member of the population
     * @return
     */
    private int getWorstIndex() {
        Individual worst = null;
        int idx = -1;
        for (int i = 0; i < population.size(); i++) {
            Individual individual = population.get(i);
            if (worst == null) {
                worst = individual;
                idx = i;
            } else if (individual.fitness > worst.fitness) {
                worst = individual;
                idx = i;
            }
        }
        return idx;
    }

    @Override
    public double activationFunction(double x) {
        if (x < -10.0) {
            return -1.0;
        } else if (x > 10.0) {
            return 1.0;
        }
        //ReLu function
        double result = Math.max(0,x);

        if (result == 0) {
            return -1.0;
        }

        return result;
    }
}
