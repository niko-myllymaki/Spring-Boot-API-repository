package com.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.entity.User;
import com.spring.app.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

    public List<User> fetchUserList() {
        return userRepository.findAll();
    }
}
