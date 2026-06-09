package com.spring.app.dto;

import java.util.HashSet;
import java.util.Set;

import com.spring.app.entity.Role;

public class UserDTO {
	private Long id;
	private String username;
    private Set<RoleDTO> roles = new HashSet<>();
    
    public UserDTO() {}
    
    public UserDTO(Long id, String username, Set<RoleDTO> roles) {
    	this.id = id;
    	this.username = username;
    	this.roles = roles;
    }

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}

}
