package edu.mst.distopsysproj.main;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class AskLocationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	
	private String[] receivers;
	
	public AskLocationBehaviour(Agent a, long period, String[] receivers) {
		super(a, period);
		this.receivers = receivers; 
	}

	@Override
	protected void onTick() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		for (String target : receivers){
			msg.addReceiver(new AID(target, AID.ISLOCALNAME));
		}
		
		msg.setLanguage("English");
		msg.setContent(ProtocolConstants.INFORM_LOCATION_REQUEST);
		
		System.out.println("Sent Request: " + msg.getContent());
		
		myAgent.send(msg);
		
	}

}
