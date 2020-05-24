package com.gussssy.orbitalsimulator;

import java.lang.Math;

/**
* Static Utility Class
*	
* Calculate graviational force and resulting velocity chnages on Ob objects  
*/
public class OrbitalMath{

	
	public static boolean test = false;

	//Math Variables
	private static final double KM_IN_AU = 150000000;
	private static final double G = 6.67408*Math.pow(10,-11);
	private static final double EARTH_MASS = 5.972*Math.pow(10,24);



	/** GET GRAVITATIONAL FORCE
	*	
	*	Get the gravitational force between two bodies
	*
	*	F = Gm1m2/r^2

	*/
	public static double getGravitationalForce(Ob body1, Ob body2){
		double force;
		double radius;

		//Determine the radius and convert from million km to km
		radius = Math.sqrt(Math.pow(body2.x-body1.x,2)+Math.pow(body2.y-body1.y,2))*Math.pow(10,9);
		//System.out.println("Radius between "+body1.name+" and "+body2.name+" is "+radius);

		// Calculate Gravitational force, need to convert from EarthMasses to kg.
		force = (body1.mass*EARTH_MASS*body2.mass*EARTH_MASS)/Math.pow(radius,2)*G;
		//System.out.println("Force between "+body1.name+" and "+body2.name+" is "+force);

		body2.gForce = force;

		return force;
		//F = Gm1m2/r^2
	}

	

	/** UPDATE VELOCITY
	*
	*	updates the velocity of a body by calculating the change in velocity resulting from gravitational force over 24 hours
	*/
	public static void updateVelocity(Ob body1, Ob body2){

		double force = getGravitationalForce(body1,body2);
		if(test){System.out.println("Gravitational Force on "+body1.name+" from "+body2.name+": "+force);}
		double acceleration = force/(body2.mass*EARTH_MASS);
		if(test){System.out.println("Acceleration on "+body2.name+" from "+body1.name+": "+acceleration);}

		double theta = getTheta(body1, body2);

		//ax = acceleration on the x axis 
		double ax = acceleration*Math.sin(Math.toRadians(theta));
		if(test){System.out.println("ax: "+ ax+"m/s^2");}

		//ay = acceleration on the y axis
		double ay = acceleration*Math.cos(Math.toRadians(theta));
		if(test){System.out.println("ay: "+ ay+"m/s^2");}


		//GIVE THE ACCELERATION VECTORS CORRECT SIGNS DEPENDING ON THE QUADRANT THE BODY IS IN

		int quad = getQuadrant(body1,body2);
		if(test){System.out.println(body2.name+" is in quad: "+quad+" relative to "+body1.name);}

		if(quad == 1){
			//ax and ay should be both positve
			// suns pull is positive on both axis
		}
		
		if(quad == 2){
			// ax is now negative, y is still positive
			ax = -ax;
		}
		
		if(quad == 3){
			//ax is still negative, ay is now negative too
			ax = -ax;
			ay = -ay;
		}
		
		if(quad == 4){
			ay = -ay;
		}
		
		
		// conversion to acceleration per day
			// x axis
		ax = ax*60*60*24; // convert from m/s^2 to m/day^2 
		if(test){System.out.println("x Acceleration : "+ax+"m/day^2 ");}
		ax = ax/1000; //convert from  m/day^2 to km/day^2
		if(test){System.out.println("x Acceleration : "+ax+"km/s ");}
		
			// y axis
		ay = ay*60*60*24; // convert from m/s^2 to m/day^2 
		if(test){System.out.println("y Acceleration : "+ay+"m/day^2 ");}
		ay = ay/1000; //convert from  m/day^2 to km/day^2
		if(test){System.out.println("y Acceleration : "+ay+"km/day^2 ");}

		// udate velocity by adding the acceleration resulting from 24 hours of gravitaional force
		body2.vx += ax;
		body2.vy += ay;
	}

	/* Update Positions 
	*
	*	Updates the position of the body
	*	- velocity is in km/s
	*	- location is given in units of million km (mkm)
	*	
	*	@param body: the object that is having its position updated in the model space
	*/
	public static void updatePositions(Ob body){

		//System.out.println("Updating Position of " + body.name);
		//System.out.println("Current Velocity: vx = "+ body.vx+", vy = "+body.vy+ "(km/s)");
		//System.out.println("Initial Position: x = "+ body.x+", y = "+body.y);

		//double initX = body.x;
		//double initY = body.y;

		body.x += body.vx*60*60*24/1000000; 
		body.y += body.vy*60*60*24/1000000;

		//double deltaX = initX - body.x;
		//double deltaY = initY - body.y;
		//double deltaSum = deltaX+deltaY;

		//System.out.println("Change in Position: x = "+ deltaX +", y = "+deltaY);
		//System.out.println("New Position after 1 day: x = "+ body.x+", y = "+body.y);
		//System.out.println("Sum of x and y change:  "+deltaSum);

	}




	/** GET THETA
 	*  get the angle (theta) between:
 	*		- the child and its parent,   radius)
	*		- the child and the y axis
	*
	*  This is done by 
	*	1. calculating first x distance between the child anbd parent, (The OPPOSITE)
	*   2. then calculating the radius (The HYPOTENUSE)
	*   3. then using sin^-1(O/H)
	*/
	public static double getTheta(Ob parent, Ob child ){
		
		// O = x distance
		double opposite = child.x - parent.x; //used
		double adjacent = child.y - parent.y; // not used
		
		// H = radius
		double hypotenuse = getDistanceBetween(parent,child);
		
		// Theta = sin-1(0/H) 
		double theta = Math.asin(opposite/hypotenuse);
		theta = Math.toDegrees(theta);

		//System.out.println("Calculating Theta between: "+parent.name+" and "+child.name);
		if(test){
			System.out.println("Opposite: "+opposite);
			System.out.println("Hypotenuse: "+hypotenuse);
			System.out.println("Adjacent: "+adjacent);
			System.out.println("Theta: "+theta);
		}

		theta = Math.abs(theta);

		return theta;
		
	}



	/**  
	* Find the distance beween two bodies, given in millions of kilometres.
	* 
	* @return the distance between the two given bodies
	*/
	public static double getDistanceBetween(Ob body1, Ob body2){
		double radius = Math.sqrt(Math.pow(body2.x-body1.x,2)+Math.pow(body2.y-body1.y,2));
		return radius;
	}


	/**
	 * Gets the quadrant the 'distant' object is currently located in relative to the 'center' objects location.
	 * 
	 *  	above and to the left:	quadrant 1,
	 *		above and to the right:	quadrant 2,
	 *  	below and to the right:	quadrant 3,
	 *  	below and to the left:	quadrant 4,
	 *  
	 *  It is important to know which quadrant the body in because this changes how vectors are calculated using the approach I have choosen.
	 *  
	 *  @param center	the object that will be considered at the origin
	 *  @param distant	the object whose reative location to the center object we are interested in
	 *  @return 		an int between 1 and 4 describing the relative location of the distant object to the center object
	 **/
	public static int getQuadrant(Ob center, Ob distant){
		double xDistance = distant.x - center.x;
		double yDistance = distant.y - center.y;

		//If xDistance is positive
		if(xDistance > 0){
			// X Distance is Positive. Must be in quad 2 or 3
			if(yDistance > 0){
				//X and Y is positive, must be quad 3
				return 3;
			}else{
				//Y is positive y is negative, must be quad 2
				return 2;
			}
		
		}else{
			//X Distance is negative Must be quad 1 or 4
			if(yDistance > 0){
				// X distance is negative, Y distance is positive Quad 4
				return 4;
			}
			
			//X and Y distance is negative, Quad 1
			return 1;
		}

	}

}