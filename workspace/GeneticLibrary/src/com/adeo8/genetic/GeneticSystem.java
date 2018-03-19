package com.adeo8.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GeneticSystem {

	public GeneticHandler handler;
	public GeneticData[] population;

	public float eliteCarry = 0.05f;
	public float otherCarry = 0.1f;
	public float mutateToBreedRatio = 0.5f;
	public float maxFitness = 1f;
	public int numThreads = 8;

	public GeneticSystem(GeneticHandler handler) {
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
		Utils.plotHistogram(population, this.handler, 50,
				"\"Round #" + round + "\" Best=" + String.format("%.3f", res.getValue()));
		// System.out.println("First for round #" + round + ": [" + res.getValue() + "]
		// " + res.getKey());
		if (res.getValue() < maxFitness) {
			return true;
		}

		int eliteAmount = (int) (population.length * eliteCarry);
		int otherAmount = (int) (population.length * otherCarry);
		int reserved = eliteAmount + otherAmount;
		int nonReserved = population.length - reserved;
		int splitIndex = reserved + (int) (nonReserved * this.mutateToBreedRatio);
		GeneticData[] sortedPop = new GeneticData[population.length];
		for (int i = 0; i < population.length; i++) {
			sortedPop[i] = results.get(i).getKey();
		}
		GeneticData[] nextGen = new GeneticData[population.length];
		Thread[] threads = new Thread[numThreads];
		GeneticProcess[] procc = new GeneticProcess[numThreads];
		if (nextGen.length % numThreads != 0) {
			System.err.println("Warning: population not divisible by threads");
		}
		int iLength = nextGen.length / numThreads;
		for (int t = 0; t < numThreads; t++) {
			procc[t] = new GeneticProcess(t, t * iLength, (t + 1) * iLength, sortedPop, eliteAmount, splitIndex,
					otherAmount);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int t = 0; t < numThreads; t++) {
			if (procc[t] != null) {
				System.arraycopy(procc[t].data, 0, nextGen, t * iLength, iLength);
			}
		}

		population = nextGen;

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

	public Map.Entry<GeneticData, Double> start(int numRounds) {
		population = handler.generatePopulation();

		for (int round = 0; round < numRounds; round++) {
			if (doRound(round)) {
				break;
			}
		}

		return this.getMostFit();
	}
}
