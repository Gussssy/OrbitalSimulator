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
public class OrbitalSimulator implements Runnable{

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
	boolean running = false;
	
	/**
	 * Controls when the simulation is to render a frame 
	 */
	boolean render = false;
	
	/**
	 *  Caps update rate at 60 updates persecond 
	 */
	final double UPDATE_CAP = 1.0/60.0; 
	
	/**
	 *  When true will uncap frame rate and print out average fps to the console.
	 *  The simulation will still update 60 times per second but a frame will be rendered for every iteration of the run loop.
	 */
	boolean efficiencyTestMode = true;
	
	/**
	 * When true, fps will be printed to the console every second
	 */
	boolean printFPS = true;
	
	/**
	 * The thread that will execute the run loop 
	 */
	private Thread thread;
	
	/**
	 * The single instance of OrbitalSimulator 
	 **/
	public OrbitalSimulator simulator;
	
	
	//only here to allow compliation with old code that will not be used but is there for reference for now
	@Deprecated
	static boolean run = true;
	
	/**
	When true, the simulation will not execute
	*/
	static boolean paused = false;

	/**
	Time delay after each execution of the run loop. Minimum value is 1ms. There is no maximum value.
	*/
	@Deprecated
	static int timeDelay = 100;
	
	/**
	The time delay uised by the ruin loop when the simulation is paused
	*/
	@Deprecated
	static final int PAUSE_TIME_DELAY = 100;
	
	/**
	* Holds the value of time delay when the simulation is paused. When the simulation is unpaused, this value is set to the time delay
	*/
	@Deprecated
	static int playTimeDelay = 100;

	


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
	 * OrbitalSimulator Constructor
	 * - initializes the model and view
	 * - initializes the thread
	 * - starts the run loop
	 */
	public OrbitalSimulator(){
				
		simulator = this;
		
		//initialize the objects
		model = new OrbitalModel();

		//initialize the frame, display panel and control panel\
		view = new OrbitalView();

		//initialize the thread that will execute the run loop
		thread = new Thread(simulator); 
		
		//begin the simulation
		thread.run();
		System.out.println("After thread start");
	}

	/**
	 * Uses a basic game loop to run the simulation. 
	 *  - the simulation only updates after a certain amount of time (UPDATE_CAP) has passed. 
	 * 
	 * This was adapted from youtuber Majoolwip, specifically this video: https://www.youtube.com/watch?v=4iPEjFUZNsw&t=6s
	 * 
	 **/
	@Override
	public void run(){

		running = true;

		// time past tracking variables
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;

		//fps tracking variables
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		int updates = 0;
		int accumulatedFrames = 0;
		int numSeconds = 0;
		

		while(running){
				

			//Update timing variables for this iteration
			firstTime = System.nanoTime() / 1000000000.0; 	//get the current time 
			passedTime = firstTime - lastTime;				//determine time passed since last iteration
			lastTime = firstTime; 							//set lastTime for the next iteration
			unprocessedTime += passedTime;					//accumulate unprocessedTime
			frameTime += passedTime;						//accumulate time since last fps count

			
			// UPDATE BLOCK
			// simulation waits for enough time to pass before updating
			while(unprocessedTime >= UPDATE_CAP){										

				//SIMULATION IS READY TO UPDATE 
				
				
				unprocessedTime -= UPDATE_CAP;	//if the thread freezes, and we miss multiple updates, as we don't reset unprocessed time to 0, it will update again until condition is not met
				
				// Set render to true so a frame is rendered on this iteration of the run loop
				render = true;

				//UPDATE: Simulate a day (as long as simulation is not paused)
				if(!paused)model.simulateDay();
			}
			
			
			// FPS TRACKING BLOCK
			// count FPS after 1 second
			if(frameTime >= 1.0){

				//Determines max possible frame rendering for efficiency testing purposes
				if(efficiencyTestMode){
					accumulatedFrames += frames;
					numSeconds++;
					System.out.println("Average Max FPS: " + getAverageMaxFrames(accumulatedFrames, numSeconds));
				}
				
				//reset frame counting variables
				frameTime = 0;
				fps = frames;
				frames = 0;
				
				if(printFPS)System.out.println("fps: "+fps);
			}

			
			
			// RENDER BLOCK
			// If render is true, the simulation has been updated, these updates need to be displayed
			if(render){
				
				//RENDER A FRAME
				
				//Update the display
				view.display.repaint();
				frames++;
				
				// set render to false so rendering occurs once after each update
				//		if in efficiency test mode...
				if(!efficiencyTestMode)render = false;	
				

			}else{
				
				// NOT RENDERING ON THIS ITERATION
				
				// frame was not rendered on this iteration of the run loop. Thread will sleep for 1 millisecond
				//System.out.println("I didnt render");
				
				try{
					Thread.sleep(1);
				} catch (InterruptedException e){e.printStackTrace();}
			}	
		}
	}

		
	/**
	 *  Calculates the average fps since the program started running. 
	 * 
	 * @param framesTotal: total number of frames since the program was started
	 * @param seconds: time the program has been running
	 * @return the average frames per second since the program started 
	 */
	private double getAverageMaxFrames(int framesTotal, int seconds){
		
		double averageMaxFrames = (double)framesTotal / (double)seconds;
		return averageMaxFrames;
	}

	/**
	* Infinite loop that runs the simulation as long as it is not paused. 
	* Still executes when paused but the simulation halts. 
	*/
	@Deprecated
	private static void runOld(){
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
	@Deprecated 
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
	@Deprecated
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
		view.reset();
	}



	
}