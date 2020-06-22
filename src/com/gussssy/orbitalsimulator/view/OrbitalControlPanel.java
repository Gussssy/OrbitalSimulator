package com.gussssy.orbitalsimulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.gussssy.orbitalsimulator.OrbitalSimulator;

/**
* A Panel located at the top of the frame containing various controls for the simulation such as:
* 	- pause/play
* 	- reset
* 	- pan left/right/up/down
* 	- zooom in/out
* 	- speed up/down 
*/
public class OrbitalControlPanel extends JPanel{

	private OrbitalSimulator simulator;
	private OrbitalView view;
	
	private ButtonListener buttonListener = new ButtonListener();

	private JPanel zoomControlsPanel = new JPanel();
	
	private JButton plus = new JButton("+");
	private JButton minus = new JButton("-");
	
	private JPanel resetPanel = new JPanel();
	private JButton reset = new JButton("RESET");
	
	private JPanel pausePanel = new JPanel();
	private JButton pause = new JButton("||");;
		
	private JPanel panControlsPanel = new JPanel();
	private JButton up = new JButton("^");
	private JButton down = new JButton("v");
	private JButton left = new JButton("<");
	private JButton right = new JButton(">");

	private JPanel speedControls = new JPanel();
	private JButton speedUp = new JButton(">>");
	private JButton speedDown = new JButton("<<");
	
	private JPanel checkBoxPanel = new JPanel();
	private JCheckBox showVelocityComponents = new JCheckBox("Show velocity components");
	private JCheckBox showTrails = new JCheckBox("Show trails");
	
	private Dimension buttonSize = new Dimension(20,20);



	/**
	* Sole Constructor.
	*/
	public OrbitalControlPanel(OrbitalSimulator simulator, OrbitalView view){

		this.simulator = simulator;
		this.view = view;

		
		
		pause.addActionListener(buttonListener);
		up.addActionListener(buttonListener);
		down.addActionListener(buttonListener);
		left.addActionListener(buttonListener);
		right.addActionListener(buttonListener);
		speedDown.addActionListener(buttonListener);
		speedUp.addActionListener(buttonListener);
		
		// Zoom controls
		plus.addActionListener(buttonListener);
		minus.addActionListener(buttonListener);
		zoomControlsPanel.setLayout(new BorderLayout());
		zoomControlsPanel.add(plus, BorderLayout.CENTER);
		zoomControlsPanel.add(minus, BorderLayout.SOUTH);
		
		
		// Reset Button
		reset.addActionListener(buttonListener);
		resetPanel.add(reset);

		// Pause button
		pausePanel.add(pause);

		// Pan controls
		panControlsPanel.setLayout(new BorderLayout());
		//up.setPreferredSize(buttonSize);
		panControlsPanel.add(up, BorderLayout.NORTH);
		panControlsPanel.add(down, BorderLayout.CENTER);
		panControlsPanel.add(left, BorderLayout.WEST);
		panControlsPanel.add(right, BorderLayout.EAST);

		// Speed controls
		speedControls.add(speedDown);
		speedControls.add(speedUp);
		
		// Check boxes
		showVelocityComponents.addItemListener(e -> velocityComponentsBoxStateChanged(e));
		showTrails.addItemListener(e -> showTrailsBoxStateChanged(e));
		showTrails.setSelected(true);
		checkBoxPanel.add(showVelocityComponents);
		checkBoxPanel.add(showTrails);
		

		// Add control panel elements together
		add(zoomControlsPanel);
		add(resetPanel);
		add(pausePanel);
		add(panControlsPanel);
		add(speedControls);
		add(checkBoxPanel);
	}

	

	private void pausePressed(){

		System.out.println("Pause/Play Button Pressed");

		if(pause.getText().equals("||")){
			pause.setText("o");
			simulator.pause();

		}else{
			pause.setText("||");
			simulator.play();
		}
		
	}

	

	private void plusPressed(){
		System.out.println("Plus Pressed");
		simulator.view.display.increaseModelScale();

	}

	

	private void minusPressed(){
		System.out.println("Minus Pressed");
		simulator.view.display.decreaseModelScale();

	}

	

	private void resetPressed(){
		System.out.println("Reset Pressed");
		simulator.reset();

	}

	

	private void upPressed(){
		System.out.println("Up Pressed");
		simulator.view.display.panUp();
		simulator.view.planetBuilderPanel.deselectFocusBox();

	}

	

	private void downPressed(){
		System.out.println("Down Pressed");
		simulator.view.display.panDown();
		simulator.view.planetBuilderPanel.deselectFocusBox();

	}

	

	private void leftPressed(){
		System.out.println("Left Pressed");
		simulator.view.display.panLeft();
		simulator.view.planetBuilderPanel.deselectFocusBox();

	}

	

	private void rightPressed(){
		System.out.println("Right Pressed");
		simulator.view.display.panRight();
		simulator.view.planetBuilderPanel.deselectFocusBox();

	}

	

	private void speedDownPressed(){
		System.out.println("Speed Down Pressed");
		//OrbitalSimulator.increaseTimeDelay();
		simulator.engine.increaseUpdateCap();

	}

	

	private void speedUpPressed(){
		System.out.println("Speed Up Pressed");
		//OrbitalSimulator.decreaseTimeDelay();
		simulator.engine.decreaseUpdateCap();
		//simulator.increaseUpdateCap();

	}
	
	private void velocityComponentsBoxStateChanged(ItemEvent e){
		int state = e.getStateChange();
		//System.out.println("velocityComponentsBox state: " + state);
		
		if(state == 1)view.toggleDrawVelocityComponents(true);
		if(state == 2)view.toggleDrawVelocityComponents(false);
		
	}
	
	private void showTrailsBoxStateChanged(ItemEvent e){
		int state = e.getStateChange();
		//System.out.println("velocityComponentsBox state: " + state);
		
		if(state == 1)view.setDrawTrails(true);
		if(state == 2)view.setDrawTrails(false);
		
	}

	

	/**
	* An Action Listener that can listen to all buttons in the OrbitalControlPanel with a single instance.
	*/
	private class ButtonListener implements ActionListener{

		String buttonText = "";

		
		@Override
		public void actionPerformed(ActionEvent e){

			System.out.println("Button Pressed: "+e.getActionCommand());
			buttonText = e.getActionCommand();
			switch(buttonText){
				
				case "+":
				plusPressed();
				break;

				case "-":
				minusPressed();
				break;

				case "RESET":
				resetPressed();
				break;

				case "||":
				pausePressed();
				break;

				case "o":
				pausePressed();
				break;

				case "^":
				upPressed();
				break;

				case "v":
				downPressed();
				break;

				case ">":
				rightPressed();
				break;

				case "<":
				leftPressed();
				break;

				case "<<":
				speedDownPressed();
				break;

				case ">>":
				speedUpPressed();
				break;

				default :
				System.out.println("Something is very wrong. See OrbitalControlPanel, ButtonListener.");
			}


		}
	}
}

