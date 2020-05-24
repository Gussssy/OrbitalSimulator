package com.gussssy.orbitalsimulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.gussssy.orbitalsimulator.OrbitalSimulator;

/**
* Creates and holds all the GUI elements.
*/
public class OrbitalView{

	private OrbitalSimulator simulator;
	
	//  TODO: 23/5/20 determine which of these need to be public and which can be private. 
	public OrbitalDisplay display;
	public OrbitalControlPanel controlPanel;
	public PlanetBuilderPanel planetBuilderPanel;

	// The containing frame
	private JFrame frame;
	private int frameWidth = 1000;
	private int frameHeight = 1000;
	private Dimension frameDim = new Dimension(frameWidth,frameHeight);

	// DisplayPanel: The panel that displays the objects
	private int displayWidth = 1000;
	private int displayHeight = 800;
	private Dimension displayDim = new Dimension(displayWidth,displayHeight);

	// ControlPanel:  The panel at the top  containing various buttons to control the sim
	private int controlWidth = 1000;
	private int controlHeight = 75;
	private Dimension controlDim = new Dimension(controlWidth,controlHeight);

	// Planet View Panel: 
	private int planetPanelWidth = 1000;
	private int planetPanelHeight = 100;
	private Dimension planetPanelDim = new Dimension(planetPanelWidth,planetPanelHeight);

	// Planet Builder Panel
	private int planetBuilderPanelWidth = 1000;
	private int planetBuilderPanelHeight = 100;
	private Dimension planetBuilderPanelDim = new Dimension(planetBuilderPanelWidth,planetBuilderPanelHeight);

	public OrbitalView(OrbitalSimulator simulator){

		System.out.println("Initializing GUI");
		
		this.simulator = simulator;

		// Setup the containing frame
		System.out.println("Initializing Frame with Width: "+frameWidth+", Height: "+frameHeight);
		frame = new JFrame("Orbital Simulator");
		frame.setSize(frameWidth,frameHeight); //This works because the frame is the highest level component
		frame.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Make the Display Panel. (Display the simulation)
		display = new OrbitalDisplay(simulator, displayWidth,displayHeight);
		display.setPreferredSize(displayDim);
		display.setBackground(Color.black);
		display.setVisible(true);

		// Make the control panel (appears at top of the window, used to control the simulation)
		controlPanel = new OrbitalControlPanel(simulator);
		controlPanel.setPreferredSize(controlDim);
		controlPanel.setVisible(true);


		// Set up the PlanetBuilderPanel (appears at the bottom of the window, used to create objects and modifiy properties of objects)
		planetBuilderPanel = new PlanetBuilderPanel(simulator, simulator.model.getObjects());
		planetBuilderPanel.setPreferredSize(planetBuilderPanelDim);
		planetBuilderPanel.setVisible(true);


		// Put togther the GUI
		frame.add(controlPanel, BorderLayout.NORTH);
		frame.add(display, BorderLayout.CENTER);
		frame.add(planetBuilderPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.pack();

	}
	
	/**
	 * Resets GUI elements when the reset button is pressed. 
	 * - Reset puts the simulation back to day 0 and each object returns to its initial status.
	 * - This method is only called by the reset funtion of the main class.
	 * 
	 * - Currently the Planet Builder Panel is the only element that needs to be reset.
	 */
	public void reset(){
		
		planetBuilderPanel.reset();
	}

}