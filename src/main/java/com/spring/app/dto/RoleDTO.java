package com.spring.app.dto;

import com.spring.app.entity.ERole;

public class RoleDTO {
	
	private ERole roleName;
	
	public RoleDTO() {}
	
	public RoleDTO(ERole roleName) {
		this.roleName = roleName;
	}

	public ERole getRoleName() {
		return roleName;
	}

	public void setRoleName(ERole roleName) {
		this.roleName = roleName;
	}

}
