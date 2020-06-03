/**
 * 
 */
package io.coding.excercise.score.calculator;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.coding.excercise.score.modal.IndexWrapper;

/**
 * Implement the complete algorithm to compute the score based on the first
 * name.
 * 
 * @author subha
 *
 */
public class FirstNameScoreCalculator implements ScoreCalculator {
	private Stream<String> inputDataStream;
	private boolean isLargeDataSet;

	public FirstNameScoreCalculator(Stream<String> inputDataStream, boolean isLargeDataSet) {
		this.isLargeDataSet = isLargeDataSet;
		this.inputDataStream = inputDataStream;
	}

	/**
	 * Compute the score using algorithm define for first name.
	 * 
	 * @return Computed Score
	 */
	public Long calculate() {
		// if Data set is small the perform the in-line sorting on stream
		if (!isLargeDataSet) {
			inputDataStream = inputDataStream.sorted(withSorting());
		}
		// Holding the index of each record in the sorted stream
		AtomicLong atomicLong = new AtomicLong(1);
		return inputDataStream.filter(withFilter()).sorted().map(name -> new IndexWrapper(name, atomicLong.getAndIncrement()))
				.map(this::computeScoreByFirstName).reduce(0L, (a, b) -> a + b);

	}

	/**
	 * Compute the score of the individual name.
	 * 
	 * @param indexWrapper
	 * @return Computed score
	 */
	private Long computeScoreByFirstName(IndexWrapper indexWrapper) {
		char[] nameChars = indexWrapper.getName().trim().toUpperCase().toCharArray();
		int tempSum = 0;
		for (char ch : nameChars) {
			tempSum += (ch - 64);
		}
		return indexWrapper.getIndex() * tempSum;
	}

	/**
	 * Define the sorting algorithm and can be used out side of this class to
	 * perform the external sorting for large data set.
	 * 
	 * @return Comparator
	 */
	public static Comparator<String> withSorting() {
		return Comparator.comparing(String::toString);

	}
	
	/**
	 * Define custom filter to filter the record based on the define algorithm.
	 * 
	 * @return Predicate
	 */
	public static Predicate<String> withFilter() {
		return str -> str!=null && str.trim().length()>0;

	}


}
