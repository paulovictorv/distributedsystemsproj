package edu.mst.distopsysproj.gui;

import jade.core.behaviours.OneShotBehaviour;

import java.util.HashMap;
import java.util.Map;

import edu.mst.distopsysproj.person.Location;
import edu.mst.distopsysproj.util.Bridge;

public class ReceiveLocationBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private int cntLocationGetter;
	private Bridge bridge;
	private Map<String, Location> location;
	private BridgeCrossingFrame guiFrame;
	
	public ReceiveLocationBehaviour(BridgeCrossingFrame guiFrame) {
		super();
		cntLocationGetter = 0;
		location = new HashMap<String, Location>();
		this.guiFrame = guiFrame;
		this.bridge = Bridge.getInstance();
	}
	
	@Override
	public void action() {
		/*while(cntLocationGetter != Person.persons.length){
			ACLMessage reply = myAgent.receive();
			if (reply != null) {
				if(reply.getConversationId().equals(ProtocolConstants.INFORM_LOCATION_CONVID)){
					location.put(reply.getSender().getLocalName(), Location.valueOf(reply.getContent()));
					cntLocationGetter++;
				}
			} else block();
		}*/
		/*System.out.println(location.toString()
				+ '\n' + "GUI READ THE BRIDGE: " + Bridge.getInstance().toString());*/
		String[] data = bridge.pollBridge();
		if(data != null){			
			guiFrame.setCircleToPosition(data[0], Location.valueOf(data[1]));
		}
		/*cntLocationGetter = 0;
		location.clear();*/
	}
}
