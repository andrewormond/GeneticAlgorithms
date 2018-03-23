package com.adeo8.genetic.gcd;

import java.util.Map;
import java.util.Random;

import com.adeo8.genetic.GeneticData;
import com.adeo8.genetic.GeneticHandler;
import com.adeo8.genetic.GeneticSystem;

public class GcdFitter implements GeneticHandler {

	GcdConfig config;
	GeneticSystem system;

	public GcdFitter(int n1, int n2) {
		config = new GcdConfig(n1, n2);
		system = new GeneticSystem(this, config);

	}

	public static void main(String[] args) {
		Random r = new Random();
		double sum = 0;
		int numTrials = 20;
		for (int i = 0; i < numTrials; i++) {
			int factor = 100 + r.nextInt(1000);
			GcdFitter fitter = new GcdFitter(factor * (1 + r.nextInt(100)), factor * (1 + r.nextInt(100)));
			if (fitter.run()) {
				sum += 1;
			}
		}
		sum /= numTrials;
		System.out.println(String.format("Was successful %.3f", sum * 100) + "% of the time.");
	}

	public boolean run() {
		System.out.println("Running: " + config);
		Map.Entry<GeneticData, Double> result = system.start();
		IncIntData winner = (IncIntData) result.getKey();
		int real = Euclid.gcd2(config.n1, config.n2);
		System.out.println("GCD(" + config.n1 + ", " + config.n2 + ") =      " + real);
		System.out.println("Best GCD(" + config.n1 + ", " + config.n2 + ") = " + winner.get(0));
		return real == winner.get(0);
	}

	@Override
	public double getFitness(GeneticData data) {
		int n = ((IncIntData) data).get(0);
		double fitness = (config.n1 % n) + (config.n2 % n);
		if (fitness != 0) {
			fitness = config.max + fitness;
		} else {
			fitness = 1d / n;
		}
		return fitness;
	}

	@Override
	public void displayBest(GeneticData best) {
	}

	@Override
	public GeneticData[] generatePopulation() {
		IncIntData[] population = new IncIntData[config.PopulationSize];
		Random r = new Random();
		for (int i = 0; i < config.PopulationSize; i++) {
			population[i] = new IncIntData(new Integer[] { 1 + r.nextInt(config.max - 1) }, config.max);
		}
		return population;
	}

}
