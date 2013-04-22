package edu.mst.distopsysproj.person;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import edu.mst.distopsysproj.util.ProtocolConstants;

public class Person extends Agent {
	private static final long serialVersionUID = 1L;

	public static String[] persons = {"person1", "person2", "person3", "person4"};

	private Location location;

	private Boolean tryCS, want, in;
	private Integer acksNumber;
	private Long timestamp;
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

		if(getName().contains("person1")
				|| getName().contains("person2")) this.location = Location.A;
		else if(getName().contains("person3")
				|| getName().contains("person4")) this.location = Location.B;
		
		if(getName().contains("person1")) tryCS = true;

		addBehaviour(new ReceiveMessageBehaviour());
	}

	public Location getLocation() {
		return location;
	}

	private class ReceiveMessageBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage msg = receive();
			if (msg != null){
				if (msg.getContent().equals(ProtocolConstants.INFORM_LOCATION_REQUEST)) {
					ACLMessage reply = msg.createReply();
					reply.setContent(getLocation().toString());
					reply.setConversationId(ProtocolConstants.INFORM_LOCATION_CONVID);
					send(reply);
				}else{
					if(tryCS){
						System.out.println("Person " + myAgent.getLocalName() + " has try = true");
						ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
						request.setContent(ProtocolConstants.MSGTYPE_REQUEST);
						timestamp = System.currentTimeMillis();
						request.addUserDefinedParameter(ProtocolConstants.TIMESTAMP, String.valueOf(timestamp));
						for (String target : persons){
							if(!getName().contains(target)){							
								msg.addReceiver(new AID(target, AID.ISLOCALNAME));
							}
						}
						send(request);
						tryCS = false;
						want = true;
					}

					if(msg.getContent().equals(ProtocolConstants.MSGTYPE_REQUEST)) {
						if(!want || Long.valueOf(msg.getUserDefinedParameter(ProtocolConstants.TIMESTAMP)) < timestamp){						
							ACLMessage ack = msg.createReply();
							ack.setContent(ProtocolConstants.MSGTYPE_ACK);
							ack.addUserDefinedParameter(ProtocolConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
							send(ack);
						}else{
							String sender = msg.getSender().getLocalName();
							acksMap.put(sender, true);
						}
					}
					
					if(msg.getContent().equals(ProtocolConstants.MSGTYPE_ACK)) {
						acksNumber = acksNumber + 1;
					}
					
					if(acksNumber == persons.length-1){
						in = true;
						//process enters CS
						System.out.println("Person " + myAgent.getLocalName() + " is on the bridge");
						want = false;
					}
					
					if(in && !want){
						in = false;
						acksNumber = 0;
						System.out.println("Person " + myAgent.getLocalName() + " left the bridge");
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
					}
				}

			}else block();
		}
	}



}
