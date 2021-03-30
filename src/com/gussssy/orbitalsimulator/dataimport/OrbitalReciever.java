package com.gussssy.orbitalsimulator.dataimport;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.databaseconnectivity.DatabaseConnection;
import com.gussssy.databaseconnectivity.ImportedRow;
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
		
		
		
		// RETRIEVE DATA FROM DATABASE
		
		// Query to be sent to the database. 
		String query = "SELECT * FROM astronomical_objects.Asteroids WHERE diameter >= " + minDiameter;
		
		
		// Query the database to return a list of ImportedRow objects. 
		// The contents of the list is actually of type ImportedAstronomicalObject which is created by AstronomicalObjectFactory
		ArrayList<ImportedRow> importedRows = database.queryDatabase(query, new AstronomicalObjectFactory());
		
		
		// print out the number of rows selected from the query: 
		System.out.println("Number of ateroids found with a diameter exceeding " + minDiameter + ": " + importedRows.size());
		
		
		
		
		
		// CAST RETRIEVED DATA TO CORRECT TYPE
		
		// this will hold the asteroids obtained from the database, once they have been cast to the correct type.
		ArrayList<AstronomicalObject> importedAsteroids = new ArrayList<AstronomicalObject>();
		
		
		// loop through the importedrows and cast to asteroid
		for(ImportedRow ir : importedRows ){
			
			importedAsteroids.add((AstronomicalObject)ir);
		}
		
		
		
		
		
		
		// PROCESS RETRIEVED DATA 
		
		// Process Imported Asteroid data to make simulation objects (Obs) so they can be added to the simulation.
		
		// create list to hold newly created asteroid Ob's
		ArrayList<Ob> asteroids = new ArrayList<Ob>();
		
		
		// loops through returned asteroids and create Ob objects that can be added to the simulation.
		//	- assign random values for position, speed, size and color.
		//  - as mass is not given by the database, a small mass is given to all asteroids
		for(AstronomicalObject iao : importedAsteroids){
			
			
			// print out imported asteroid detail to console
			System.out.println(iao.toString());
			
			
			// the mass of the asteroid. the database does not provide this so will set to a small value
			double mass = 0.001;
			
			
			
			// below I have set ranges aimed at producing interesting simulations. 
				// in the future, asteroid orbits should reflect actual orbital path to some degree.. but in 2D
			
			// initial x location in mKm
			double x = ThreadLocalRandom.current().nextDouble(400, 800);
			
			// initial y location in mkm
			double y = ThreadLocalRandom.current().nextDouble(0, 600);
			
			// initial x velocity in km/s
			double vx = ThreadLocalRandom.current().nextDouble(9.68, 20);
			
			// initial y velocity in km/s
			double vy = ThreadLocalRandom.current().nextDouble(-2, 2);
			
			
			// generate a random color
			int red = ThreadLocalRandom.current().nextInt(0,255);
			int green = ThreadLocalRandom.current().nextInt(0,255);
			int blue = ThreadLocalRandom.current().nextInt(0,255);
			Color color = new Color(red,green,blue);
			
			// size in the display, random value between 1-10
			int size = ThreadLocalRandom.current().nextInt(1,10);
			
			// make new Ob which can be added tothe simulation
			asteroids.add(new Ob(iao.getName(), mass, x, y, vx, vy, color, size));
			
		}
		
		
		// return the asteroid Ob's list
		return asteroids;
		
	}
	
	

}
