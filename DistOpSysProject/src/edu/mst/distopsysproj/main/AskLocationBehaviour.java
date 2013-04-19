package edu.mst.distopsysproj.main;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class AskLocationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	
	private String[] receivers;
	private int step;
	private int cntLocationGetter;
	
	public AskLocationBehaviour(Agent a, long period, String[] receivers) {
		super(a, period);
		this.receivers = receivers;
		this.step = 0;
		this.cntLocationGetter = 0;
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
					System.out.println(reply.getSender().getName() + " -> " + reply.getContent());
					cntLocationGetter++;
				}
			}
			step = 0;
			cntLocationGetter = 0;
			break;
		default:
			break;
		}
		
	}

}
