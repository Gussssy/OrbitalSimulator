/** PlanetViewPanel

An optional panel that appears at the bottom of the application. 
Contains cells which display the name of the Object and an image of the object



*/
package com.gussssy.orbitalsimulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlanetViewPanel extends JPanel{

	public PlanetViewPanel(ArrayList<Ob> objects){
		System.out.println("Initialising PlanetControlPanel");

		setLayout(new FlowLayout());

		for(Ob object : objects){
			//add(new JButton(object.name));
			add(new PlanetViewCell(object));
		}

	}

}