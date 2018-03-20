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
	
	public void displayBest(GeneticData best);

	public GeneticData[] generatePopulation();
}
