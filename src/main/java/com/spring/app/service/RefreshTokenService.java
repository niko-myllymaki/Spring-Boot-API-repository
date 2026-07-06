package com.spring.app.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.app.entity.RefreshToken;
import com.spring.app.exception.RefreshTokenException;
import com.spring.app.mapper.RefreshtokenMapper;
import com.spring.app.repository.RefreshTokenRepository;
import com.spring.app.repository.UserRepository;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Autowired
    private RefreshtokenMapper mapper;

    public RefreshTokenService(RefreshTokenRepository repo, UserRepository userRepo) {
        this.refreshTokenRepository = repo;
        this.userRepository = userRepo;
    }

    public RefreshToken createRefreshToken(Long userId) {
        var token = new RefreshToken();
        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
    
    //method to return a refreshToken based on userid or username? because both of them are unique.    
    public ResponseEntity<?> getRefreshTokenByUserId(Long userId) {
    	return refreshTokenRepository.findByUserId(userId)
    			.map(token -> {
    				return ResponseEntity.ok(mapper.toDto(token));

    			})
    			.orElseThrow(() -> new RefreshTokenException("Refresh token not found with given user id."));
    }
    

}
