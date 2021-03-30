package com.gussssy.databaseconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

/**
 * Used to extract data from a database and package this data into RowImport objects. 
 */
public class DatabaseConnection {

	
	// Variables used to access a database
	private String url;
	private String username;
	private String password;



	/**
	 * Creates a new DatabaseConnection object.
	 * 
	 *  @param url of the database
	 *  @param username
	 *  @param password
	 */
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
	 * Uses the Factory Method Pattern so the returned object type is dependent on the type of ImportedRowFactory used. 
	 * 
	 * @param query The query to be executed
	 * @param factory Facotry object that will control the kind of object created for each row.
	 * @return results array list containing the imported row objects created by the factory
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
					
					// Use the factory to create an ImportedRow object from the data in the current row. 
					//  - exactly what values are extracted depends on the type of factory object passed in
					results.add(factory.createImportedRow(resultSet));
					System.out.println("Created a new ImportedRowObject. Count: " + count);
					
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
