package edu.mst.distopsysproj.gui;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.person.Person;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class AskLocationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	
	private String[] receivers;
	private int step;
	private int cntLocationGetter;
	private Map<String, Location> location;
	
	public AskLocationBehaviour(Agent a, long period, String[] receivers) {
		super(a, period);
		this.receivers = receivers;
		this.step = 0;
		this.cntLocationGetter = 0;
		this.location = new HashMap<String, Location>();
	}

	@Override
	protected void onTick() {
		switch (step) {
		case 0:			
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			
			for (String target : receivers){
				msg.addReceiver(new AID(target, AID.ISLOCALNAME));
			}
			
			msg.setLanguage("English");
			msg.setContent(ProtocolConstants.INFORM_LOCATION_REQUEST);
			
			System.out.println("Sent Request: " + msg.getContent());
			
			myAgent.send(msg);
			step++;
			break;
		case 1:
			while(cntLocationGetter != Person.persons.length){
				ACLMessage reply = myAgent.receive();
				if(reply.getConversationId().equals(ProtocolConstants.INFORM_LOCATION_CONVID)){
					location.put(reply.getSender().getName(), Location.valueOf(reply.getContent()));
					cntLocationGetter++;
				}
			}
			//TODO atualizar tela
			System.out.println(location);
			step = 0;
			cntLocationGetter = 0;
			location.clear();
			break;
		default:
			break;
		}
		
	}

}
