//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package coursework;

import java.util.ArrayList;
import java.util.Iterator;
import model.Fitness;
import model.Individual;
import model.NeuralNetwork;

public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
    public ExampleEvolutionaryAlgorithm() {
    }

    public void run() {
        this.population = this.initialise();
        this.best = this.getBest();
        System.out.println("Best From Initialisation " + this.best);

        while(this.evaluations < Parameters.maxEvaluations) {
            Individual parent1 = this.select();
            Individual parent2 = this.select();
            ArrayList<Individual> children = this.reproduce(parent1, parent2);
            this.mutate(children);
            this.evaluateIndividuals(children);
            this.replace(children);
            this.best = this.getBest();
            this.outputStats();
        }

        this.saveNeuralNetwork();
    }

    private void evaluateIndividuals(ArrayList<Individual> individuals) {
        Individual individual;
        for(Iterator var3 = individuals.iterator(); var3.hasNext(); individual.fitness = Fitness.evaluate(individual, this)) {
            individual = (Individual)var3.next();
        }

    }

    private Individual getBest() {
        this.best = null;
        Iterator var2 = this.population.iterator();

        while(var2.hasNext()) {
            Individual individual = (Individual)var2.next();
            if (this.best == null) {
                this.best = individual.copy();
            } else if (individual.fitness < this.best.fitness) {
                this.best = individual.copy();
            }
        }

        return this.best;
    }

    private ArrayList<Individual> initialise() {
        this.population = new ArrayList();

        for(int i = 0; i < Parameters.popSize; ++i) {
            Individual individual = new Individual();
            this.population.add(individual);
        }

        this.evaluateIndividuals(this.population);
        return this.population;
    }

    private Individual select() {
        Individual parent = (Individual)this.population.get(Parameters.random.nextInt(Parameters.popSize));
        return parent.copy();
    }

    private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList();
        children.add(parent1.copy());
        children.add(parent2.copy());
        return children;
    }

    private void mutate(ArrayList<Individual> individuals) {
        Iterator var3 = individuals.iterator();

        while(var3.hasNext()) {
            Individual individual = (Individual)var3.next();

            for(int i = 0; i < individual.chromosome.length; ++i) {
                if (Parameters.random.nextDouble() < Parameters.mutateRate) {
                    double[] var10000;
                    if (Parameters.random.nextBoolean()) {
                        var10000 = individual.chromosome;
                        var10000[i] += Parameters.mutateChange;
                    } else {
                        var10000 = individual.chromosome;
                        var10000[i] -= Parameters.mutateChange;
                    }
                }
            }
        }

    }

    private void replace(ArrayList<Individual> individuals) {
        Iterator var3 = individuals.iterator();

        while(var3.hasNext()) {
            Individual individual = (Individual)var3.next();
            int idx = this.getWorstIndex();
            this.population.set(idx, individual);
        }

    }

    private int getWorstIndex() {
        Individual worst = null;
        int idx = -1;

        for(int i = 0; i < this.population.size(); ++i) {
            Individual individual = (Individual)this.population.get(i);
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

    public double activationFunction(double x) {
        if (x < -20.0D) {
            return -1.0D;
        } else {
            return x > 20.0D ? 1.0D : Math.tanh(x);
        }
    }
}
