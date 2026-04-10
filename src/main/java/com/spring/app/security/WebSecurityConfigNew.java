package com.spring.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.app.service.CustomUserDetailsService;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfigNew {
	
    @Autowired
    CustomUserDetailsService userDetailsService;
    
    @Autowired
    private AuthEntryPointJwtNew unauthorizedHandler;

    @Bean
    public AuthTokenFilterNew authenticationJwtTokenFilter() {
        return new AuthTokenFilterNew();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Updated configuration for Spring Security 6.x
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .cors(cors -> cors.disable()) // Disable CORS (or configure if needed)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                        		//If we don't specify an uri path here, then it needs authorization with a token, otherwise it's open to all.
                                .requestMatchers("/api/auth/**", "/api/test/all").permitAll() // Use 'requestMatchers' instead of 'antMatchers'
                                //The uri path must be very specific for some reason, otherwise it will not work. /api/role/admin/** doesn't work either.
                                .requestMatchers("/api/role/user/test").hasRole("USER")
                                .requestMatchers("/api/role/admin/test").hasRole("ADMIN")
                                //.requestMatchers("/api/auth/users").hasAuthority("READ_USER")
                                .anyRequest().authenticated()
                );
        // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
   
}
