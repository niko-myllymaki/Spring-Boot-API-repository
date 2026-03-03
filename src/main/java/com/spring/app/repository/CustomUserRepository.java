package com.spring.app.repository;

import java.util.List;
import java.util.Optional;

import com.spring.app.dto.UserRecord;

public interface CustomUserRepository {
	Optional<UserRecord> findUserById(int id);
	List<UserRecord> selectAllUsers();
	String deleteUser(int id);
	String addNewUser(String username, String password);
	String updateUser(int id, String newUsername, String newPassword);

}
