package com.gussssy.orbitalsimulator;

import java.awt.Color;
import java.util.ArrayList;

/**
* Orbital Model. MVC: Model
* 
* Creates and holds the data for the simulation. 
* Requests data manipulations that are carried out by OrbitalMath, a static helper class.
*/
public class OrbitalModel{

	/**The objects in the simulation*/
	private ArrayList<Ob> objects = new ArrayList<Ob>();

	// Model Dimensions
	private static final int HEIGHT = 1200; 
	private static final int WIDTH = 1200;
	private static final double CENTRE = HEIGHT/2;

	//Physical distance vales, in mKm (million kilometres)
	private static final double D_EARTH_SUN = 149.6;
	private static final double D_MARS_SUN = 227.9;
	private static final double ISO = 105.783174465;
	private static final double D_JUPITER_SUN = 778.5;
	private static final double D_VENUS_SUN = 108.2;
	private static final double D_MERCURY_SUN = 57.91;
	private static final double D_SATURN_SUN = 1400;
	private static final double D_NEPTUNE_SUN = 1;
	private static final double D_URANUS_SUN = 1;
	private static final double D_MOON_EARTH = 0.384400;
	
	// Velocity values (km/s)
	private final double V_MOON_EARTH = 1.022;
	
	// The day the simulations has reached for the model instance
	private int day = 0;

	
	/** The scale of the model. Set to 1 million km per pixel*/
	private final int MODEL_SCALE = 1000000;

	

	/**
	* Constructor. Initilialies the data for the simulation. 
	*/
	public OrbitalModel(){
		
		System.out.println("Initializing OrbitalModel");
		initializeObjects();
	}

	private void initializeObjects(){

		//Initialize the objects

		//Solar Syatem objects
		Ob sun = new Ob("The Sun",330000, CENTRE,CENTRE, 0,0, new Color(255,255,0),75);
		Ob earthQ1_2 = new Ob("Earth",1,CENTRE,CENTRE-D_EARTH_SUN, 29.78,0,  new Color(0,100,255),25);
		Ob mars = new Ob("Mars", 0.107, CENTRE, CENTRE-D_MARS_SUN, 24, 0, new Color(200, 50, 50),13);
		Ob jupiter = new Ob("Jupiter", 318, CENTRE, CENTRE-D_JUPITER_SUN, 13.07,0, new Color(153,102,0),80);
		Ob venus = new Ob("Venus", 0.815, CENTRE, CENTRE-D_VENUS_SUN, 35.02,0, new Color(255,255,204),24);
		Ob mercury = new Ob("Mercury", 0.055, CENTRE, CENTRE-D_MERCURY_SUN, 47.362,0, new Color(212,209,195),10);
		Ob saturn = new Ob("Saturn", 95.159,CENTRE, CENTRE-D_SATURN_SUN, 9.68, 0, new Color(110,100,120), 70);
		Ob moon = new Ob("Moon", 0.012, CENTRE, CENTRE-D_EARTH_SUN-D_MOON_EARTH, 29.78 + V_MOON_EARTH, 0, new Color(100,100,100), 3);

		// Additional Objects to create interesting interactions 
		Ob mars2 = new Ob("Big Mars", 250, CENTRE-D_MARS_SUN, CENTRE-D_MARS_SUN, -24, 0, new Color(200, 50, 50),35);
		Ob intruder = new Ob("intruder", 10, CENTRE-D_JUPITER_SUN,CENTRE);
		intruder.size = 50;
		intruder.vx = 15;
		intruder.vy = 3;


		objects.add(sun);
		objects.add(earthQ1_2);
		objects.add(mars);
		objects.add(jupiter);
		objects.add(venus);
		objects.add(mercury);
		objects.add(saturn);
		objects.add(moon);
		//objects.add(mars2);
		//objects.add(intruder);


	}


	/**
	* Execute simulation of a day 
	*/
	public void simulateDay(){
		
		requestVelocityUpdateFullGravity();
		requestPositionUpdateFullGravity();
		day++;
	}



	/**
	* Updates the velocity of each obeject. Gravity only calculated between the first object
	*
	* Simplified Gravity: Gravitational effects only calculated between the centre objects and surronding child objects.  
	*/
	private void requestVelocityUpdateSimplifiedGravity(){

		//update velocity cause by sun on objects
		for(int x = 1; x < objects.size() ; x++ ){
			OrbitalMath.updateVelocity(objects.get(0),objects.get(x));
		}

	}



	/**
	* Updates the position of each objects. 
	* 
	* Simplified Gravity: Gravitational effects only calculated between the centre objects and surronding child objects. 
	*/
	private void requestPositionUpdateSimplifiedGravity(){
		
		//Update position of the obejcts excluding the central parent object
		for(int x = 1; x < objects.size() ; x++ ){
			OrbitalMath.updatePositions(objects.get(x));
		}

	}



	/**
	* Updates the velocity of the objects after a fay in the simulation.
	*
	* Full Gravity: Gravitational interactions are calculated for all objects, between all other objects.
	*/
	private void requestVelocityUpdateFullGravity(){
		
		//Outer Loop: loops through objects. each iteration calculates the force the current/parent object
		//	exerts on all other bodies EXCLUDING ITSELF
		for(int parent = 0; parent < objects.size(); parent++){
			
			//Inner Loop: Loops through objects and calculates force/velocityy impact of the parent body on the child body

			for(int child = 0; child < objects.size(); child++){

				// If the two Obs being compared are the same, do not calculate
				if(parent == child){
				
					continue;
				}

				//System.out.println("Calculating effect of: "+objects.get(parent).name+ " on "+ objects.get(child).name);
				OrbitalMath.updateVelocity(objects.get(parent),objects.get(child));

			}
		}
	}



	/**
	* Updates the position of the objects after a day in the simulation.
	*
	* Full Gravity: Gravitational interactions are calculated for all objects, between all other objects.
	*/
	private void requestPositionUpdateFullGravity(){
		
		//update position of the obejcts
		for(int x = 0; x < objects.size() ; x++ ){
			OrbitalMath.updatePositions(objects.get(x));
		}
	}



	/**
	* Get the Objects ArrayList.
	*
	* @return the Objects.
	*/
	public ArrayList<Ob> getObjects(){
		return objects;
	}


	/**
	* Gets the day the simulation has reached
	*
	* @return day   
	*/
	public int getDay(){
		return day;
	}

	

	/**
	* Get the Model Scale.
	*
	* @return Model Scale
	*/
	public int getModelScale(){
		return MODEL_SCALE;
	}

	

	/**
	* Reset the model to initial state.
	*/
	public void reset(){
		day = 0;
		objects = new ArrayList<Ob>();
		initializeObjects();
	}



}