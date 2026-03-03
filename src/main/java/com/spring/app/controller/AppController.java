package com.spring.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.dto.UserRecord;
import com.spring.app.service.DatabaseConnectionService;
import com.spring.app.service.RegexEngineService;

@RestController
@RequestMapping("/api")
public class AppController {
	
	@Autowired
	private DatabaseConnectionService dbConnectionService;
	
	@GetMapping("/regex")
	public boolean checkPattern(@RequestParam("pattern") String pattern, 
								@RequestParam("text") String text) {
		return RegexEngineService.search(pattern, text);
	}
	
	//TODO: Put a default limit of how many users this returns and possibly additional parameters to select a certain amount of users
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "testvalue") String value) {
		System.out.println(value);
		List<UserRecord> usersList = dbConnectionService.getAllUsers();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable int id) {
		UserRecord user = dbConnectionService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> addNewUser(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		
		String outcome = dbConnectionService.addNewUser(username, password);
        return new ResponseEntity<>(outcome, HttpStatus.CREATED); 
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable int id,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String password) {
		
		String outcome = dbConnectionService.updateUser(id, username, password);
        return new ResponseEntity<>(outcome, HttpStatus.CREATED); 
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		String outcome = dbConnectionService.deleteUserById(id);
        return new ResponseEntity<>(outcome, HttpStatus.OK); 
	}

}
