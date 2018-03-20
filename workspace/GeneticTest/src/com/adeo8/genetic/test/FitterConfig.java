package com.adeo8.genetic.test;

import java.util.LinkedHashMap;

import com.adeo8.genetic.GeneticConfig;

public class FitterConfig extends GeneticConfig{
	public double noiseModifier = 0;
	public int popSize = 15000;
	public double X_MIN = -10d;
	public double X_MAX = 10d;
	public int NUM_POINTS = 200;
	public int POWER = 8;
	public LinkedHashMap<Double, Double> testPoints;

	public FitterConfig() {

	}
}
