/**
 * 
 */
package com.adeo8.genetic;

/**
 * @author adeo8
 *
 */
public interface GeneticData extends Cloneable {
	public GeneticData mutate();
	public GeneticData breed(GeneticData other);
}
