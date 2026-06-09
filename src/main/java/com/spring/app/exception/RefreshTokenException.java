package com.spring.app.exception;

public class RefreshTokenException extends RuntimeException {
	public RefreshTokenException() {}
	
	public RefreshTokenException(String message) {
		super(message);
	}

	public RefreshTokenException(Throwable cause) {
		super(cause);
	}

	public RefreshTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
