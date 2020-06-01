/**
 * 
 */
package io.coding.excercise.score.exception;

/**
 * Custom error to wrap all the error and catch it at one place using custom
 * exception handler.
 * 
 * @author subhash
 *
 */
public class ComputeScoreException extends RuntimeException {

	private static final long serialVersionUID = 5286924507853960692L;

	public ComputeScoreException(String message) {
		super(message);
	}

	public ComputeScoreException(Throwable th) {
		super(th);
	}

	public ComputeScoreException(String message, Throwable th) {
		super(message, th);
	}

}
