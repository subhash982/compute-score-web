/**
 * 
 */
package io.coding.excercise.score.service;

/**
 * Interface to define the API for the computing the score
 * 
 * @author subhash
 *
 */
public interface ComputeScoreService {

	/**
	 * Read all the data from the file and compute the score by delegating to the
	 * corresponding algorithm.
	 * 
	 * @param filePath
	 * @param scoringAlgorith
	 * @return Computed Score
	 */
	public Long compute(String filePath, String scoringAlgorith);

}
