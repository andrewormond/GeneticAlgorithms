package com.adeo8.genetic.taylor;

import java.util.LinkedHashMap;

import com.adeo8.genetic.GeneticConfig;

public class FitterConfig extends GeneticConfig{
	public double noiseModifier = 0;
	public int popSize = 25000;
	public double X_MIN = -1.5d;
	public double X_MAX = 1.5d;
	public int NUM_POINTS = 200;
	public int POWER = 5;
	public LinkedHashMap<Double, Double> testPoints;

	public FitterConfig() {
		this.NUM_ROUNDS = 10;
		this.verbose = true;
	}
}
