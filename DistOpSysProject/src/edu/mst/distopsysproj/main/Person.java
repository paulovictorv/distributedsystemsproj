package edu.mst.distopsysproj.main;

import jade.core.Agent;

public class Person extends Agent {
	private static final long serialVersionUID = 1L;

	public static String[] persons = {"person1", "person2", "person3", "person4"};
	
	private Location location;
	
	@Override
	protected void setup() {
		if(getName().contains("person1")
				|| getName().contains("person2")) this.location = Location.A;
		else if(getName().contains("person3")
				|| getName().contains("person4")) this.location = Location.B;
		
		addBehaviour(new ReceiveMessageBehaviour());
	}
	
	public Location getLocation() {
		return location;
	}
	
}
