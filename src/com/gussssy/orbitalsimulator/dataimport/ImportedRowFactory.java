package com.gussssy.orbitalsimulator.dataimport;

import java.sql.ResultSet;

/**
 * Interface used to create objects from a single row from a database. 
 * 
 * Implementations of this interface will specify the format of the object to be created:
 * 	- values to be contained and the corresponding column name from the database. 
 * 
 * Uses Facotry Method Design Pattern
 */
public interface ImportedRowFactory {
	
	/**
	 * Creates a new ImportedRow object 
	 */
	public ImportedRow createImportedRow(ResultSet resultSet);

}
