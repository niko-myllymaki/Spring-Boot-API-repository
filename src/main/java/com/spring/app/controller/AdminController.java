package com.spring.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.security.JwtUtil;

/*
 * Controller specific for users with the Admin role.
 */

@RestController
@RequestMapping("api/role/admin")
public class AdminController {

	@Autowired
	JwtUtil jwtUtils;
	
    @GetMapping("/test")
    public String sayHello(@RequestHeader(value="Authorization") String authValue) {
    	//Getting username and role from token and token from header
    	String token = authValue;
    	List<String> roles = jwtUtils.getRoleFromToken(token.substring(6).trim());
        return "Hello Admin " + jwtUtils.getUsernameFromToken(token.substring(6).trim()) + " " +roles.toString();	
    }
}
