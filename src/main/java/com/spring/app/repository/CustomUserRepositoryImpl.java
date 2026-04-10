package com.spring.app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.spring.app.dto.UserRecord;
import com.spring.app.entity.User;
import com.spring.app.service.PasswordHashingService;
import com.spring.app.service.PropertiesReader;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

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
	
	@Override
	public Optional<UserRecord> findUserById(int id) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			UserRecord user = null;
			connection = connectToDB();
			
			//Prepared statements prevent sql injections
			prepStatement = connection.prepareStatement("SELECT idusers, username, role FROM USERS WHERE idusers = ?");
			
			//Set parameterIndex starts at 1 not 0
			prepStatement.setInt(1, id);
			
			//Use execute with unknown statements or when statements produce multiple results
			//otherwise use executeQuery
			resultSet = prepStatement.executeQuery();
			
			while(resultSet.next()) {
				user = new UserRecord(resultSet.getInt("idusers"), resultSet.getString("username"), resultSet.getString("role"));
			}

			return Optional.ofNullable(user);
			} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement, resultSet);
		}
		return null;
	}
	
	@Override
	public List<UserRecord> selectAllUsers() {
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
	
	@Override
	public String deleteUser(int id) {
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
	
	@Override
	public String addNewUser(User user) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		
		byte[] salt = PasswordHashingService.generateSalt();		
		String hashedPassword = PasswordHashingService.hashPassword(user.getPassword(), salt);
		
		try {
			connection = connectToDB();

			//Prepared statements prevents sql injections
			prepStatement = connection.prepareStatement("INSERT INTO USERS (username, passwordHash, passwordSalt, role) VALUES (?, ?, ?, ?)");
			//SetString parameterIndex starts at 1 not 0
			prepStatement.setString(1, user.getUsername());
			prepStatement.setString(2, hashedPassword);
			prepStatement.setBytes(3, salt);
//			prepStatement.setString(4, user.getRole());
			prepStatement.execute();
			
			return "New User Added Successfully: " + user.getUsername();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement);
		}
		return null;
	}
	
//	@Override
//	public String addNewUser(String username, String password) {
//		Connection connection = null;
//		PreparedStatement prepStatement = null;
//		
//		byte[] salt = PasswordHashingService.generateSalt();		
//		String hashedPassword = PasswordHashingService.hashPassword(password, salt);
//		
//		try {
//			connection = connectToDB();
//
//			//Prepared statements prevents sql injections
//			prepStatement = connection.prepareStatement("INSERT INTO USERS (username, passwordHash, passwordSalt) VALUES (?, ?, ?)");
//			//SetString parameterIndex starts at 1 not 0
//			prepStatement.setString(1, username);
//			prepStatement.setString(2, hashedPassword);
//			prepStatement.setBytes(3, salt);
//			prepStatement.execute();
//			
//			return "New user added: " + username;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			releaseResources(connection, prepStatement);
//		}
//		return null;
//	}
	
	@Override
	public String updateUser(int id, String newUsername, String newPassword) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		String newHashedPassword = null;
		byte[] newSalt = null;
		
		if(newPassword != null) {
			newSalt = PasswordHashingService.generateSalt();		
			newHashedPassword = PasswordHashingService.hashPassword(newPassword, newSalt);
		}
		try {
			connection = connectToDB();
			//Prepared statements prevents sql injections
			prepStatement = connection.prepareStatement("UPDATE USERS "
					+ "SET username = COALESCE(?, username), "
					+ "passwordHash = COALESCE(?, passwordHash), "
					+ "passwordSalt = COALESCE(?, passwordSalt) "
					+ "WHERE idusers = ?");
			
			//SetString parameterIndex starts at 1 not 0
			prepStatement.setString(1, newUsername);
			prepStatement.setString(2, newHashedPassword);
			prepStatement.setBytes(3, newSalt);
			prepStatement.setInt(4, id);
			prepStatement.executeUpdate();
			
			return "User updated with ID: " + id;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement);
		}
		return null;
	}
	
	@Override
	public User findByUsername(String username) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet resultSet = null;
		try {
			User user = null;
			connection = connectToDB();
			
			//Prepared statements prevent sql injections
			prepStatement = connection.prepareStatement("SELECT username, passwordHash, role FROM USERS WHERE username = ?");
			
			//Set parameterIndex starts at 1 not 0
			prepStatement.setString(1, username);
			
			//Use execute with unknown statements or when statements produce multiple results
			//otherwise use executeQuery
			resultSet = prepStatement.executeQuery();
			
			while(resultSet.next()) {
//				user = new User(resultSet.getString("username"), resultSet.getString("passwordHash"), resultSet.getString("passwordHash"));
			}

			return user;
			} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			releaseResources(connection, prepStatement, resultSet);
		}
		return null;
	}
	
	//Record class is used as an Data Transfer Object (DTO) because we just need an User class to hold and carry data.
	private static List<UserRecord> resultSetToList(ResultSet resultSet) {
		List<UserRecord> resultList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				UserRecord user = new UserRecord(resultSet.getInt("idusers"), resultSet.getString("username"), resultSet.getString("role"));
				resultList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	//Handling releasing of different types of resources
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
