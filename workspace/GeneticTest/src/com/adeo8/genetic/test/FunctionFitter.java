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

	public static int popSize = 15000;
	public static final double X_MIN = -1d;
	public static final double X_MAX = 1d;
	public static final int NUM_POINTS = 200;
	public static final int NUM_ROUNDS = 10;
	public static final int POWER = 15;
	LinkedHashMap<Double, Double> testPoints;
	GeneticSystem system;

	public FunctionFitter(LinkedHashMap<Double, Double> points) {
		testPoints = points;
		system = new GeneticSystem(this);
		popSize -= (popSize % system.numThreads);
		long st = System.currentTimeMillis();
		Map.Entry<GeneticData, Double> result = system.start(NUM_ROUNDS);
		long tm = System.currentTimeMillis() - st;
		DoubleData winner = (DoubleData) result.getKey();

		System.out.println("Final doubles: " + winner);
		System.out.println("Fitness: " + this.getFitness(winner));
		System.out.println(String.format("Took %.3f seconds", tm / 1000d));

		DoubleData[] finalPop = new DoubleData[system.population.length];
		for (int i = 0; i < finalPop.length; i++) {
			finalPop[i] = (DoubleData) system.population[i];
		}
		// U.plotAll(finalPop, X_MIN, X_MAX, NUM_POINTS);
		plotLine(winner);
		Utils.plotHistogram(system.population, this, 75,
				"\"Final Histogram\" Best=" + String.format("%.3f", this.getFitness(winner)));

	}

	public static LinkedHashMap<Double, Double> generatePoints() {
		double[] doubles = new double[POWER];
		Random r = new Random();
		for (int i = 0; i < POWER; i++) {
			doubles[i] = 6 * (r.nextDouble() - 0.5d);
		}
		System.out.print("True doubles:  ");
		DoubleData trueData = new DoubleData(doubles);
		System.out.println(trueData);
		LinkedHashMap<Double, Double> points = U.evaluateSeries(doubles, X_MIN, X_MAX, NUM_POINTS);
		double noise = 0;
		for (double d : points.keySet()) {
			points.put(d, points.get(d) + noise * 2 * (r.nextDouble() - 0.5d));
		}
		return points;
	}

	public static void main(String[] args) {
		LinkedHashMap<Double, Double> points = generatePoints();
		FunctionFitter fitter = new FunctionFitter(points);
	}

	public void plotLine(DoubleData data) {

		LinkedHashMap<Double, Double> points = U.evaluateSeries(data.toDoubles(), X_MIN, X_MAX, NUM_POINTS);
		U.plotPoints(data.toString(), points, this.testPoints);
	}

	@Override
	public double getFitness(GeneticData data) {
		DoubleData line = (DoubleData) data;
		LinkedHashMap<Double, Double> points = U.evaluateSeries(line.toDoubles(), X_MIN, X_MAX, NUM_POINTS);
		double r = 0;
		for (double x = X_MIN; x <= X_MAX; x += (X_MAX - X_MIN) / NUM_POINTS) {
			r += Math.pow(points.get(x) - this.testPoints.get(x), 2);
		}
		return Math.sqrt(r);
	}

	@Override
	public GeneticData[] generatePopulation() {
		DoubleData[] population = new DoubleData[popSize];
		Random r = new Random();
		int noise = 10;
		double[] doubles = new double[POWER];
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < POWER; j++) {
				doubles[j] = 2 * noise * (r.nextDouble() - 0.5d);
			}
			population[i] = new DoubleData(doubles);
		}
		return population;
	}

}
