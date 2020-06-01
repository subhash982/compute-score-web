/**
 * 
 */
package io.coding.excercise.score.calculator;

/**
 * @author subha
 *
 */
public interface ScoreCalculator {
	
	/**
	 * Compute the score based on the define algorithm in the corresponding subclasses.
	 * 
	 * @return Computed score
	 */
	public Long calculate();
}
