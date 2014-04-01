package com.amazonaws.services.kinesis.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class LogAnalyzer {
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} 
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private HashSet<String> users = null;
	private Connection conn = null;
	private String query;
	private String accessPath;
	
	public LogAnalyzer() throws SQLException {
		accessPath = System.getProperty("kinesisapp.accesspath");
		query = System.getProperty("kinesisapp.query");
		
		conn = DriverManager.getConnection(
				System.getProperty("kinesisapp.jdbcurl"), 
				System.getProperty("kinesisapp.dbuser"), 
				System.getProperty("kinesisapp.dbpassword"));
		conn.setAutoCommit(true);
	}
	
	public HashSet<String> getUsers() throws SQLException {
		if (users == null) {
			users = new HashSet<String>();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accessPath);
			ResultSet rset = stmt.executeQuery();
			while(rset.next()) users.add(rset.getString(1));
			conn.close();
		}
		
		return users;		
	}

}
