/**
 * 
 */
package com.adeo8.genetic;

/**
 * @author adeo8
 *
 */
public interface GeneticHandler {
	public double getFitness(GeneticData data);

	public GeneticData[] generatePopulation();
}
