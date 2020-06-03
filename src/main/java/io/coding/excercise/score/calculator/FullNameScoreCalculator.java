/**
 * 
 */
package io.coding.excercise.score.calculator;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import io.coding.excercise.score.modal.IndexWrapper;

/**
 * @author subha
 *
 */
public class FullNameScoreCalculator implements ScoreCalculator {
	private Stream<String> inputDataStream;
	private boolean isLargeDataSet;

	public FullNameScoreCalculator(Stream<String> inputDataStream, boolean isLargeDataSet) {
		this.isLargeDataSet = isLargeDataSet;
		this.inputDataStream = inputDataStream;
	}

	/**
	 * Compute the score using algorithm define for first name.
	 * 
	 * @return Computed Score
	 */
	@Override
	public Long calculate() {

		// if Data set is small the perform the in-line sorting on stream
		if(!isLargeDataSet) {
			inputDataStream = inputDataStream.sorted(withSorting());
		}
		// Holding the index of each record in the sorted stream
		AtomicLong atomicLong = new AtomicLong(1);
		return inputDataStream.sorted().map(name -> new IndexWrapper(name, atomicLong.getAndIncrement()))
				.map(this::computeScoreByFullName).reduce(0L, (a, b) -> a + b);
	}

	/**
	 * Compute the score of the individual full name.
	 * 
	 * @param indexWrapper
	 * @return Computed score
	 */
	private long computeScoreByFullName(IndexWrapper indexWrapper) {
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
	public static Comparator<String> withSorting(){
		return  Comparator.comparing(String :: toString);
		
	}

}
