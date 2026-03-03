package com.spring.app.model;

import java.time.LocalDateTime;

public class ErrorResponse {
	private int status;
	private String message;
	private String details;
	private LocalDateTime timestamp;
	
	public ErrorResponse() {
		
	}
	
	public ErrorResponse(int status, String message, String details, LocalDateTime timestamp) {
		this.status = status;
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}
	
	//Getters
	public int getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDetails() {
		return details;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	//Setters
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
