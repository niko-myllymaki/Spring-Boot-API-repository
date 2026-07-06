package com.spring.app.mapper;

import org.springframework.stereotype.Component;

import com.spring.app.dto.RefreshtokenDTO;
import com.spring.app.entity.RefreshToken;

@Component
public class RefreshtokenMapper {
	
	public RefreshtokenDTO toDto(RefreshToken refreshtoken) {
		RefreshtokenDTO dto = new RefreshtokenDTO(refreshtoken.getToken());
		return dto;
	}
	
	public RefreshToken toEntity(RefreshtokenDTO dto) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(dto.getRefreshtoken());
		return null;
		
	}

}
