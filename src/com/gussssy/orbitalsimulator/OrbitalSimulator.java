package com.gussssy.orbitalsimulator;

import com.gussssy.orbitalsimulator.view.OrbitalView;

/**
* OrbitalSimulator : MCV Controller
*	- Program Entry Point
*	- Initialize the model and view
*	- Initialize the engine which then begins execution of the simulation
*	
*	BELOW NOT FULLY IMPLEMENTED
*	- Provide view and model components access to each other.
*			-  View components can talk to other view components without going through OrbitalSimulator
*			-  However the view must go through the controller to get to the model or engine
*	- facilitate user driven actions that require crossing between divisions of the program e.g view to engine to pause the simulation
*/
public class OrbitalSimulator{

	
	// PROGRAM COMPONENTS
	
	/**
	 * MVC: Controller.
	 **/
	public OrbitalSimulator simulator;
	
	/**
	* MVC: View. Contains all the View elements
	**/
	public OrbitalView view;
	
	/**
	 *  Executes the run loop that powers the simulation 
	 **/
	public OrbitalEngine engine;
	
	/**
	* MVC Model. Holds the data for the simulation and performs the calculations etc
	**/
	public OrbitalModel model;

	
	
	/**
	* Main method/Program entry point.
	* 
	* Initialises the Model and View then starts the run loop by calling run()
	*/
	public static void main(String[] args){

		System.out.println("Starting Orbital Simulator v2.0");
		
		OrbitalSimulator simulator = new OrbitalSimulator();
	}
	
	
	
	/**
	 * The sole OrbitalSimulator constructor
	 * - initializes the model and view
	 * - initializes the thread
	 * - starts the run loop
	 */
	public OrbitalSimulator(){
				
		simulator = this;
		
		//initialize the objects
		model = new OrbitalModel();

		//initialize the frame, display panel and control panel\
		view = new OrbitalView(simulator);

		// initialize the engine that will execute the run loop
		engine = new OrbitalEngine(simulator);
		
		// start the simulation
		engine.start();
	}
	

	
	/**
	* Pauses the simulation. The run loop still executes but the simualtion does not. 
	*/
	public void pause(){
		
		engine.pause();
	}

	
	
	/**
	* Plays the simulation if it was paused.
	*/
	public void play(){
		
		engine.play();
	}



	/**
	* Resets the simulation to day 0.
	* 
	* Calls reset on any other components that needs restting also. 
	*/
	public void reset(){
		
		model.reset();
		view.reset();
		engine.reset();
	}	
}