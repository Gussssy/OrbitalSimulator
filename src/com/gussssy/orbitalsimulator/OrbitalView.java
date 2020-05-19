package com.gussssy.orbitalsimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
* Creates and holds all the GUI elements.
*/
public class OrbitalView{

	
	public OrbitalDisplay display;
	public OrbitalControlPanel controlPanel;
	public PlanetViewPanel planetViewPanel;
	public PlanetBuilderPanel planetBuilderPanel;

	//The containing frame
	public JFrame frame;
	private int frameWidth = 1000;
	private int frameHeight = 1000;
	private Dimension frameDim = new Dimension(frameWidth,frameHeight);

	//DisplayPanel: The panel that displays the objects
	private int displayWidth = 1000;
	private int displayHeight = 800;
	private Dimension displayDim = new Dimension(displayWidth,displayHeight);

	// ControlPanel:  The panel at the top  containing various buttons to control the sim
	private int controlWidth = 1000;
	private int controlHeight = 75;
	private Dimension controlDim = new Dimension(controlWidth,controlHeight);

	//Planet View Panel: 
	private int planetPanelWidth = 1000;
	private int planetPanelHeight = 100;
	private Dimension planetPanelDim = new Dimension(planetPanelWidth,planetPanelHeight);

	//Pnaet BuilderPanel
	private int planetBuilderPanelWidth = 1000;
	private int planetBuilderPanelHeight = 100;
	private Dimension planetBuilderPanelDim = new Dimension(planetBuilderPanelWidth,planetBuilderPanelHeight);

	public OrbitalView(){

		System.out.println("Initializing GUI");

		//1.Make the frame
		System.out.println("Initializing Frame with Width: "+frameWidth+", Height: "+frameHeight);
		frame = new JFrame("Orbital Simulator");
		frame.setSize(frameWidth,frameHeight); //This works because the frame is the highest level component
		frame.setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setBackground(Color.black); //This does nothijng
		
		//Set the Layount of the containing frame
		frame.setLayout(new BorderLayout());

		// Make the Display Panel
		display = new OrbitalDisplay(displayWidth,displayHeight);
		display.setPreferredSize(displayDim);
		display.setBackground(Color.black);
		display.setVisible(true);

		// Make the control panel (button panel)
		controlPanel = new OrbitalControlPanel();
		controlPanel.setPreferredSize(controlDim);
		controlPanel.setVisible(true);

		// Make the PlanetViewPanel
		planetViewPanel = new PlanetViewPanel(OrbitalSimulator.model.getObjects());
		planetViewPanel.setPreferredSize(planetPanelDim);
		planetViewPanel.setVisible(false);

		planetBuilderPanel = new PlanetBuilderPanel(OrbitalSimulator.model.getObjects());
		planetBuilderPanel.setPreferredSize(planetBuilderPanelDim);
		planetBuilderPanel.setVisible(true);


		//5. Put togther the GUI
		frame.add(controlPanel, BorderLayout.NORTH);
		frame.add(display, BorderLayout.CENTER);
		frame.add(planetViewPanel, BorderLayout.SOUTH);
		frame.add(planetBuilderPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.pack();

	}

}