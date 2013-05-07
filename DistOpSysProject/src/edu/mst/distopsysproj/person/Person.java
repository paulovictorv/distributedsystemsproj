package edu.mst.distopsysproj.person;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import edu.mst.distopsysproj.util.Bridge;
import edu.mst.distopsysproj.util.ProtocolConstants;

public class Person extends Agent {
	private static final long serialVersionUID = 1L;

	public static String[] persons = {"person1", "person2", "person3", "person4"};

	private Bridge bridge;
	
	private Location lastLocation;
	private Location location;

	private Boolean tryCS, want, in;
	private Integer acksNumber;
	private long timestamp;
	private HashMap<String, Boolean> acksMap;

	@Override
	protected void setup() {
		tryCS = false;
		want = false;
		in = false;
		acksNumber = 0;
		acksMap = new HashMap<String, Boolean>();
		for (String p : persons) {
			acksMap.put(p, false);
		}
		
		bridge = Bridge.getInstance();

		if(getName().contains("person1")
				|| getName().contains("person2")){
			this.location = Location.A;
			this.lastLocation = Location.A;
		} else if(getName().contains("person3")
				|| getName().contains("person4")){
			this.location = Location.B;
			this.lastLocation = Location.B;
		}
		
		tryCS = true;

		ParallelBehaviour parBeh = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);
		parBeh.addSubBehaviour(new ReceiveMessageBehaviour());
		parBeh.addSubBehaviour(new CrossBridgeBehaviourAgrawala());
		addBehaviour(parBeh);
	}
	
	private class ReceiveMessageBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null){
				if (msg.getContent().equals(ProtocolConstants.INFORM_LOCATION_REQUEST)) {
					ACLMessage reply = msg.createReply();
					reply.setContent(location.toString());
					reply.setConversationId(ProtocolConstants.INFORM_LOCATION_CONVID);
					//System.out.println(myAgent.getLocalName() + " sent " + location.toString());
					myAgent.send(reply);
				}else myAgent.putBack(msg);
			}else block();
		}
		
	}

	private class CrossBridgeBehaviourAgrawala extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;
		private static final long BRIDGE_WAITING_TIME = 1500;

		@Override
		public void action() {
			if(tryCS){
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.setContent(ProtocolConstants.MSGTYPE_REQUEST);
				timestamp = System.currentTimeMillis();
				request.addUserDefinedParameter(ProtocolConstants.TIMESTAMP, String.valueOf(timestamp));
				for (String target : persons){
					if(!getName().contains(target)){							
						request.addReceiver(new AID(target, AID.ISLOCALNAME));
					}
				}
				send(request);
				tryCS = false;
				want = true;
			}
	
			if(acksNumber == persons.length-1){
				in = true;
				//process enters CS
				//System.out.println("Person " + myAgent.getLocalName() + " is on the bridge");
				bridge.enterBridge(myAgent.getLocalName());
				lastLocation = location;
				location = Location.BRIDGE; //TODO check if correct, also set person's last position
				want = false;
				try {
					Thread.sleep(BRIDGE_WAITING_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(in && !want){
				in = false;
				acksNumber = 0;
				
				//System.out.println("Person " + myAgent.getLocalName() + " left the bridge");
				//System.out.println("Current state of bridge is " + bridge);
				location = Location.getOppositeLocation(lastLocation); //TODO check if correct, also set person's last position
				bridge.leftBridge(myAgent.getLocalName(), location);
				
				ACLMessage ack = new ACLMessage(ACLMessage.REQUEST);
				ack.setContent(ProtocolConstants.MSGTYPE_ACK);
				ack.addUserDefinedParameter(ProtocolConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
				for (String p : acksMap.keySet()) {
					Boolean val = acksMap.get(p);
					if(val){
						ack.addReceiver(new AID(p, AID.ISLOCALNAME));
						val = false;
					}
				}
				send(ack);
				tryCS = true;
				try {
					Thread.sleep(BRIDGE_WAITING_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			ACLMessage msg = receive();
			if (msg != null){
				String content = msg.getContent();
				if(content.equals(ProtocolConstants.MSGTYPE_REQUEST)) {
					if(!want || compareTimestamps(msg, timestamp)){						
						ACLMessage ack = msg.createReply();
						ack.setContent(ProtocolConstants.MSGTYPE_ACK);
						ack.addUserDefinedParameter(ProtocolConstants.TIMESTAMP,
								String.valueOf(System.currentTimeMillis()));
						send(ack);
					}else{
						String sender = msg.getSender().getLocalName();
						acksMap.put(sender, true);
					}
				}else if(content.equals(ProtocolConstants.MSGTYPE_ACK)) {
					acksNumber = acksNumber + 1;
				}else{
					myAgent.putBack(msg);
				}
			}
		}
		
		private boolean compareTimestamps(ACLMessage msg, long localTimestamp){
			long msgTimestamp = Long.valueOf(msg.getUserDefinedParameter(ProtocolConstants.TIMESTAMP)).longValue();
			if ( msgTimestamp < localTimestamp ){
				return true;
			} else if( msgTimestamp == localTimestamp ){
				return getLocalName().compareTo(msg.getSender().getLocalName()) > 0;
			} else {
				return false;
			}
		}
	}
}
