package com.spring.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spring.app.security.JwtUtil;

/*
 * Anyone can access this controller
 */

@RestController
@RequestMapping("/api/test")
public class PublicController {
	
	@Autowired
	JwtUtil jwtUtils;
	 
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    
    @GetMapping("/user")
    public String userAccess(@RequestHeader(value="Authorization") String authValue) {
    	//Getting username from token and token from header
    	String token = authValue;
        return "User Content: " + jwtUtils.getUsernameFromToken(token.substring(6).trim());		
    }
    
}
