package com.spring.app.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spring.app.entity.Permission;
import com.spring.app.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;
    // Initializes the key after the class is instantiated and the jwtSecret is injected, 
    // preventing the repeated creation of the key and enhancing performance
    
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    
    // Generate JWT token
    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Extract roles
        List<String> roles = userDetails.getUser().getRoles().stream()
                .map(role -> role.getName().name()) // Assuming Enum ERole
                .collect(Collectors.toList());
        claims.put("roles", roles);

        // Extract permissions
        Set<String> permissions = userDetails.getUser().getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
        claims.put("permissions", permissions);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    // Get username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    } 
    
    // Get role from JWT token to List
    public List<String> getRoleFromToken(String token) {
    	List<String> claimRoles = extractAllClaims(token).get("roles", List.class);
        return claimRoles;
    } 
    
    // Get role from JWT token to List
    public List<String> getPermissionFromToken(String token) {
    	List<String> claimPermissions = extractAllClaims(token).get("permissions", List.class);
        return claimPermissions;
    } 
    
    // Validate JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey((jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
}
