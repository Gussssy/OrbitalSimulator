package com.gussssy.orbitalsimulator.dataimport;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implmentation of ImportedRow. Holds data from a row from the AstronomicalObjects database representing a single asteroid. 
 */
public class AstronomicalObject implements ImportedRow {
	
	
	
	// The name of the object 
	private String name;
	
	// The diameter of the object (in km)
	private double diameter;
	
	// The aphelion distance (in Au)
	private double aphelion;
	
	
	// The perihelion distance (in Au)
	private double perihelion;
	
	
	// The orbital eccentricity
	private double eccentricity;
	

	
	
	/**
	 * Creates a new AstronomicalObject object. 
	 * 
	 * @param name of the object
	 * @param diameter of the object (in km)
	 * @param aphelion the aphelion distance of the object (in Au)
	 * @param perihelion the perihelion distance of the object (in Au)
	 * @param eccentricity the objects orbital eccentricity
	 */
	public AstronomicalObject(String name, double diameter, double aphelion, double perihelion, double eccentricity){
		
		this.name = name;
		this.diameter = diameter;
		this.aphelion = aphelion;
		this.perihelion = perihelion;
		this.eccentricity = eccentricity;

	}
	
	
	
	/**
	 * Returns a String describing the AstronomicalObject
	 * 
	 * @return String describing the AstronomicalObject 
	 */
	public String toString(){
		
		return "AstronomicalObject.toString() \n\tName: " + name +"\n\tDiameter: " + diameter + "\n\taphelion: " + aphelion
				+ "\n\tperihelion: " + perihelion + "\n\teccentricity: " + eccentricity;
	}


	
	/**
	 * Returns the name of the Astronomical object
	 * 
	 *  @return the name of the Astronomical Object
	 */
	public String getName() {
		return name;
	}
	
	
	
	/**
	 * @return the diameter
	 */
	public double getDiameter() {
		return diameter;
	}

	

	/**
	 * @return the aphelion
	 */
	public double getAphelion() {
		return aphelion;
	}


	
	/**
	 * @return the perihelion
	 */
	public double getPerihelion() {
		return perihelion;
	}


	
	/**
	 * @return the eccentricity
	 */
	public double getEccentricity() {
		return eccentricity;
	}


	










}
