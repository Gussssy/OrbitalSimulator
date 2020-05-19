package com.gussssy.orbitalsimulator;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
* A JSpinner that uses doubles rather then int.
* 
* Also formats input.
*/
public class DoubleJSpinner extends JSpinner{

	

	/**
	* Constructor. 
	*
	* @param initValue the initial value the Jspinner will display
	* @param minValue the minimum value the JSPinner will accept
	* @param maxValue the maximum value the JSPinner will accept
	* @param stepValue the amount the JSpinner will increment or decrement 
	*/
	public DoubleJSpinner(double initValue, double minValue, double maxValue, double stepValue){
		
		super(new SpinnerNumberModel(initValue, minValue, maxValue, stepValue));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this);
		setEditor(editor);

	}
}