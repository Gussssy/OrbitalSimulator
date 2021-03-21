package com.gussssy.orbitalsimulator.dataimport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gussssy.orbitalsimulator.Ob;

/**
 * Class used to recieve and process astronomical object data obtained from a database.
 * 
 * Data recieved will be used to create Objects to add to the simulation.
 */
public class OrbitalReciever{
	
	static String url = "jdbc:mysql://localhost:3306/astronomical_objects";
	static String username = "java";
	static String password = "noog";
	static DatabaseConnection database = new DatabaseConnection(url, username, password);
	
	
	
	
	
	/**
	 * Returns a list of asteroid Obs made by accessing the AstronomicalObjects database and slecting for a  specified minimum diameter.   
	 * 
	 * @param minDiamater the minimum diameter in kilometres of asteroids to be included in the returned objects
	 * @returns list containing Obs representing all the ateroids loaded from the database that have a diameter exceeding the specified minimum.  Obs can now be loaded into the silumator. 
	 */
	public static ArrayList<Ob> getAteroidsWithDiamterExceeding(double minDiameter){
		
		
		// test connection to database
		
		database.testConnectionToDatabase();
		
		String query = "SELECT * FROM astronomical_objects.Asteroids WHERE diameter >= " + minDiameter;
		
		ResultSet data = database.queryDatabase(query);
		
		try {
			while(data.next()){
				System.out.println("DATA LINE READ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		return new ArrayList<Ob>();
		
	}
	
	

}
