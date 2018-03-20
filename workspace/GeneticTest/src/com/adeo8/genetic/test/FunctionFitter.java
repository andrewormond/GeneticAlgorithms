package com.adeo8.genetic.test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.adeo8.genetic.DoubleData;
import com.adeo8.genetic.GeneticData;
import com.adeo8.genetic.GeneticHandler;
import com.adeo8.genetic.GeneticSystem;
import com.adeo8.genetic.Utils;

public class FunctionFitter implements GeneticHandler {

	FitterConfig config;
	GeneticSystem system;

	public FunctionFitter(FitterConfig config) {
		this.config = config;
		system = new GeneticSystem(this, config);
		system.config.maxLog10_fitness = 1.2d * config.POWER;
		config.popSize -= (config.popSize % system.config.number_threads);

	}

	public DoubleData run() {
		long st = System.currentTimeMillis();
		Map.Entry<GeneticData, Double> result = system.start();
		long tm = System.currentTimeMillis() - st;
		DoubleData winner = (DoubleData) result.getKey();

		System.out.println(String.format("Took %.3f seconds", tm / 1000d));

		DoubleData[] finalPop = new DoubleData[system.population.length];
		for (int i = 0; i < finalPop.length; i++) {
			finalPop[i] = (DoubleData) system.population[i];
		}

		// Display Graphs
		U.plotLine(winner, config);
		Utils.plotHistogram(system.population, this, 75,
				"\"Final Histogram\" Best=" + String.format("%.3f", this.getFitness(winner)), system.config);
		return winner;
	}

	@Override
	public double getFitness(GeneticData data) {
		DoubleData line = (DoubleData) data;
		LinkedHashMap<Double, Double> points = U.evaluateSeries(line.toDoubles(), config.X_MIN, config.X_MAX,
				config.NUM_POINTS);
		double r = 0;
		for (double x = config.X_MIN; x <= config.X_MAX; x += (config.X_MAX - config.X_MIN) / config.NUM_POINTS) {
			r += Math.pow(points.get(x) - config.testPoints.get(x), 2);
		}
		return Math.sqrt(r);
	}

	@Override
	public GeneticData[] generatePopulation() {
		DoubleData[] population = new DoubleData[config.popSize];
		Random r = new Random();
		int noise = 10;
		double[] doubles = new double[config.POWER];
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < config.POWER; j++) {
				doubles[j] = 2 * noise * (r.nextDouble() - 0.5d);
			}
			population[i] = new DoubleData(doubles);
		}
		return population;
	}

	@Override
	public void displayBest(GeneticData best) {
		U.plotLine((DoubleData) best, config);

	}

	private static DoubleData trueDoubles;

	public static LinkedHashMap<Double, Double> generatePoints(FitterConfig config) {
		double[] doubles = new double[config.POWER];
		Random r = new Random();
		for (int i = 0; i < config.POWER; i++) {
			doubles[i] = 6 * (r.nextDouble() - 0.5d);
		}
		LinkedHashMap<Double, Double> points = U.evaluateSeries(doubles, config.X_MIN, config.X_MAX, config.NUM_POINTS);
		double noise = Math.pow(config.X_MAX, config.POWER - 1) * config.noiseModifier;
		for (double d : points.keySet()) {
			points.put(d, points.get(d) + noise * 2 * (r.nextDouble() - 0.5d));
		}
		trueDoubles = new DoubleData(doubles);
		return points;
	}

	public static void main(String[] args) {
		FitterConfig config = new FitterConfig();
		LinkedHashMap<Double, Double> points = generatePoints(config);
		config.testPoints = points;
		FunctionFitter fitter = new FunctionFitter(config);
		System.out.print("True doubles:     ");
		System.out.println(trueDoubles);
		DoubleData best = fitter.run();
		System.out.print("Best fit doubles: ");
		System.out.println(best);
	}

}
