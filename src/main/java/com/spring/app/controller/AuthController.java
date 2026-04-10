package com.spring.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.entity.RefreshToken;
import com.spring.app.entity.User;
import com.spring.app.repository.RefreshTokenRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.JwtUtil;
import com.spring.app.service.RefreshTokenService;
import com.spring.app.service.UserDetailsImpl;

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
	 
	 @PostMapping("/signin")
	 public Map<String, String> authenticateUser(@RequestBody User user) {
		 Authentication authentication = authenticationManager.authenticate(
				 new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
				 );
		 
		 SecurityContextHolder.getContext().setAuthentication(authentication);
		 UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		 String accessToken = jwtUtils.generateToken(userDetails);

		 User dbUser = userRepository.findByUsername(userDetails.getUsername());
		 RefreshToken refreshToken = refreshTokenService.createRefreshToken(dbUser.getId());

		 return Map.of(
				 "accessToken", accessToken,
				 "refreshToken", refreshToken.getToken()
				 );
	 }
	 
	 //This now works
	 @PostMapping("/signup")
	 public ResponseEntity<?> registerUser(@RequestBody User user) {
		 if (userRepository.existsByUsername(user.getUsername())) {
			 return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.CONFLICT);
		 }
		 User newUser = new User(
				 null,
				 user.getUsername(),
				 //TODO: Can we use our custom encoder instead of what is provided?
				 encoder.encode(user.getPassword()),
				 user.getRoles()
				 );
		 userRepository.save(newUser);
		 return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
	 }
	 
	 //For refresh token
	 @PostMapping("/refresh")
	 public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
		 String requestToken = payload.get("refreshToken");
		 return refreshTokenRepository.findByToken(requestToken)
				 .map(token -> {
					 if (refreshTokenService.isTokenExpired(token)) {
						 refreshTokenRepository.delete(token);
						 return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
					 }
					 User user = token.getUser();
					 UserDetailsImpl userDetails = new UserDetailsImpl();
					 userDetails.setUser(user);
					 String newJwt = jwtUtils.generateToken(userDetails);
					 
					 return ResponseEntity.ok(Map.of("token", newJwt));
				 })
				 .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
	 }
	 
	 //For refresh token
	 @PostMapping("/logout")
	 public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
		 String requestToken = payload.get("refreshToken");

		 if (requestToken == null || requestToken.isBlank()) {
			 return ResponseEntity.badRequest().body("Refresh token is required.");
		 }

		 return refreshTokenRepository.findByToken(requestToken)
				 .map(token -> {
					 refreshTokenRepository.delete(token);
					 return ResponseEntity.ok("Logged out successfully.");
				 })
				 .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
	 }

}
