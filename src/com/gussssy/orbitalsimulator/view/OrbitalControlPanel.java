package com.gussssy.orbitalsimulator.view;

import java.awt.*;
import javax.swing.*;

import com.gussssy.orbitalsimulator.OrbitalSimulator;

import java.text.DecimalFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	
	private ButtonListener buttonListener = new ButtonListener();

	private JPanel div1 = new JPanel();
	private JButton plus = new JButton("+");
	private JButton minus = new JButton("-");
	private JButton reset = new JButton("RESET");
	
	private JPanel div2 = new JPanel();
	private JButton pause = new JButton("||");;
		
	private JPanel div3 = new JPanel();
	private JButton up = new JButton("^");
	private JButton down = new JButton("v");
	private JButton left = new JButton("<");
	private JButton right = new JButton(">");

	private JPanel speedControls = new JPanel();
	private JButton speedUp = new JButton(">>");
	private JButton speedDown = new JButton("<<");



	/**
	* Sole Constructor.
	*/
	public OrbitalControlPanel(OrbitalSimulator simulator){

		this.simulator = simulator;

		plus.addActionListener(buttonListener);
		minus.addActionListener(buttonListener);
		reset.addActionListener(buttonListener);
		pause.addActionListener(buttonListener);
		up.addActionListener(buttonListener);
		down.addActionListener(buttonListener);
		left.addActionListener(buttonListener);
		right.addActionListener(buttonListener);
		speedDown.addActionListener(buttonListener);
		speedUp.addActionListener(buttonListener);
		
		div1.add(plus);
		div1.add(minus);
		div1.add(reset);

		div2.add(pause);

		div3.setLayout(new BorderLayout());
		div3.add(up, BorderLayout.NORTH);
		div3.add(down, BorderLayout.CENTER);
		div3.add(left, BorderLayout.WEST);
		div3.add(right, BorderLayout.EAST);

		speedControls.add(speedDown);
		speedControls.add(speedUp);

		add(div1);
		add(div2);
		add(div3);
		add(speedControls);
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

