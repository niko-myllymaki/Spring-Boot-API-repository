package com.spring.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.app.security.JwtUtil;
import com.spring.app.service.RefreshTokenService;
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
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	//This specifies if its okay to allow api calls from this source. This can be done globally as well.
	//@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
    	return new ResponseEntity<>("Public access...", HttpStatus.OK);
    }
    
    @GetMapping("/all/refresh-token")
    public ResponseEntity<?> refreshTokenByUserId(@RequestParam() Long userId) {
    	return refreshTokenService.getRefreshTokenByUserId(userId);
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
    //Default of 5 users returned or value given in uri
    @GetMapping("/users")
    public ResponseEntity<?> readUsers(@RequestParam(defaultValue = "5") int value) {
    	return new ResponseEntity<>(userService.fetchUserList(value), HttpStatus.OK);
    }
       
}
