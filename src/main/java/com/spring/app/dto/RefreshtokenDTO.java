package com.spring.app.dto;

public class RefreshtokenDTO {
	private String refreshToken;
	
	public RefreshtokenDTO() {}
	
	public RefreshtokenDTO(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getRefreshtoken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	

}
