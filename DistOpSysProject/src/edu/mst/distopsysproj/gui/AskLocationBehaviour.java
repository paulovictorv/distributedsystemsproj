package edu.mst.distopsysproj.gui;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class AskLocationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
	
	private String[] receivers;
	
	private BridgeCrossingFrame guiFrame;
	
	public AskLocationBehaviour(Agent a, long period, String[] receivers, BridgeCrossingFrame guiFrame) {
		super(a, period);
		this.receivers = receivers;
		this.guiFrame = guiFrame;
	}

	@Override
	protected void onTick() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		for (String target : receivers){
			msg.addReceiver(new AID(target, AID.ISLOCALNAME));
		}
		
		msg.setLanguage("English");
		msg.setContent(ProtocolConstants.INFORM_LOCATION_REQUEST);
		
		myAgent.send(msg);

		myAgent.addBehaviour(new ReceiveLocationBehaviour(guiFrame));
	}

}
