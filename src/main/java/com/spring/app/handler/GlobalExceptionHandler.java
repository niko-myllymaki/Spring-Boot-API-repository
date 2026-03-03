package com.spring.app.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.app.exception.ResourceNotFoundException;
import com.spring.app.exception.UserNotFoundException;
import com.spring.app.model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
		ErrorResponse errorResponse = new ErrorResponse(
				404,
				exception.getMessage(),
				"The requested resource was not found.",
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
		ErrorResponse errorResponse = new ErrorResponse(
				404,
				exception.getMessage(),
				"The requested user was not found.",
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
		ErrorResponse errorResponse = new ErrorResponse(
				500,
				exception.getMessage(),
				"An unexpected error occured.",
				LocalDateTime.now()
				);
		
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
