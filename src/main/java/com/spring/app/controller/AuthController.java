package com.spring.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.entity.User;
import com.spring.app.repository.PermissionRepository;
import com.spring.app.repository.RefreshTokenRepository;
import com.spring.app.repository.RoleRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.JwtUtil;
import com.spring.app.service.RefreshTokenService;
import com.spring.app.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	 @Autowired
	 AuthenticationManager authenticationManager;
	 @Autowired
	 UserRepository userRepository;
	 @Autowired
	 PasswordEncoder encoder;
	 @Autowired
	 JwtUtil jwtUtils;
	 @Autowired
	 RefreshTokenRepository refreshTokenRepository;
	 @Autowired
	 RefreshTokenService refreshTokenService;
	 @Autowired
	 PermissionRepository permissionRepository;
	 @Autowired
	 RoleRepository roleRepository;
	 @Autowired
	 UserService userService;
	 
	 //@CrossOrigin(origins = "http://localhost:4200")
	 @PostMapping("/signin")
	 public ResponseEntity<?> authenticateUser(@RequestBody User user) {
		 return new ResponseEntity<>(userService.generateUserTokens(user), HttpStatus.OK);
	 }
	 
	 //@CrossOrigin(origins = "http://localhost:4200")
	 @PostMapping("/signup")
	 public ResponseEntity<?> registerUser(@RequestBody User user) {
		 userService.saveUserToDB(user);
		 return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
	 }
	 
	 //Refreshing token
	 @PostMapping("/refresh")
	 public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
		 return userService.refreshUserToken(payload);
	 }
	 
	 //Deleting token
	 @PostMapping("/logout")
	 public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
		 return userService.logoutUserToken(payload);
	 }
	 
	 //@CrossOrigin(origins = "http://localhost:4200")
	 @GetMapping("/role")
	 public ResponseEntity<?> getRoleFromJwt(@RequestHeader(value="Authorization") String authToken) {
		 return new ResponseEntity<>(userService.getRoleFromJwtToken(authToken), HttpStatus.OK);
	 }
	 
	
}
