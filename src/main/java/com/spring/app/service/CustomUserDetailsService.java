package com.spring.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.app.entity.Permission;
import com.spring.app.entity.Role;
import com.spring.app.entity.User;
import com.spring.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
	
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : userFromDB.getRoles()) {
        	System.out.println("here: " + role.getName());
        	authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (Permission permission : role.getPermissions()) {
            	authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
            
        }
           
        UserDetailsImpl customUserDetail = new UserDetailsImpl();
        customUserDetail.setUser(userFromDB);
        customUserDetail.setAuthorities(authorities);
        
        return customUserDetail;
    }
   
}
