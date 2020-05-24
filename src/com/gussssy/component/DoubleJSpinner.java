package com.gussssy.component;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
* A JSpinner that uses doubles rather then int.
*  - also formats input.
* 
*  Adapted from: http://www.java2s.com/Tutorials/Java/Swing_How_to/JSpinner/Set_step_for_JSpinner_in_Double_format.htm
*/
public class DoubleJSpinner extends JSpinner{
	
	/**
	* The sole constructor for DoubleJSpinner. 
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
	
	
	
	/**
	 * Sets the width of the spinners text field.
	 * 
	 *  @param width: the width to set the spinner (in pixels)
	 **/
	public void setWidth(int width){
		
		JComponent field = ((JSpinner.DefaultEditor) this.getEditor());
		Dimension size = field.getPreferredSize();
		size = new Dimension(width, size.height);
		field.setPreferredSize(size);
	}
}