package edu.mst.distopsysproj.gui;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.person.Person;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class ReceiveLocationBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private int cntLocationGetter;
	private Map<String, Location> location;
	private BridgeCrossingFrame guiFrame;
	
	public ReceiveLocationBehaviour(BridgeCrossingFrame guiFrame) {
		super();
		cntLocationGetter = 0;
		location = new HashMap<String, Location>();
		this.guiFrame = guiFrame;
	}
	
	@Override
	public void action() {
		while(cntLocationGetter != Person.persons.length){
			ACLMessage reply = myAgent.receive();
			if (reply != null) {
				if(reply.getConversationId().equals(ProtocolConstants.INFORM_LOCATION_CONVID)){
					location.put(reply.getSender().getLocalName(), Location.valueOf(reply.getContent()));
					cntLocationGetter++;
				}
			} else block();
		}
		System.out.println(location);
		for (Iterator<String> iterator = location.keySet().iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			guiFrame.setCircleToPosition(id, location.get(id));
		}
		cntLocationGetter = 0;
		location.clear();
	}
}
