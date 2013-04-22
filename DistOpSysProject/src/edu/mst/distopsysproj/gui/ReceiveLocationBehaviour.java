package edu.mst.distopsysproj.gui;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.person.Person;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class ReceiveLocationBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private int cntLocationGetter;
	private Map<String, Location> location;
	
	public ReceiveLocationBehaviour() {
		super();
		cntLocationGetter = 0;
		location = new HashMap<String, Location>();
	}
	
	@Override
	public void action() {
		while(cntLocationGetter != Person.persons.length){
			ACLMessage reply = myAgent.receive();
			if (reply != null) {
				if(reply.getConversationId().equals(ProtocolConstants.INFORM_LOCATION_CONVID)){
					location.put(reply.getSender().getName(), Location.valueOf(reply.getContent()));
					cntLocationGetter++;
				}
			} else block();
		}
		//TODO atualizar tela
		System.out.println(location);
		cntLocationGetter = 0;
		location.clear();
		
	}

}
