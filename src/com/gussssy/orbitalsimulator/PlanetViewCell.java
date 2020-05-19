package com.gussssy.orbitalsimulator;
	
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** Planet View Cell
*
*	An individual cell that will display within the PlanetControlPanel and PlanetBuilderPanel
*
*/
public class PlanetViewCell extends JPanel{

	/** The object that will be displayed*/
	private Ob object;

	/**Dimension controlling the size of the panel*/
	private Dimension dims = new Dimension(100,100);
	
	
	/** x location of the label*/
	private int textX = dims.width/2;
	/** y location of the label*/
	private int textY = dims.height/10; //text alligned with senter of cell
	
	
	/**The size the object when drawn, set during initalization*/
	private int planetSize;
	/**The maximum size the object can be drawn within the cell*/
	private int maxPlanetSize = 80;
	
	
	/**Whether or not the planet name is to bre printed in the cell, true by default*/
	private boolean drawLabel = true;
	/**Controls the drawing of rectangular outline, true by default*/
	private boolean drawOutline = true;



	/**  
	* The only constructor.
	* @param object to be displayed
	*/
	public PlanetViewCell(Ob object){
		
		setPreferredSize(dims);
		setObject(object);		
	}

	
	/**
	* Turn the drawing of labels on or off.
	* @param drawLabel boolean that specifies if the label is to be drawn or not
	*/
	public void drawLabel(boolean drawLabel){
		this.drawLabel = drawLabel;
	}

	

	/**
	* Set drawing of an outline to be true pr false
	* @param drawOutline boolen specifying if an outline will be drawn or not 
	*/
	public void drawOutline(boolean drawOutline){
		this.drawOutline = drawOutline;
	}



	/**
	* Changes the object in the planet view cell so a different object can be displayed
	* @param newObject the new object to be displayed
	*/
	public void setObject(Ob newObject){
		object = newObject;
		//BUG: call to repaint leads to OrbitalDisplay paintcomponent being called as well which ruins this
		//repaint();
		setPlanetDisplaySize();
	}

	
	/**
	* Sets the size the Object/Planet drawn in the cell.
	*
	* Size is determined by the Objects size up to a max size to fit into the panel correctly.
	*/
	private void setPlanetDisplaySize(){

		if(object.size > maxPlanetSize){
			planetSize = 80;

		}else{
			planetSize = (int)object.size;
		}
	}

	

	/**
	* Paints the Object. Also optionally paints an outline and label. 
	*/
	public void paintComponent(Graphics g){

		//Draw Label
		if(drawLabel){
		Font font = new Font("Plain",Font.BOLD,10);
		g.setFont(font);
		g.drawString(object.name,textX,textY);
	}

		//Draw the outline of cell
		if(drawOutline){
		g.setColor(Color.black);
		g.drawRect(0,0,95,95);
	}

		//Draw Object
		g.setColor(object.color);
		g.fillOval((dims.width/2)-(planetSize/2),(dims.height/2)-(planetSize/2),planetSize,planetSize);
	}
}