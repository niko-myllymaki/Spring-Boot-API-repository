package com.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.app.dto.UserRecord;
import com.spring.app.entity.UserInfo;
import com.spring.app.exception.UserNotFoundException;
import com.spring.app.repository.CustomUserRepositoryImpl;

@Service
public class DatabaseConnectionService {
	
	@Autowired
	private CustomUserRepositoryImpl userRepository;
	
	public UserRecord getUserById(int id) {
		return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
	}
	
	public List<UserRecord> getAllUsers() {
		return userRepository.selectAllUsers();
	}

	public String deleteUserById(int id) {
		//Check if user exists in the first place. If not -> throw userNotFoundException
		getUserById(id);
		return userRepository.deleteUser(id);
	}
	
	public String addNewUser(UserInfo userInfo) {
		return userRepository.addNewUser(userInfo);
	}
	
//	public String addNewUser(String username, String password) {
//		return userRepository.addNewUser(username, password);
//	}
	
	public String updateUser(int id, String username, String password) {
		getUserById(id);
		return userRepository.updateUser(id, username, password);
	}
	
}
