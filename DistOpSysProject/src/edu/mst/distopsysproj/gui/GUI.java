package edu.mst.distopsysproj.gui;

import jade.core.Agent;
import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.person.Person;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class GUI extends Agent {
	private static final long serialVersionUID = -8484372606650826462L;
	
	private BridgeCrossingFrame frame;

	@Override
	protected void setup() {
		frame = new BridgeCrossingFrame(Person.persons);
		
		addBehaviour(new AskLocationBehaviour(this,
				ProtocolConstants.ASK_LOCATION_INTERVAL, Person.persons, frame));
	}
	
	@Override
	protected void takeDown() {
		frame.dispose();
		System.out.println("Bye! " + getAID().getName() + " is shutting down!");
	}
}
