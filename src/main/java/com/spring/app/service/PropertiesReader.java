package com.spring.app.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	public static Properties readProperties() {
		FileReader reader = null;
		try {
			reader = new FileReader("src\\main\\resources\\config.properties");
			
			Properties dbProperties = new Properties();
			
			dbProperties.load(reader);
			
	
			return dbProperties;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		

	}

}
