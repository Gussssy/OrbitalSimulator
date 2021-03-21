package com.gussssy.orbitalsimulator.view;

import java.awt.Color;
import java.util.ArrayList;

import com.gussssy.orbitalsimulator.Ob;
import com.gussssy.orbitalsimulator.OrbitalModel;
import com.gussssy.orbitalsimulator.OrbitalSimulator;

/**
 * Responsible for creating and managing trails that show the previous location of objects in the simulation. 
 * 
 * Trails are made of a series of TrailNodes which are visible in the display as a white circle/square that becomes increasingly more transparent over its lifetime untill removed.  
 **/
public class TrailManager {
	
	// controls printing of debug infomation to console
	private boolean print = false;
	
	private OrbitalSimulator simulator;
	
	public ArrayList<TrailNode> nodes = new ArrayList<TrailNode>();
	
	// nodes that have reach the end of thier life time and will be removved next update.
	private ArrayList<TrailNode> deadNodes = new ArrayList<TrailNode>();
	
	// an array containing each color a trail node will display untill being removed
	private Color[] nodeColors;
	
	// how many times the alpha level of a trail node will be lowered before becoming fully trasparent and then removed. 
	private int numberOfAlphaLevels = 200;
	
	
	// How often trails are updated. 1 = trails updated every update, 10 = trails update every 10 updates
	private int trailUpdateFrequency = 3;
	
	// How many updates have occured since the trail nodes where updated
	private int updatesSinceLastTrailUpdate = 0;
	
	
	
	/**
	 * Constructs a new TrailManager Object
	 **/
	public TrailManager(OrbitalSimulator simulator){
		
		System.out.println("Initializing TrailManager");
		
		this.simulator = simulator; 
		
		double alphaInitialValue = 255;
		double alphaDecrement = alphaInitialValue/(double)numberOfAlphaLevels;
		double alpha;
		
		System.out.println("Alpha Decrement: "+ alphaDecrement);
		
		nodeColors = new Color[numberOfAlphaLevels];
		
		
		for(int i = 0; i < numberOfAlphaLevels; i++){
			alpha = alphaInitialValue - alphaDecrement * i;
			nodeColors[i] = new Color(255, 255, 255, (int)alpha);
			if(print)System.out.println("Color Alpha: " + nodeColors[i].getAlpha());
		}
	}
	
	
	
	/**
	 * Updates the trails if enough updates have past since trails were last updated.
	 **/
	public void update(){
		
		if(updatesSinceLastTrailUpdate == trailUpdateFrequency){
			makeNodes(simulator.model.getObjects());
			updateNodes();
			updatesSinceLastTrailUpdate = 0; 
		}else{
			updatesSinceLastTrailUpdate++;
		}
		
	}
	
	
	/**
	 * Makes a trail node at the current position of each object in the simulation
	 * 
	 * @param objects The objects in the simulation. A trail node will be made for each object in this list. 
	 **/
	public void makeNodes(ArrayList<Ob> objects){
		
		for(Ob o : objects){
			TrailNode newNode = new TrailNode(o.dispX, o.dispY, nodeColors[0], numberOfAlphaLevels);
			nodes.add(newNode);
		}
	}
	
	
	
	/**
	 * Updates the trail nodes: 
	 * - delete all nodes that have reached the end of thier lifetime
	 * - lower the alpha value of each existing node by an amount determined by the length of the trail 
	 **/
	public void updateNodes(){
		
		
		for(TrailNode node : nodes){
			// remove dead nodes if lifeTime is 0
			if(node.lifeTime == 0)deadNodes.add(node);
		}
		
		// Remove any nodes that are now dead
		for(TrailNode node : deadNodes){
			nodes.remove(node);
		}
		
		// clear the dead nodes list for next time. 
		deadNodes.clear();
		
		
		// Decrement remaining lifetime for current nodes
		for(TrailNode node : nodes){
			
			node.lifeTime--;
			node.color = nodeColors[numberOfAlphaLevels - node.lifeTime -1];
			// if set to 0, will display 0 colour till next update where it will be killed
			
		}
	}
	
	/**
	 * Resets the trails when the reset button has been pressed
	 **/
	public void reset(){
		nodes.clear();
	}

}
