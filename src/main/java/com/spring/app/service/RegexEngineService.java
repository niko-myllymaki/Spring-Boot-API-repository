package com.spring.app.service;

import org.springframework.stereotype.Service;

@Service
public class RegexEngineService {
	
	//Compares one character pattern and one character text 
	private static boolean matchOne(char pattern, char text) {
		if(pattern == ' ') return true; //Any text can match if pattern is empty
		if(text == ' ') return false; //There can be no match if text is empty
		if(pattern == '.') return true; //Any inputed text matches the wildcard
		return pattern == text;
	}
		
	private static boolean match(String pattern, String text) {
		char oneText = ' ';
		//Default case - if pattern is empty, any input text is a match
		if(pattern.isEmpty()) {
			return true;
		} else if(pattern.equals("$") && text.equals("")) {
			return true;
		} else if (pattern.length() > 1 && pattern.charAt(1) == '?') {
			return matchQuestion(pattern, text);
		} else if (pattern.length() > 1 && pattern.charAt(1) == '*') {
			return matchStar(pattern, text);
		}  else
			//Must be done because charAt throws an exception if charAt(index) is null
			try {
				oneText = text.charAt(0);
			} catch (Exception e) {
				oneText = ' ';
			}
		return (matchOne(pattern.charAt(0), oneText) && match(pattern.substring(1), text.substring(1)));
	}
		
	private static boolean matchStar(String pattern, String text) {
		char oneText = ' ';
		try {
			oneText = text.charAt(0);
		} catch (Exception e) {
			oneText = ' ';
		}

		return (
				(matchOne(pattern.charAt(0), oneText) && match(pattern, text.substring(1))) ||
				match(pattern.substring(2), text)
				);
	}

	private static boolean matchQuestion(String pattern, String text) {
		char oneText = ' ';
		try {
			oneText = text.charAt(0);
		} catch (Exception e) {
			oneText = ' ';
		}
		if(matchOne(pattern.charAt(0), oneText) && match(pattern.substring(2), text.substring(1))) {
			return true;
		} else {
			return match(pattern.substring(2), text);
		}
	}

	public static boolean search(String pattern, String text) {
		if(pattern.charAt(0) == '^') {
			return match(pattern.substring(1), text);
		} else {
			return match(".*" + pattern, text);
		}
	}
}
