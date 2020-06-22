

package com.gussssy.orbitalsimulator.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.gussssy.orbitalsimulator.Ob;
import com.gussssy.orbitalsimulator.OrbitalMath;
import com.gussssy.orbitalsimulator.OrbitalSimulator;

import java.text.DecimalFormat;

/**
* The Panel that displays the simulation.
*  
* Important Notes on operation: 
*		- whenever the display size or display scale is changed, modelToDisplayCoords must be called
*		  to adjust the display coordinates of the objects to fit properly into the altered display
*/
public class OrbitalDisplay extends JPanel{

	private OrbitalSimulator simulator;
	private OrbitalView view;
	
	//Panel Dimension Variables
	private int width;
	private int height;
	private Dimension dim;

	//used to format velocity and force on screen labels to sensible number of didgits
	DecimalFormat nForm = new DecimalFormat("#.00");

	//Scale and Offset variables:
	//  - model and display scales are independent, 
	private int modelScale;				//distance scale used by the model
	private int displayScale;			//km per pixel scale used by the display
	private double modelDisplayRatio;	//the ratio of the model and display scale, used to convert model coords to display coords
	private int xOffset = 0;			// how much the display offsets on the x axis
	private int yOffset = 0;			// how much the display is offset on the y axis
	private Ob centeredObject;
	private boolean centered = true;
	
	private boolean drawVelocityComponents = false;
	
	private Color textColor = new Color(255, 255, 255);
	Font font = new Font("Plain",Font.BOLD,10);
	
	//private Point dayLabelCoords = new Point();
	
	
	/** When true, will draw trails*/
	private boolean drawTrails = true;



	public void setDrawTrails(boolean drawTrails) {
		this.drawTrails = drawTrails;
	}


	public void setDrawVelocityComponents(boolean drawVelocityComponents) {
		this.drawVelocityComponents = drawVelocityComponents;
	}


	public OrbitalDisplay(OrbitalSimulator simulator, OrbitalView view, int width, int height){
		
		System.out.println("Initializing Display");

		this.simulator = simulator;
		this.view = view;
		this.width = width;
		this.height = height;

		//Initialize Scale Variables
		modelScale = simulator.model.getModelScale();
		displayScale = modelScale;	//for now model scale - display scale untill v2.0 is fully built to v1.2 level; fucntionality

		//Centre the display around the Sun:
		centreDisplayAroundObject(simulator.model.getObjects().get(0));

		//Initialize Display Coords (as they are initially zero)
		modelToDisplayCoords(); //must be done before centering I think

		//Initialize Action Listener for Button events
		DisplayListener dlistener = new DisplayListener();
		addComponentListener(dlistener);

		DisplayMouseAdapter dMouseAdapter = new DisplayMouseAdapter();
		addMouseMotionListener(dMouseAdapter);
		addMouseListener(dMouseAdapter);
		
		


	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Call modelToDisplayCoords to make sure display coords have been updated if model coords have chnaged
		modelToDisplayCoords();

		if(centered)centreDisplayAroundObject(centeredObject);
				

		// WIP DRAW TRAILS
		
		if(drawTrails){

			for(TrailNode node : view.trailManager.nodes){
				g.setColor(node.color);
				g.fillOval((int)(node.x-node.size/2),(int)(node.y-node.size/2),node.size,node.size);
			}
		}
		
		
		
		
		// DRAW OBJECTS
		for(Ob o : simulator.model.getObjects()){
			g.setColor(o.color);
			g.fillOval((int)(o.dispX-o.size/2),(int)(o.dispY-o.size/2),(int)o.size,(int)o.size);
		}
		
		
		// DRAW TEXT
		// Set font and color
		g.setColor(textColor);
		g.setFont(font);
		
		// Draw the Day in top left of the panel
		g.drawString("Day: "+ simulator.model.getDay(), 0,10);

		// Draw Object properties
		for(Ob o : simulator.model.getObjects()){
			g.drawString(o.name,(int)(o.dispX+o.size/2),(int)o.dispY-10);
			
			if(drawVelocityComponents){
				g.drawString("Vx: "+nForm.format(o.vx)+"kmps",(int)(o.dispX+o.size/2),(int)o.dispY);
				g.drawString("Vy: "+nForm.format(o.vy)+"kmps",(int)(o.dispX+o.size/2),(int)o.dispY+10);	
			}else{
				OrbitalMath.setTotalVelocity(o);
				g.drawString("v: "+nForm.format(o.v)+"kmps",(int)(o.dispX+o.size/2),(int)o.dispY);
			}
		}
	}



	/** Model To Display Coordinates
	*
	*	- takes the model coords and converts to display coords, 
	*	- stores the display coords in each Ob objects datafield
	*	- model coords are doubles, display coords are integer
	*	- the scale of the display will change at runtime, model scale is constant
	*	- allows the display scale to be independent of the model scale
	*/
	public void modelToDisplayCoords(){
		//modelScale = (double)OrbitalSimulator.model.getModelScale();

		modelDisplayRatio = (double)modelScale/(double)displayScale;
		//System.out.println("modelScale(int): "+ modelScale );
		//System.out.println("displayScale(int): "+ displayScale);
		//System.out.println("modelDisplayRatio: "+ modelDisplayRatio);

		for(Ob o : simulator.model.getObjects()){

			o.dispX = o.x*modelDisplayRatio + xOffset;
			o.dispY = o.y*modelDisplayRatio + yOffset;
			//System.out.println("Display Coords: dispX = "+o.dispX+", dispY = "+o.dispY);
		}

	}
	
	/**
	 * planned rework of coordinate conversion so trail nodes and objects use same operations
	 * 
	 * modelToDisplayCoordsSingleObject(DrawableObject object, modelX, modelY, dispX, yDisp, xOffset, yOffset modelRatio, displayRatio) 
	 **/

	/**
	 * This method is only used for testing. 
	 * Takes a single point via x and y value e.g the position of the sun in the display, and uses the reverse operation of model to display coords 
	 * and then prints out the transformed x an y values. 
	 * The printed out values should match the original model coords before they were converted to display coords.... 
	 * ... so far this has always been the case.
	 * 
	 **/
	public String displayToModelCoords(double x, double y){
		double displayModelRatio = 1/modelDisplayRatio;
		System.out.println("ModelDisplayRatio "+modelDisplayRatio);
		System.out.println("DisplayModelRatio "+displayModelRatio);

		double modelX = x-xOffset;
		modelX = modelX*displayModelRatio;

		double modelY = y-yOffset;
		modelY = modelY*displayModelRatio;
		
		return "Transformed Point x: "+modelX+", y: "+modelY;

		//System.out.println("Transformed Point x: "+modelX+", y: "+modelY);
	}

	
	/**
	 * Centers the display around an Object 
	 * 
	 * @param object: the object that the display will ensure is always in the center
	 **/
	public void centreDisplayAroundObject(Ob object){

		//System.out.println("Centrering the display around object: "+object.name);
		//System.out.println("Initial Display x and y for this object: " + object.dispX+ ", "+object.dispY);
		//System.out.println("Size of the Display: "+width+","+height);

		//find x and y center coordinates
		int centreX = width/2;
		int centreY = height/2;

		//System.out.println("The Center is: "+centreX + ", "+centreY);

		//set offset to be the center value - x and y display values of the centering object
		xOffset += centreX - (int)object.dispX;
		yOffset += centreY - (int)object.dispY;

		//System.out.println("xOffset: "+xOffset+" yOffset: "+yOffset);

		//set centeredObject property to the object we just centered so recentering is easy if the display scale or display size changes
		centeredObject = object;
	}

	public void dontCenterDisplay(){
		centered = false;
	}

	public void centerDisplay(){
		centered = true;
	}

	public void panLeft(){
		centered = false;
		xOffset += 50;
	}

	public void panRight(){
		centered = false;
		xOffset -= 50;
	}

	public void panUp(){
		centered = false;
		yOffset += 50;
	}

	public void panDown(){
		centered = false;
		yOffset -= 50;
	}

	public void increaseModelScale(){

		System.out.println("Initial Model Scale: "+ modelScale);

		//increase model scale by 10% 
		modelScale += (double)modelScale *0.1;

		System.out.println("Modified Model Scale: "+ modelScale);

		
		modelToDisplayCoords();

		//when the sclae is changed, need to recenter
		centreDisplayAroundObject(centeredObject);
	}

	public void decreaseModelScale(){

		System.out.println("Initial Model Scale: "+ modelScale);

		//increase model scale by 10% 
		modelScale -= (double)modelScale *0.1;

		System.out.println("Modified Model Scale: "+ modelScale);

		// Reset display coords contained in each object.
		modelToDisplayCoords();

		//when the sclae is changed, need to recenter
		centreDisplayAroundObject(centeredObject);
	}



	
	
	/** 
	 * When the size of the panel changes this method is called to reorganize the contents to fit into the resized panel
	 */
	private void updateSize(){
		Dimension size = getSize();
		width = size.width;
		height = size.height;
		centreDisplayAroundObject(simulator.model.getObjects().get(0));
		
	}
	
	public void reset(){}

	private class DisplayListener implements ComponentListener{

		public DisplayListener(){
			System.out.println("Initializing Display Listner");
		}

		public void componentResized(ComponentEvent e){
			System.out.println(e.getComponent().getClass().getName()+" Panel has been resized");
			//this has access to shit from contianing class
			updateSize();


		}

		public void componentHidden(ComponentEvent e){
			System.out.println(e.getComponent().getClass().getName()+" Panel has been hidden");

		}

		public void componentShown(ComponentEvent e){
			System.out.println(e.getComponent().getClass().getName()+" Panel has been shown");

		}

		public void componentMoved(ComponentEvent e){
			System.out.println(e.getComponent().getClass().getName()+" Panel has been moved");

		}
	}

	
	/**
	 * Inner Class to handle mouse events within the display panel. 
	 * 
	 *  Currently does nothing useful. 
	 **/
	private class DisplayMouseAdapter extends MouseAdapter{

		/**
		 * When the mouse is clicked on the display panel, the location of the click is printed to the console.
		 * The location is then copverted from display coords to model coords which are then printed to the console also.  
		 **/
		public void mouseClicked(MouseEvent e){
			//System.out.println("Mouse Clicked");
			System.out.println("Mouse Clicked at: " + e.getPoint());
			System.out.println("X and Y offsets: "+xOffset+" ,"+yOffset);
			System.out.println("Location of click in the model space: " + displayToModelCoords(e.getPoint().x,e.getPoint().y));
		}

		public void mouseDragged(MouseEvent e){
			System.out.println("Mouse Dragged");
		}

		public void mouseEntered(MouseEvent e){
			System.out.println("Mouse Entered");
		}

		public void mouseExited(MouseEvent e){
			System.out.println("Mouse Exited");
		}

		public void mouseMoved(MouseEvent e){
			//System.out.println("Mouse Moved");
		}

		public void mousePressed(MouseEvent e){
			System.out.println("Mouse Pressed...?");
		}

		public void mouseReleased(MouseEvent e){
			System.out.println("Mouse Released");
		}

		public void mouseWheelMoved(MouseEvent e){
			System.out.println("Mouse Wheel Moved");
		}

	}

}