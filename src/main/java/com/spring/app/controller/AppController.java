package com.spring.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.dto.UserRecord;
import com.spring.app.service.DatabaseConnectionService;
import com.spring.app.service.RegexEngineService;


@RestController
@RequestMapping("/api")
public class AppController {
	
	
	@GetMapping("/regex")
	public boolean checkPattern(@RequestParam("pattern") String pattern, 
								@RequestParam("text") String text) {
		return RegexEngineService.search(pattern, text);
	}
	
	//TODO: Put a default limit of how many users this returns and possibly additional parameters to select a certain amount of users
	@GetMapping("/users")
	public List<UserRecord> selectAllUsers() {
		return DatabaseConnectionService.selectAll();
	}
	
	@GetMapping("/user")
	public UserRecord selectUser(@RequestParam("id") int userId) {
		return DatabaseConnectionService.selectOne(userId);
	}
	
	@GetMapping("/user/add")
	public String addNewUser(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		return DatabaseConnectionService.insertNewUser(username, password);
	}
	
	@GetMapping("/user/update")
	public String updateUser(@RequestParam("id") int userId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String password) {
		return DatabaseConnectionService.updateUser(userId, username, password);
	}
	
	@GetMapping("/user/delete")
	public String deleteUser(@RequestParam("id") int userId) {
		return DatabaseConnectionService.deleteUser(userId);
	}

}
