package com.gussssy.orbitalsimulator;

import java.util.concurrent.TimeUnit;

/**
* OrbitalSimulator : MCV Controller
*	- Program Entry Point
*	- Initialize the model 
*	- Initialize the view
*	- Provide the view access to the model data (not quite the case at the moment)
*	- Execute the simulation
*	- pause/play the simulation
*	- reset the simulation
*	- speed up / slow down the simulation
*/
public class OrbitalSimulator{

	/**
	* MVC: View. Contains all the View elements
	*/
	static OrbitalView view;
	
	/**
	* MVC Model. Holds the data for the simulation and performs the calculations etc
	*/
	static OrbitalModel model;

	/**
	* Currently does nothing but keep the run loop executing. Setting to false causes bad things.
	*/
	static boolean run = true;
	
	/**
	When true, the simulation will not execute
	*/
	static boolean paused = false;

	/**
	Time delay after each execution of the run loop. Minimum value is 1ms. There is no maximum value.
	*/
	static int timeDelay = 100;
	
	/**
	The time delay uised by the ruin loop when the simulation is paused
	*/
	static final int PAUSE_TIME_DELAY = 100;
	
	/**
	* Holds the value of time delay when the simulation is paused. When the simulation is unpaused, this value is set to the time delay
	*/
	static int playTimeDelay = 100;

	


	/**
	* Main method/Program entry point.
	* 
	* Initialises the Model and View then starts the run loop by calling run()
	*/
	public static void main(String[] args){

		System.out.println("Starting Orbital Simulator v2.0");

		//initialize the objects
		model = new OrbitalModel();

		//initialize the frame, display panel and control panel\
		view = new OrbitalView();

		//begin the simulation
		run();
	}

	

	/**
	* Infinite loop that runs the simulation as long as it is not paused. 
	* Still executes when paused but the simulation halts. 
	*/
	private static void run(){
		while(run){

			if(!paused){
			
				//1. Simulate a day
				model.simulateDay();
			
				//2. Update the display
				view.display.repaint();
			}else{

				//Simulation is paused update the display anyway
				view.display.repaint();

			}

			//4. Pause execution for some time
			try{
				TimeUnit.MILLISECONDS.sleep(timeDelay);
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
		}
	}

	


	/**
	* Decreases the speed of the simulation by increasing the time delay in the run loop by 10ms.
	* 
	* Does nothing if the simulation is paused. 
	*/
	public static void increaseTimeDelay(){

		//If the simulation is paused, do nothing
		if(paused){
			System.out.println("The Simulation is Paused, Speed Cannot be modified");
			return;
		}

		timeDelay += 10;
		System.out.println("TimeDelay is now: "+timeDelay);
	}

	


	/**
	* Decreases the time delay by 10ms in the run loop which speeds up the simulation.
	* 
	* Does nothing if the simulation is paused or the time delay is at is minimum value of 0ms.
	* If the time delay is below 10ms, will only decrease the time delay by 1ms. 
	*/
	public static void decreaseTimeDelay(){

		//If the simulation is paused, do nothing
		if(paused){
			System.out.println("The Simulation is Paused, Speed Cannot be modified");
			return;
		}

		if(timeDelay >= 20){
			timeDelay -= 10;
		}else{
			if(timeDelay>=1){
				timeDelay -= 1;
			}else{
				System.out.println("Time Delay is at minimum value");
			}
		}

		System.out.println("TimeDelay is now: "+timeDelay);
		
	}

	


	/**
	* Pauses the simulation. The run loop still executes but the simualtion does not. 
	*/
	public static void pause(){
		playTimeDelay = timeDelay;
		timeDelay = PAUSE_TIME_DELAY;
		paused = true;
	}

	


	/**
	* Plays the simulation if it was paused.
	*/
	public static void play(){
		timeDelay = playTimeDelay;
		paused = false;
	}

	/**
	* Resets the simulation to day 0.
	* 
	* Calls reset on any other components that needs restting also. 
	*/
	public static void reset(){
		
		model.reset();
		
		view.planetBuilderPanel.reset();
	}
}