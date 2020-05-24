

package com.gussssy.orbitalsimulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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



	public OrbitalDisplay(OrbitalSimulator simulator, int width, int height){
		
		System.out.println("Initializing Display");

		this.simulator = simulator;
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

	public void displayToModelCoords(double x, double y){
		double displayModelRatio = 1/modelDisplayRatio;
		System.out.println("ModelDisplayRatio "+modelDisplayRatio);
		System.out.println("DisplayModelRatio "+displayModelRatio);

		double modelX = x-xOffset;
		modelX = modelX*displayModelRatio;

		double modelY = y-yOffset;
		modelY = modelY*displayModelRatio;

		System.out.println("Transformed Point x: "+modelX+", y: "+modelY);
	}

	public void centreDisplayAroundObject(Ob object){

		//if(!centered){
		//	return;
		//}

		//display coords must be set before centering, this ensure that is done
		//NOTE THIS MAY BE CALLED REDUNDANTLY WHICH I DONT LIKE 
		//modelToDisplayCoords();

		//System.out.println("Centrering the display around object: "+object.name);
		//System.out.println("Initial Display x and y for this object: " + object.dispX+ ", "+object.dispY);
		//System.out.println("Size of the Display: "+width+","+height);

		//find x and y center
		int centreX = width/2;
		int centreY = height/2;

		//System.out.println("The Centre is: "+centreX + ", "+centreY);

		//set offset to be the centre value - x and y display values of the centering object
		xOffset += centreX - (int)object.dispX;
		yOffset += centreY - (int)object.dispY;

		//System.out.println("xOffset: "+xOffset+" yOffset: "+yOffset);

		//set centeredObject DF value so that recentering is easy if the display scale or display size changes
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



	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Call modelToDisplayCoords to make sure display coords have been updated if model coords have chnaged
		modelToDisplayCoords();

		if(centered){
			centreDisplayAroundObject(centeredObject);
		}

		Color color = new Color(255,255,0);
		g.setColor(color);
		//g.fillOval(500,500,100,100);

		//draw the objects
		for(Ob o : simulator.model.getObjects()){
			g.setColor(o.color);
			g.fillOval((int)(o.dispX-o.size/2),(int)(o.dispY-o.size/2),(int)o.size,(int)o.size);
		}
		
		//draw the text
		int x = 550;
		int y = 450;
		color = new Color(255,255,255);
		g.setColor(color);
		Font font = new Font("Plain",Font.BOLD,10);
		g.setFont(font);
		//g.drawString("The Sun",x,y);
		//g.drawString("Mass: "+"330,000 Earth Mass", x, y+10);
		//g.drawString("Velocity: ?", x, y+20);

		g.drawString("Day: "+ simulator.model.getDay(), 0,10);

		for(Ob o : simulator.model.getObjects()){
			g.drawString(o.name,(int)(o.dispX+o.size/2),(int)o.dispY-10);
			//g.drawString("Mass: "+o.mass+" Earth Masses",(int)o.x,(int)o.y+10);
			g.drawString("Vx: "+nForm.format(o.vx)+"kmps",(int)(o.dispX+o.size/2),(int)o.dispY);
			g.drawString("Vy: "+nForm.format(o.vy)+"kmps",(int)(o.dispX+o.size/2),(int)o.dispY+10);
			//g.drawString("Gravitational Force: "+o.gForce+"Newtons",(int)(o.x+o.size/2),(int)o.y+20);

		}

		//System.out.println("Printing Display Coords");
		//
		//print object coords for debugging
		//for(Ob o : OrbitalSimulator.model.getObjects()){
		//	System.out.println(o.name+" dispX: "+o.dispX+", dispY: "+o.dispY);
		//}
		

	}

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

	private class DisplayMouseAdapter extends MouseAdapter{

	public void mouseClicked(MouseEvent e){
		System.out.println("Mouse Clicked");
		System.out.println(e.getPoint());
		System.out.println("X and Y offsets: "+xOffset+" ,"+yOffset);
		displayToModelCoords(e.getPoint().x,e.getPoint().y);
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