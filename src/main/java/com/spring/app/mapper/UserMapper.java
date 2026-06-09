package com.spring.app.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.app.dto.RoleDTO;
import com.spring.app.dto.UserDTO;
import com.spring.app.entity.User;

@Component
public class UserMapper {
	
	RoleDTO roleDto;
	@Autowired
	RoleMapper mapper;
	
	public UserDTO toDto(User user) {
		UserDTO dto = new UserDTO(user.getId(), user.getUsername(), mapper.toRoleSetDto(user.getRoles()));
		return dto;
	}
	
	public User toEntity(UserDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setUsername(dto.getUsername());
		user.setRoles(mapper.toSetEntity(dto.getRoles()));
		return user;
		
	}

}
