package com.adeo8.genetic;

import java.util.Random;

public class GeneticProcess implements Runnable {
	GeneticData[] population;
	GeneticData[] data;
	int id;
	int eliteAmount;
	int otherAmount;
	int iMin;
	int splitIndex;

	public GeneticProcess(int ID, int iMin, int iMax, GeneticData[] population, int eliteAmount, int splitIndex,
			int otherAmount) {
		this.id = ID;
		this.splitIndex = splitIndex;
		this.iMin = iMin;
		this.population = population.clone();
		this.eliteAmount = eliteAmount;
		this.otherAmount = otherAmount;
		data = new GeneticData[iMax - iMin];
		System.arraycopy(population, iMin, data, 0, data.length);
	}

	@Override
	public void run() {
		int reserved = eliteAmount + otherAmount;
		int nonReserved = population.length - reserved;
		Random r = new Random();
		for (int i = 0; i < data.length; i++) {

			if (i + iMin < eliteAmount) {
				data[i] = population[i];
			} else if (i < reserved) {
				data[i] = population[reserved + r.nextInt(nonReserved)];
			}
			if (i + iMin < splitIndex) {

				data[i] = population[i].mutate();
			} else {
				int i1 = r.nextInt(population.length);
				int i2;
				do {
					i2 = r.nextInt(population.length);
				} while (i1 != i2);
				data[i] = population[i1].breed(population[i2]);
			}

		}

	}

}
