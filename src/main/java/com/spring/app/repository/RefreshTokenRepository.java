package com.spring.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    
    //@Query("SELECT token FROM refresh_token WHERE user_id = :userId")
    //Optional<RefreshToken> findByUserId(@Param("user_id") long userId);
    
    Optional<RefreshToken> findByUserId(long userId);


}
