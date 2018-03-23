package com.adeo8.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GeneticSystem {

	public GeneticHandler handler;
	public GeneticData[] population;

	public GeneticConfig config;

	public GeneticSystem(GeneticHandler handler, GeneticConfig config) {
		this.config = config;
		this.handler = handler;
	}

	private boolean anyRunning(Thread[] threads) {
		for (Thread t : threads) {
			if (t.isAlive()) {
				return true;
			}
		}
		return false;
	}

	private boolean doRound(int round) {
		ArrayList<Map.Entry<GeneticData, Double>> results = testPopulation();
		Map.Entry<GeneticData, Double> res = getMostFit(results);

		if (config.verbose) {
			Utils.plotHistogram(population, this.handler, 50,
					"\"Round #" + round + "\" Best=" + String.format("%.3f", res.getValue()), config);
			handler.displayBest(res.getKey());
		}
		System.out.println("\"Round #" + round + "\" Best="+res.getKey()+" : " + String.format("%.3f", res.getValue()));

		try {
			Thread.sleep(config.waitBeforeRound);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int eliteAmount = (int) (population.length * config.percentage_elite);
		int carryAmount = (int) (population.length * config.percentage_carry);
		int reserved = eliteAmount + carryAmount;
		int nonReserved = population.length - reserved;
		int splitIndex = reserved + (int) (nonReserved * config.ratio_mutateToBreed);
		GeneticData[] sortedPop = new GeneticData[population.length];
		for (int i = 0; i < population.length; i++) {
			sortedPop[i] = results.get(i).getKey();
		}
		GeneticData[] nextGen = new GeneticData[population.length];
		Thread[] threads = new Thread[config.number_threads];
		GeneticProcess[] procc = new GeneticProcess[config.number_threads];
		if (nextGen.length % config.number_threads != 0) {
			System.err.println("Warning: population not divisible by threads");
		}
		int iLength = nextGen.length / config.number_threads;
		for (int t = 0; t < config.number_threads; t++) {
			procc[t] = new GeneticProcess(t, t * iLength, (t + 1) * iLength, sortedPop, eliteAmount, splitIndex,
					carryAmount);
			threads[t] = new Thread(procc[t]);

		}

		for (Thread t : threads) {
			t.start();
		}

		while (anyRunning(threads)) {
			for (int t = 0; t < threads.length; t++) {
				if (!threads[t].isAlive() && procc[t] != null) {
					System.arraycopy(procc[t].data, 0, nextGen, t * iLength, iLength);
					procc[t] = null;
				}
			}
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int t = 0; t < config.number_threads; t++) {
			if (procc[t] != null) {
				System.arraycopy(procc[t].data, 0, nextGen, t * iLength, iLength);
			}
		}

		population = nextGen;
		population[0] = res.getKey();

		return false;

	}

	public ArrayList<Map.Entry<GeneticData, Double>> testPopulation() {
		HashMap<GeneticData, Double> results = new HashMap<GeneticData, Double>();
		int i = 0;
		for (GeneticData data : population) {
			while (results.containsKey(data)) {
				data = data.mutate();
			}

			results.put(data, handler.getFitness(data));
		}

		return new ArrayList<>(Utils.entriesSortedByValues(results));
		// return Utils.sortByValue(results);

	}

	public Map.Entry<GeneticData, Double> getMostFit(ArrayList<Entry<GeneticData, Double>> results) {
		Map.Entry<GeneticData, Double> best = null;
		for (Map.Entry<GeneticData, Double> entry : results) {
			if (best == null || entry.getValue() < best.getValue()) {
				best = entry;
			}
		}
		return best;
	}

	public Map.Entry<GeneticData, Double> getMostFit() {
		ArrayList<Entry<GeneticData, Double>> results = testPopulation();
		return getMostFit(results);
	}

	public Map.Entry<GeneticData, Double> start() {
		population = handler.generatePopulation();

		for (int round = 0; round < config.NUM_ROUNDS; round++) {
			if (doRound(round)) {
				break;
			}
		}

		return this.getMostFit();
	}
}
