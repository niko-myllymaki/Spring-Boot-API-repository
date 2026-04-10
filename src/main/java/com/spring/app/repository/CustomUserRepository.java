package com.spring.app.repository;

import java.util.List;
import java.util.Optional;

import com.spring.app.dto.UserRecord;
import com.spring.app.entity.User;

public interface CustomUserRepository {
	Optional<UserRecord> findUserById(int id);
	List<UserRecord> selectAllUsers();
	String deleteUser(int id);
	String addNewUser(User user);
	String updateUser(int id, String newUsername, String newPassword);
    User findByUsername(String username);

}
