package com.spring.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public List<UserRecord> getAllUsers(@RequestParam(defaultValue = "testvalue") String value) {
		System.out.println(value);
		return DatabaseConnectionService.selectAll();
	}
	
	@GetMapping("/users/{id}")
	public UserRecord getUserById(@PathVariable int id) {
		return DatabaseConnectionService.selectOne(id);
	}
	
	@PostMapping("/users")
	public String addNewUser(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		return DatabaseConnectionService.insertNewUser(username, password);
	}
	
	@PutMapping("/users/{id}")
	public String updateUser(@PathVariable int id,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String password) {
		return DatabaseConnectionService.updateUser(id, username, password);
	}
	
	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable int id) {
		return DatabaseConnectionService.deleteUser(id);
	}

}
