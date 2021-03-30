package com.gussssy.orbitalsimulator.dataimport;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gussssy.databaseconnectivity.ImportedRow;
import com.gussssy.databaseconnectivity.ImportedRowFactory;

/**
 * Uses the Factory Method pattern to create objects to holding values describing an asteroid with data obtained from the AstronomicalObjects database.
 */
public class AstronomicalObjectFactory implements ImportedRowFactory {

	@Override
	public ImportedRow createImportedRow(ResultSet resultSet) {
		
		
		// Field values the factory will extract and set:
		
		// The name of the asteroid
		String name;
		
		// The diameter of the asteroid (in km)
		double diameter;
		
		// The aphelion of the asteroid (in Au)
		double aphelion;
		
		// The perihelion of the asteroid (in Au)
		double perihelion;
		
		// The orbital eccentricity of the asteroid
		double eccentricity;
		
		// Extract data from the current row of the ResultSet
		try {
			
			// extract name
			name = resultSet.getString("full_name");
			
			// extract diameter
			diameter = resultSet.getDouble("diameter");
			
			// extract apehlion
			aphelion = resultSet.getDouble("ad");
			
			
			// extract perihelion
			perihelion = resultSet.getDouble("q");
			
			
			// extract eccentricity
			eccentricity = resultSet.getDouble("e");
			
			
			// Create the new object and return it
			return new AstronomicalObject(name, diameter, aphelion, perihelion, eccentricity);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return null;
	}

}
