package com.adeo8.genetic.gcd;

import com.adeo8.genetic.GeneticConfig;

public class GcdConfig extends GeneticConfig {

	public final int n1;
	public final int n2;
	public final int max;
	public final int PopulationSize = 10000;

	public GcdConfig(int n1, int n2) {
		this.n1 = n1;
		this.n2 = n2;
		if (n1 > n2) {
			max = n2;
		} else {
			max = n1;
		}
		this.NUM_ROUNDS = 3;

		this.setPercentage_carry(0.1d);
		this.setPercentage_elite(0.05d);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "GCD(" + n1 + ", " + n2 + ") population: " + this.PopulationSize + " over " + this.NUM_ROUNDS
				+ " rounds.";
	}

}
