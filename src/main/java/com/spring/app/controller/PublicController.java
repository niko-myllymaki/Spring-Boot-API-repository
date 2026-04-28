package com.spring.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.app.entity.User;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.JwtUtil;
import com.spring.app.service.UserService;

/*
 * Controller for anyone to access.
 */

@RestController
@RequestMapping("/api/test")
public class PublicController {
	
	@Autowired
	JwtUtil jwtUtils;
	
	@Autowired
	UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
    	return new ResponseEntity<>("Public access.", HttpStatus.OK);
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> userAccess(@RequestHeader(value="Authorization") String authValue) {
    	//Getting username from token and token from header
    	String token = authValue;
    	List<String> permissions = jwtUtils.getPermissionFromToken(token.substring(6).trim());
    	String UserInfo = "User Content: " + jwtUtils.getUsernameFromToken(token.substring(6).trim()) + " " + permissions.toString();
        return new ResponseEntity<>(UserInfo, HttpStatus.OK);

    }
    
    //Needs READ_USER permission to access and get all data of all users.
    @GetMapping("/users")
    public ResponseEntity<?> readUsers() {
    	return new ResponseEntity<>(userService.fetchUserList(), HttpStatus.OK);
    }
       
}
