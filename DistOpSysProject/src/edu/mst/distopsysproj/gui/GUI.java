package edu.mst.distopsysproj.gui;

import edu.mst.distopsysproj.person.Person;
import edu.mst.distopsysproj.util.ProtocolConstants;
import jade.core.Agent;

public class GUI extends Agent {
	private static final long serialVersionUID = -8484372606650826462L;

	@Override
	protected void setup() {
		System.out.println("Hello! " + getAID().getName() + " is ready!");
		
		addBehaviour(new AskLocationBehaviour(this, ProtocolConstants.ASK_LOCATION_INTERVAL, Person.persons));
		
	}
	
	@Override
	protected void takeDown() {
		System.out.println("Bye! " + getAID().getName() + " is shutting down!");
	}
}
