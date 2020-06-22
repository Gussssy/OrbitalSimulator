package com.gussssy.orbitalsimulator.view;

import java.awt.Color;

public class TrailNode {
	
	private boolean active = true;
	public double x;
	public double y;
	public Color color;
	public int lifeTime;
	public int size = 5;
	public int originObjectSize;
	
	
	
	
	public TrailNode(double x, double y, Color color, int lifeTime){
		
		this.x = x;
		this.y = y;
		this.color = color;
		this.lifeTime = lifeTime;
		
	}
	

}
