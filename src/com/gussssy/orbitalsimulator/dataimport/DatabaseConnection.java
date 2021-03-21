package com.gussssy.orbitalsimulator.dataimport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {


	String url;
	String username;
	String password;




	public DatabaseConnection(String url, String username, String password){

		this.url = url;
		this.username = username;
		this.password = password;


	}


	public boolean testConnectionToDatabase(){

		System.out.println("connecting to database...");


		try(Connection connection = DriverManager.getConnection(url, username, password)){

			System.out.println("Connected to database!");
			return true;


		}catch(SQLException e){

			System.out.println("Cannot connect to database. \n" + e.toString());
			return false;

			//throw new IllegalStateException("Cannot connect to database.", e);

		}


	}





	public ResultSet queryDatabase(String query){

		System.out.println("Connecting to database...");


		try(Connection connection = DriverManager.getConnection(url, username, password)){

			System.out.println("Connected to database!\n\n\tAttempting to Query database with:\n\n\t " + query);

			
			try(Statement statement = connection.createStatement()){

				ResultSet resultSet = statement.executeQuery(query);
				
				System.out.println("\n Query successful");
				return resultSet;

				
				//System.out.println("Number of results returned: " + count);



			}catch(SQLException e){
				
				// Connection successful, query unsuccessful
				System.out.println("\n Query unsuccessful. Connection was successful, something is wrong with the query.");
				return null;
				
			}

		}catch(SQLException e){
			
			// Connection unsuccesful
			System.out.println("Cannot connect to database. \n" + e.toString());
			return null;

			//throw new IllegalStateException("Cannot connect to database.", e);

		}

	}






}
