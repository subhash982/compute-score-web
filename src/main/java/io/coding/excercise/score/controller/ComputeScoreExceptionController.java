/**
 * 
 */
package io.coding.excercise.score.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.modal.ErrorResponse;

/**
 * @author subha
 *
 */
@ControllerAdvice
public class ComputeScoreExceptionController {
	
	 @ExceptionHandler(value = ComputeScoreException.class)
	  public ResponseEntity<Object> handleComputeScoreException(ComputeScoreException exception) {
	      return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
	   }
	 
	 @ExceptionHandler(value = Throwable.class)
	  public ResponseEntity<Object> handleAllOtherException(Throwable exception) {
	      return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
	   }

}
