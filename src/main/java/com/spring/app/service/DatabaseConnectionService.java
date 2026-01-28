package com.spring.app.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.spring.app.dto.UserRecord;

@Service
public class DatabaseConnectionService {
	
	//Constants are read from a config.properties file
	private static final String DATABASE_URL = PropertiesReader.readProperties().getProperty("jdbc-url");
	private static final String DATABASE_USER = PropertiesReader.readProperties().getProperty("db-username");
	private static final String USER_PASSWORD = PropertiesReader.readProperties().getProperty("db-password");
	
	private static Connection connectToDB() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, USER_PASSWORD);
			System.out.println("Connected to the database.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
	
	public static String updateUser(int id, String username, String password) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = connectToDB();
			//Prepared statements prevents sql injections
			prepStatement = connection.prepareStatement("UPDATE USERS "
					+ "SET username = COALESCE(?, username), "
					+ "password = COALESCE(?, password) "
					+ "WHERE idusers = ?");
			//SetString parameterIndex starts at 1 not 0
			prepStatement.setString(1, username);
			prepStatement.setString(2, password);
			prepStatement.setInt(3, id);
			prepStatement.executeUpdate();
			
			return "User updated";
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement);
		}
		
		return null;
	}
	
	public static String insertNewUser(String username, String password) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = connectToDB();

			//Prepared statements prevents sql injections
			prepStatement = connection.prepareStatement("INSERT INTO USERS (username, password) VALUES (?, ?)");
			//SetString parameterIndex starts at 1 not 0
			prepStatement.setString(1, username);
			prepStatement.setString(2, password);
			prepStatement.execute();
			
			return "New User added";
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement);
		}
		return null;
	}
	
	public static String deleteUser(int id) {
		Connection connection = null;
		PreparedStatement prepStatement = null;

		try {
			connection = connectToDB();
			//Prepared statements prevents sql injections
			prepStatement = connection.prepareStatement("DELETE FROM USERS WHERE idusers = ?");
			//SetString parameterIndex starts at 1 not 0
			prepStatement.setInt(1, id);

			prepStatement.execute();
			
			return "Deleted user of id: " + id;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement);
		}
		return null;
	}
	
	public static UserRecord selectOne(int id) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			UserRecord user = null;
			connection = connectToDB();
			
			//Prepared statements prevent sql injections
			prepStatement = connection.prepareStatement("SELECT * FROM USERS WHERE idusers = ?");
			
			//Set parameterIndex starts at 1 not 0
			prepStatement.setInt(1, id);
			
			//Use execute with unknown statements or when statements produce multiple results
			//otherwise use executeQuery
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()) {
				user = new UserRecord(resultSet.getInt("idusers"), resultSet.getString("username"), resultSet.getString("password"));
			}
			System.out.println("User selected: " + user);

			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement, resultSet);
		}
		return null;
	}
	
	
	public static List<UserRecord> selectAll() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = connectToDB();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM USERS");

			List<UserRecord> allUsers = resultSetToList(resultSet);
			System.out.println("Selecting all users");

			return allUsers;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, statement, resultSet);
		}
		return null;
	}
	
	//Record class is used as an Data Transfer Object (DTO) because we just need an User class to hold and carry data.
	private static List<UserRecord> resultSetToList(ResultSet resultSet) {
		List<UserRecord> resultList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				UserRecord user = new UserRecord(resultSet.getInt("idusers"), resultSet.getString("username"), resultSet.getString("password"));
				resultList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	//Handling closing of different types of objects
	private static void releaseResources(Object... args) {
		System.out.println("Releasing resources...");
		for(Object obj: args) {
			if(obj instanceof Connection) {
				try { ((Connection)obj).close();} catch (Exception e) {}
			} 
			if(obj instanceof Statement) {
				try { ((Statement)obj).close();} catch (Exception e) {}
			} 
			if(obj instanceof ResultSet) {
				try { ((ResultSet)obj).close();} catch (Exception e) {}
			} 
		}
	}	
}
