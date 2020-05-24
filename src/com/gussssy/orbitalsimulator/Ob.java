package com.gussssy.orbitalsimulator;

import java.awt.Color;
import java.io.Serializable;

/**
* Ob defines a single Object in space such as a planet, moon, star etc
*/
public class Ob implements Serializable{

	/**The name of the object.*/
	public String name;	

	/**The Mass of the object in Earth Masses. A mass of 1 corresponds to 5.972x10^25 kg */
	public double mass; 

	// Gravitational force from the parent body in newtons
	/**The gravitational force in Newtons, applied to this body by the last object it was compared with*/
	public double gForce;
	
	
	// MODEL COORDS
	/**The x value of the objects loaction within the model space*/
	public double x;
	/**The y value of the objects loaction within the model space*/		
	public double y;

	
	//Display coords
	/**The x coordinate of the objects loaction in the display space*/
	public double dispX;
	/**The y coordinate of the objects loaction in the display space*/
	public double dispY;

	//Velocity, given in km/s
	/**The x component of the objects velocity in km per second*/
	public double vx;
	/**The y component of the objects velocity in km per second*/
	public double vy;
	
	
	/**The colour the object will display*/
	public Color color;
	
	/**The size of the object in the display*/
	public double size;
	
	/** Serial Version ID. **/
	private static final long serialVersionUID = 5136180108127531507L;
	

	/**
	* Main constructor. Specefies all necessary values to make a functional object. 
	* 
	* @param name: the name of the object
	* @param mass: the mass of the object (in earth masses)
	* @param x: the initial x location in the model NOT the display
	* @param y: the initial y location in the model NOT the display
	* @param vx: the initial x velocity
	* @param vy: the initial y velocity
	* @param color: the color of the object in the display
	* @param size: the size of the object in the display in pixels
	*/
	public Ob(String name,double mass, double x, double y, double vx, double vy, Color color, int size){
		this.name = name;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.color = color;
		this.size = size;
		System.out.println("New Object Created. "+toString());
	}

	

	/**
	* Simplified constructor that sets necesary unspecified variables to default values
	* 
	* @param name: the name of the object
	* @param mass: the mass of the object (in earth masses)
	* @param x: the initial x location in the model NOT the display
	* @param y: the initial y location in the model NOT the display
	*/
	public Ob(String name, double mass, double x, double y){
		this.name = name;
		this.mass = mass;
		this.x = x;
		this.y = y;

		
		//variables not specified by this simplified constructor
		
		//velocity values, default set to 0
		vx = 0;
		vy = 0;

		//default color is white
		color = Color.white;

		// 25 is the default size of earth
		size = mass*25;

	}



	/**
	*  Returns a String providing details about the current state of the Object
	* 
	* @return details about the current state of the Object 
	*/
	public String toString(){
		String str = "Name: "+name+", mass: "+mass+", x: "+x+", y: "+y;
		return str;
	}

}