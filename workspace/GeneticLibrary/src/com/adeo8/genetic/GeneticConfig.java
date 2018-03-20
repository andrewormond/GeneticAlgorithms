package com.adeo8.genetic;

public class GeneticConfig {

	public double percentage_elite = 0.01f;
	public double percentage_carry = 0.1f;
	public double ratio_mutateToBreed = 0.5f;
	public int number_threads = 8;
	public double maxLog10_fitness = 10d;
	public int NUM_ROUNDS = 10;
	
	public long waitBeforeRound = 0;

	public GeneticConfig() {

	}

	public void setPercentage_elite(double percentage) {
		this.percentage_elite = Utils.clamp(percentage);
	}

	public void setPercentage_carry(double percentage) {
		this.percentage_carry = Utils.clamp(percentage);
	}

	public void setRatio_mutateToBreed(double ratio) {
		this.ratio_mutateToBreed = Utils.clamp(ratio);
	}

	public void setThreads(int number_of_threads) {
		if (number_of_threads < 1) {
			this.number_threads = 1;
		} else {
			this.number_threads = number_of_threads;
		}
	}

}
