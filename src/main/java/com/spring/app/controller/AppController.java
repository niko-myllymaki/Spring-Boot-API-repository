package com.spring.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.service.RegexEngineService;


@RestController
@RequestMapping("/api")
public class AppController {
	
	
	@GetMapping("/regex")
	public boolean checkPattern(@RequestParam("pattern") String pattern, 
								@RequestParam("text") String text) {
		return RegexEngineService.search(pattern, text);
	}

}
