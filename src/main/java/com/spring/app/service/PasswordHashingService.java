package com.spring.app.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHashingService {
	
	public static String hashPassword(String password, byte[] salt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			
			byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));
			
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
 
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            
            // Ensure the hash is padded to 128 characters
            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }
           	
            return hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
}
