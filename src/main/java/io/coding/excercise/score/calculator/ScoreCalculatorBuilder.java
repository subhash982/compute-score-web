/**
 * 
 */
package io.coding.excercise.score.calculator;

import java.util.stream.Stream;

import io.coding.excercise.score.constants.ScoreConstants;
import io.coding.excercise.score.exception.ComputeScoreException;

/**
 * Score Algorithm builder to construct the algorithm. This can be extended in
 * future if the algorithm complexity increases.
 * 
 * @author subha
 *
 */
public class ScoreCalculatorBuilder {

	/**
	 * Build the score algorithms.
	 * 
	 * @param inputDataStream
	 * @param isLargeDataSet
	 * @param scoreAlgorithm
	 * @return
	 */
	public static ScoreCalculator buildWith(Stream<String> inputDataStream, boolean isLargeDataSet,
			String scoreAlgorithm) {
		if (ScoreConstants.SCORE_BY_FIRST_NAME.endsWith(scoreAlgorithm)) {
			return new FirstNameScoreCalculator(inputDataStream, isLargeDataSet);
		} else if (ScoreConstants.SCORE_BY_FULL_NAME.endsWith(scoreAlgorithm)) {
			return new FullNameScoreCalculator(inputDataStream, isLargeDataSet);
		} else {
			throw new ComputeScoreException(
					"Scoring algorithm is not supported. Possible value can be from [scoreByFirstName, scoreByFullName].");
		}
	}
}
