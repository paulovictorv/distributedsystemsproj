package edu.mst.distopsysproj.gui;

import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.person.Person;
import jade.core.Agent;

public class GUI extends Agent {
	private static final long serialVersionUID = -8484372606650826462L;
	
	private BridgeCrossingFrame frame;

	@Override
	protected void setup() {
		frame = new BridgeCrossingFrame(Person.persons);
		
		frame.setCircleToPosition("person1", Location.BRIDGE);
		frame.setCircleToPosition("person1", Location.B);
		frame.setCircleToPosition("person3", Location.BRIDGE);
		frame.setCircleToPosition("person3", Location.B);
		frame.setCircleToPosition("person2", Location.BRIDGE);
		
		//addBehaviour(new AskLocationBehaviour(this, ProtocolConstants.ASK_LOCATION_INTERVAL, Person.persons));
	}
	
	@Override
	protected void takeDown() {
		frame.dispose();
		System.out.println("Bye! " + getAID().getName() + " is shutting down!");
	}
}
