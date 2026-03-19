package com.spring.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.app.entity.UserInfo;
import com.spring.app.repository.CustomUserRepository;

@Service
public class UserInfoService implements UserDetailsService {

    private final CustomUserRepository repository;
    //private final PasswordEncoder encoder;
	
    @Autowired
    public UserInfoService(CustomUserRepository repository) {
        this.repository = repository;
        //this.encoder = encoder;
    }
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = repository.findByUsername(username);
        
        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        // Convert UserInfo to UserDetails (UserInfoDetails)
        UserInfo user = userInfo.get();
        return new User(user.getUsername(), user.getPassword(), null);	
	}

}
