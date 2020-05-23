package com.gussssy.orbitalsimulator;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
Used to create new objects and modify existing objects at runtime.
*/
public class PlanetBuilderPanel extends JPanel{

	/**The objects*/
	private ArrayList<Ob> objects;
	
	private OrbitalSimulator simulator;
	
	/**A JCombobox that wil contain the name each object*/
	private JComboBox planetSelect;

	/** A JPanel that displays an image of the selected object  */
	private PlanetViewCell cell;

	/**A JSpinner that displays and modifies the size of an object, purely cosmetic*/
	private JSpinner sizeSpinner;

	/**A JSpinner that has been modified to handel doubles and formatting of input */
	private DoubleJSpinner massSpinner;

	/**A checkbox that will centre this display around the selected object if checked*/
	private JCheckBox centerDisplayCheckBox;
	

	/**
	* PlanetBuilderPanel sole constructor.
	*
	* Initilizes the GUI elements, registers listeners and sets initial values of components.
	* @param objects and Array List of Ob. 
	*/
	public PlanetBuilderPanel(OrbitalSimulator simulator, ArrayList<Ob> objects){
		

		this.objects = objects;
		this.simulator = simulator;
		
		//Generate array of strings containing the name of each object in the collection for the combobox.
		String[] items = new String[objects.size()];
		for(int i = 0; i<objects.size(); i++){
			items[i] = objects.get(i).name;
		}

		//Iniitalize the planetSeclect combobox
		planetSelect = new JComboBox(items);
		add(planetSelect);
		//Add action listener and set function to be called on event using lambda expression
		planetSelect.addActionListener(e -> comboBoxSelectionChanged());

		//Setup the planet view cell
		cell = new PlanetViewCell(getSelectedObjectFromComboBox());
		cell.drawLabel(false);
		cell.drawOutline(false);
		add(cell);

		//Initialize the Size Spinner, set initial value, add listener 
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BorderLayout());
		JLabel sizeLabel = new JLabel("Size:");
		sizeSpinner = new JSpinner();
		sizeSpinner.setValue((int)getSelectedObjectFromComboBox().size);
		sizeSpinner.addChangeListener(e -> sizeSpinnerChanged());
		sizePanel.add(sizeLabel, BorderLayout.CENTER);
		sizePanel.add(sizeSpinner, BorderLayout.SOUTH);
		add(sizePanel);

		
		//Initialize the Mass Spinner, set initial value and add listener
		JPanel massControlPanel = new JPanel();
		massControlPanel.setLayout(new BorderLayout());
		massSpinner = new DoubleJSpinner(getSelectedObjectFromComboBox().mass, 0, 10000000, 1);
		massSpinner.addChangeListener(e -> massSpinnerChanged());
		massSpinner.setValue(getSelectedObjectFromComboBox().mass);
		massControlPanel.add(massSpinner, BorderLayout.CENTER);
		JLabel massLabel = new JLabel("Mass:");
		massControlPanel.add(massLabel, BorderLayout.NORTH);
		massSpinner.setWidth(75);
		add(massControlPanel);

		JPanel focusPanel = new JPanel();
		focusPanel.setLayout(new BorderLayout());
		centerDisplayCheckBox = new JCheckBox("Focus on this object ");
		centerDisplayCheckBox.addItemListener(e -> centreDisplayCheckBoxChanged(e));
		JLabel spacer = new JLabel(" ");
		focusPanel.add(spacer, BorderLayout.CENTER);
		focusPanel.add(centerDisplayCheckBox, BorderLayout.SOUTH);
		add(focusPanel);
	}


	/**
	*  Resets the planet builder panel when the reset button is pressed.
	*
	*  Gets the new objects from the model and sets the values of the comboboxes to the new objects. 
	*/
	public void reset(){
		
		objects = simulator.model.getObjects();
		resetCombobox();
		resetSpinners();
	}

	
	/**
	* Resets the combobox.
	*
	* Removes all the old items then adds the new items 
	*/
	private void resetCombobox(){
		
		//remove all old items from combobox
		planetSelect.removeAllItems();

		//loop through objects and add to combobox
		for(Ob object : objects){
			planetSelect.addItem(object.name);
		}
	}

	

	/**
	* Resets the values of the spinners 
	*/
	private void resetSpinners(){
		massSpinner.setValue(getSelectedObjectFromComboBox().mass);
		sizeSpinner.setValue((int)getSelectedObjectFromComboBox().size); //need to cast to int
	}



	/**
	 When the Object selected in the ComboBox changes this method is triggered. 
	
	 Sets the values of the various spinners to display the properties of the new selected Object

	*/
	public void comboBoxSelectionChanged(){
		
		cell.setObject(getSelectedObjectFromComboBox());
		resetSpinners();
		repaint();
	}

	

	/**
	 Changes the size value contained in an Object to the new value in the spinner.

	 Changing the size value effects what should be displayed so a call to repaint is made. 

	*/
	public void sizeSpinnerChanged(){
		
		//Casting value from the spinner from int to double, apprently I cannot directly cast to double or runtime error
		int i = (int)sizeSpinner.getValue();
		double d = (double)i;
		getSelectedObjectFromComboBox().size = d;
		
		//Need to  tell the cell setObject so the newly sized image is displayed.
		cell.setObject(getSelectedObjectFromComboBox());
		
		//Call repaint to update the display to show the chnaged size of the object in the cell
		repaint();
	}

	
	
	/**
	* Changes the mass value of the Object to the new value in the spinner
	*/
	public void massSpinnerChanged(){
		
		getSelectedObjectFromComboBox().mass = (double) massSpinner.getValue();
	}

	
	
	/**
	* Takes appropriate action when the.....
	*/
	private void centreDisplayCheckBoxChanged(ItemEvent e){
		//System.out.println(e.paramString());
		int state = e.getStateChange();
		// state = 1 Slected, state = 2 Deselected 
		if(state == 1){
			simulator.view.display.centreDisplayAroundObject(getSelectedObjectFromComboBox());
			simulator.view.display.centerDisplay();
		}else{
			simulator.view.display.dontCenterDisplay();
		}
	}

	

	/**
	Gets the Ob associated corresponding to the String that is currently selected in the combo box
	@return the Ob that corresponds to the String that is currenbtly selected in the combo box
	*/
	public Ob getSelectedObjectFromComboBox(){
		 
		// get the same of the currently selected object
		 String selectedObjectName = (String) planetSelect.getSelectedItem();
		 
		 //The Object we will return. Set to the first item to avoid compile error. 
		 Ob toReturn = objects.get(0);
		 
		 //Find the corresponding Object by looping through objects and returing the matching Object.
		 for(Ob o: objects){
		 	if(o.name.equals(selectedObjectName)){
		 	toReturn = o;
		 	break;
			}
		 }
		 return toReturn; 
	}	
}