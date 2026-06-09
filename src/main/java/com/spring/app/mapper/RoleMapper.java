package com.spring.app.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.spring.app.dto.RoleDTO;
import com.spring.app.entity.Role;

@Component
public class RoleMapper {
	
	public RoleDTO toDto(Role role) {
		RoleDTO dto = new RoleDTO(role.getName());
		return dto;
	}
	
	public Set<RoleDTO> toRoleSetDto(Set<Role> roles) {
		return roles.stream().map(this::toDto).collect(Collectors.toSet());
	}
	
	public Role toEntity(RoleDTO dto) {
		Role role = new Role();
		role.setName(dto.getRoleName());
		return role;
	}
	
	//TODO: Test this out?
	public Set<Role> toSetEntity(Set<RoleDTO> rolesDto) {
		Set<Role> roles = rolesDto.stream().map(this::toEntity)
				.collect(Collectors.toSet());
		return roles;
	}
	
//	Set<Role> roles = rolesDto.stream().map(role -> new Role(null, role.getRoleName()))
//			.collect(Collectors.toSet());

}
