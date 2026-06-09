package com.spring.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.app.dto.UserDTO;
import com.spring.app.entity.ERole;
import com.spring.app.entity.Permission;
import com.spring.app.entity.RefreshToken;
import com.spring.app.entity.Role;
import com.spring.app.entity.User;
import com.spring.app.exception.RefreshTokenException;
import com.spring.app.exception.UserAlreadyExistsException;
import com.spring.app.mapper.UserMapper;
import com.spring.app.repository.PermissionRepository;
import com.spring.app.repository.RefreshTokenRepository;
import com.spring.app.repository.RoleRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.JwtUtil;

@Service
public class UserService {
	@Autowired
	private UserMapper mapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtil jwtUtils;
	@Autowired
	RefreshTokenService refreshTokenService;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;

    public List<UserDTO> fetchUserList(int value) {
    	//We can limit the amount of users returned by Pageable using .ofSize(n) method, where n is the amount of users returned.
        return userRepository.findAll(Pageable.ofSize(value))
        		.stream()
        		.map(mapper::toDto)
        		.collect(Collectors.toList());
    }
    
    
    //Login
    public User saveUserToDB(User user) {
		 if (userRepository.existsByUsername(user.getUsername())) {
			 throw new UserAlreadyExistsException("Username " + user.getUsername() + " already exists");
		 }
		 
		 Set<Permission> permissionsSet = new HashSet<>();
		 Set<Role> roleSet = new HashSet<>();
		 
		 User newUser = new User(
				 null,
				 user.getUsername(),
				 //TODO: Can we use our custom encoder instead of what is provided?
				 encoder.encode(user.getPassword()),
				 roleSet,
				 permissionsSet
				 );
		 
		 for(Role role : user.getRoles()) {
			 ERole roleName = role.getName();
			 newUser.getRoles().add(roleRepository.findByName(roleName)
					 .orElseGet(() -> new Role(null, roleName)));
		 }
		 
		for(Permission permission : user.getPermissions()) {
			 String permissionString = permission.getName();
			 newUser.getPermissions().add(permissionRepository.findByName(permissionString)
					 .orElseGet(() -> new Permission(null, permissionString)));
		}
		
		return userRepository.save(newUser);
    }
    
    //Signin
    public Map<String, String> generateUserTokens(User user) {
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
    
    public ResponseEntity<?> refreshUserToken(Map<String, String> payload) {
    	String requestToken = payload.get("refreshToken");
    	return refreshTokenRepository.findByToken(requestToken)
    			.map(token -> {
    				if (refreshTokenService.isTokenExpired(token)) {
    					refreshTokenRepository.delete(token);
    					throw new RefreshTokenException("Refresh token expired. Please login again.");
    				}
    				User user = token.getUser();
    				UserDetailsImpl userDetails = new UserDetailsImpl();
    				userDetails.setUser(user);
    				String newJwt = jwtUtils.generateToken(userDetails);

    				return ResponseEntity.ok(Map.of("token", newJwt));
    			})
    			.orElseThrow(() -> new RefreshTokenException("Invalid refresh token."));

    }
    
    public ResponseEntity<?> logoutUserToken(Map<String, String> payload) {
		 String requestToken = payload.get("refreshToken");

		 if (requestToken == null || requestToken.isBlank()) {
			 throw new RefreshTokenException("Refresh token is required.");

		 }
		 
		 return refreshTokenRepository.findByToken(requestToken)
				 .map(token -> {
					 refreshTokenRepository.delete(token);
					 return ResponseEntity.ok("Logged out successfully.");
				 })
				 .orElseThrow(() -> new RefreshTokenException("Invalid refresh token."));
    }
    
    public User findUserByUsername(String username) {
    	return userRepository.findByUsername(username);
    }
    
    public List<String> getRoleFromJwtToken(String authToken) {
    	return jwtUtils.getRoleFromToken(authToken.substring(6).trim());
    }
}
