package com.gussssy.orbitalsimulator.dataimport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class DatabaseConnection {


	String url;
	String username;
	String password;




	public DatabaseConnection(String url, String username, String password){

		this.url = url;
		this.username = username;
		this.password = password;


	}


	/**
	 * Test whether or not a connection can be established to the database. 
	 */
	public boolean testConnectionToDatabase(){

		System.out.println("connecting to database...");

		
		
		// Attempt to establish a connection to the database
		try(Connection connection = DriverManager.getConnection(url, username, password)){

			// Connection successful
			
			System.out.println("Connected to database!");
			return true;


		}catch(SQLException e){
			
			// Connection Unsuccessful

			System.out.println("Cannot connect to database. \n" + e.toString());
			return false;

		}


	}




	/**
	 * Executes a query and creates an ImportedRow object for each row returned from the query.
	 * 
	 * @param query The query to be executed
	 * @param factory Facotry object that will control the kind of object created for each row.
	 * @return results array list containing the imported row objects created by the factory
	 * 
	 */
	public ArrayList<ImportedRow> queryDatabase(String query, ImportedRowFactory factory){

		System.out.println("Connecting to database...");
		
		// create array list to hold imported data to be returned.
		ArrayList<ImportedRow> results = new ArrayList<ImportedRow>();


		try(Connection connection = DriverManager.getConnection(url, username, password)){

			System.out.println("Connected to database!\n\n\tAttempting to Query database with:\n\n\t " + query);

			// compile query...? 
			try(Statement statement = connection.createStatement()){

				// Execute the query
				ResultSet resultSet = statement.executeQuery(query);
				
				int count = 0;
				
				// Create an ImportedRow object for each row retrieved. 
				while(resultSet.next()){
					
					count++;
					
					System.out.println("Creating a new ImportedRowObject. Count: " + count);
					results.add(factory.createImportedRow(resultSet));
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				// Finished importing data. Return results. 
				System.out.println("\n Query successful");
				return results;

				
				



			}catch(SQLException e){
				
				// Connection successful, query unsuccessful
				System.out.println("\n Query unsuccessful. Connection was successful, something is wrong with the query.");
				return null;	
			}

		}catch(SQLException e){
			
			// Connection unsuccesful
			System.out.println("Cannot connect to database. \n" + e.toString());
			return null;
		}

		

	}






}
