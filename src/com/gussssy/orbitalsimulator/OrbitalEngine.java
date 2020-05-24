package com.gussssy.orbitalsimulator;

/**
 * O R B I T A L   E N G I N E 
 *
 * - Executes a game/run loop which powers the simulation
 * - Run loop updates the simulation 60 times per second (if using the default update cap of 1.0/60.0)
 * 		- Each update simulates the change in motion of objects after a 24 hours
 * - Once the simulation has been updated, a frame is rendered
 *
 *
 **/
public class OrbitalEngine implements Runnable{

	/**
	 * MVC: Controller.
	 **/
	private OrbitalSimulator simulator;

	/**
	 * Currently does nothing but keep the run loop executing. Setting to false causes bad things.
	 */
	private boolean running = false;

	/**
	 * Controls when the simulation is to render a frame 
	 */
	private boolean render = false;

	/**
	 *  Caps update rate at 60 updates persecond 
	 */
	private final double UPDATE_CAP = 1.0/60.0; 

	/**
	 *  When true will uncap frame rate and print out average fps to the console.
	 *  The simulation will still update 60 times per second but a frame will be rendered for every iteration of the run loop.
	 */
	private boolean efficiencyTestMode = false;

	/**
	 * When true, fps will be printed to the console every second
	 */
	private boolean printFPS = true;
	
	/**
	When true, the simulation will not execute. NOTE: why does this still work when static? Because static can be used in non static, but no vice versa
	*/
	private boolean paused = false;

	/**
	 * The thread that will execute the run loop 
	 */
	private Thread thread;



	///////////////////////////////////////////////////////////////////////////////////////////////
	//						W O R K   I N   P R O G R E S S 									//
	///////////////////////////////////////////////////////////////////////////////////////////////
	// All variables below are new and being tested. 24-5-20
	double[] updateCaps = {1.0/15.0, 1.0/30.0, 1.0/60.0, 1.0/120.0, 1.0/240.0};
	int updateCapIndex = 2;
	double updateCap = 1.0/60.0;
	double frameCap = 1.0/60.0;

	///////////////////////////////////////////////////////////////////////////////////////////////



	public OrbitalEngine(OrbitalSimulator simulator){

		this.simulator = simulator;	
	}
	
	
	/**
	 * Starts the simulation. Makes a new thread and assigns it to execution of the run loop. 
	 */
	public void start(){
		
		//initialize the thread that will execute the run loop
		thread = new Thread(this); 
						
		//begin the simulation
		thread.run();	
	}



	/**
	 * Uses a basic game loop to run the simulation. 
	 *  
	 *  The simulation only updates after a certain amount of time (UPDATE_CAP) has passed.
	 *  After an update the boolean render is set to true so a frame is rendered on the current iteration.
	 *  If efficiency test mode is enabled, frame rate will be uncapped to allow testing of renderering efficiency.
	 *   
	 * 
	 * This was adapted from youtuber Majoolwip, specifically this video: https://www.youtube.com/watch?v=4iPEjFUZNsw&t
	 * 
	 * 
	 * 
	 * Currently the speed of the simulation can be chnaged but at the moment this coupled to the frame rate which is particularily problematic at low speeds.
	 * For the solution I have narrowed down two options: 
	 * 	1. Allow update caps that 1x, 2x, 3x, 5x, 10x, 50x, 0.5x, where x is the initial update cap. For 2x, render every second update, for 3x every third etx
	 * 	2. Have a second time variable to track how long since last frame and enforce 60fps regardless of update rate.
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

		// [TEST_FRAME_LIMITING] TESTING NEW WAY TO LIMIT FRAMES WITH LOW UPDATE CAP (low update cap = lots of updates)
		int count = 0;


		while(running){

			//Update timing variables for this iteration
			firstTime = System.nanoTime() / 1000000000.0; 	//get the current time 
			passedTime = firstTime - lastTime;				//determine time passed since last iteration
			lastTime = firstTime; 							//set lastTime for the next iteration
			unprocessedTime += passedTime;					//accumulate unprocessedTime
			frameTime += passedTime;						//accumulate time since last fps count


			// UPDATE BLOCK
			// simulation waits for enough time to pass before updating
			while(unprocessedTime >= updateCap){										

				//SIMULATION IS READY TO UPDATE 


				unprocessedTime -= updateCap;	//if the thread freezes, and we miss multiple updates, as we don't reset unprocessed time to 0, it will update again until condition is not met

				// Set render to true so a frame is rendered on this iteration of the run loop
				render = true;


				// [TEST_FRAME_LIMITING]
				if(count >= 60){
					//System.out.println("Count Reset");
				}else{
					count++;
				}


				//UPDATE: Simulate a day (as long as simulation is not paused)
				if(!paused)simulator.model.simulateDay();
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
				simulator.view.display.repaint();
				frames++;

				// set render to false so rendering occurs once after each update
				//		if in efficiency test mode...
				if(!efficiencyTestMode)render = false;	


			}else{

				// NOT RENDERING ON THIS ITERATION

				// frame was not rendered on this iteration of the run loop. Thread will sleep for 1 millisecond

				try{
					Thread.sleep(1);
				} catch (InterruptedException e){e.printStackTrace();}
			}	
		}
	}

	
	/** WORK IN PROGRESS
	 * 
	 **/
	public void increaseUpdateCap(){


		if(updateCapIndex > 0){
			updateCapIndex--;
		}

		updateCap = updateCaps[updateCapIndex];
		System.out.println("New Update Cap: "+ updateCap);
	}

	/** WORK IN PROGRESS
	 * 
	 **/
	public void decreaseUpdateCap(){

		if(updateCapIndex < updateCaps.length-1){
			updateCapIndex++;
		}

		System.out.println("Did the simulation speed up?");


		updateCap = updateCaps[updateCapIndex];
		System.out.println("New Update Cap: "+ updateCap);
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
	* Pauses the simulation. The run loop still executes but the simualtion does not. 
	*/
	public void pause(){
		paused = true;
	}


	/**
	* Plays the simulation if it was paused.
	*/
	public void play(){
		paused = false;
	}
	
	public void reset(){
		
		updateCap = UPDATE_CAP;
	}
	
	public String toString(){
		
		String message = "Engine is running: " + running;
		return message;
	}


}
