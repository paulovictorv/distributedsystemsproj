package edu.mst.distopsysproj.gui;

import java.awt.Color;

import edu.mst.distopsysproj.person.Location;

public class Circle {

	private String id;
	private Color color;
	private Location position;
	private Integer x;
	private Integer y;
	
	public Circle(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Location getPosition() {
		return position;
	}
	
	public void setPosition(Location position) {
		this.position = position;
	}
	
	public Integer getX() {
		return x;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}
	
	public Integer getY() {
		return y;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}

}
