package edu.mst.distopsysproj.person;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import edu.mst.distopsysproj.util.Bridge;
import edu.mst.distopsysproj.util.ProtocolConstants;

/**
 * Class <code>Person</code> that extends from JADE's <code>Agent</code>
 * will represent each participant in the Ricart-Agrawala's protocol
 * implementation, being allowed to cross the 'bridge', that represents
 * the critical-section following the rules of mutual exclusion.<br /><br />
 * 
 * A parameter sent as argument in command-line will define the type of
 * protocol, if <code>false</code>, only one Person will be able to cross
 * the bridge at each time. If <code>true</code>, two persons will be able
 * to cross the bridge at each time, but only if they came from the same side
 * of the river.
 * 
 * @author Julio Zynger, Paulo Victor Melo
 */
public class Person extends Agent {
	private static final long serialVersionUID = 1L;

	/**
	 * Array of ids of each <code>Person</code> participating in
	 * the mechanism's execution.
	 */
	public static String[] persons = { "person1", "person2", "person3", "person4" };

	/**
	 * Single instance of the bridge, accessible at any time in the computation
	 */
	private Bridge bridge;
	
	/**
	 * Variable to store the <code>lastLocation</code> of this <code>Person</code>,
	 * so the entering in the critical-section can be defined in the protocol with
	 * multiple-visitors in the bridge at once.
	 */
	private Location lastLocation;
	/**
	 * Current {@link Location} of this {@link Person}.
	 */
	private Location location;

	private Boolean tryCS, want, in;
	private Integer acksNumber;
	private long timestamp;
	private HashMap<String, Boolean> acksMap;

	@Override
	protected void setup() {
		tryCS = true;
		want = false;
		in = false;
		acksNumber = 0;
		acksMap = new HashMap<String, Boolean>();
		for (String p : persons) {
			acksMap.put(p, false);
		}
		
		bridge = Bridge.getInstance();

		/**
		 * Set the initial position of each {@link Person} in the
		 * GUI's canvas.
		 */
		if(getName().contains("person1")
				|| getName().contains("person2")){
			this.location = Location.A;
			this.lastLocation = Location.A;
		} else if(getName().contains("person3")
				|| getName().contains("person4")){
			this.location = Location.B;
			this.lastLocation = Location.B;
		}
		
		/**
		 * Get the arguments passed to the agents that determine
		 * what type of protocol to use. Reading <code>true</code>
		 * will allow two people in the bridge at once, and only
		 * one otherwise.
		 */
		Object[] args = getArguments();
		boolean twoVisitors = Boolean.valueOf(args[0].toString()).booleanValue();
		
		addBehaviour(new CrossBridgeBehaviourAgrawala(twoVisitors));
	}

	/**
	 * {@link CyclicBehaviour} that will implement the
	 * Ricart-Agrawala's protocol in this application.
	 * The behavior will treat the message passing between the
	 * {@link Person}s and the communication between them and
	 * the {@link Bridge}.
	 * 
	 * @author Julio Zynger, Paulo Victor Melo
	 *
	 */
	private class CrossBridgeBehaviourAgrawala extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		/**
		 * Constant that defines the time the thread will be frozen
		 * when entering and leaving the critical-section. Commonly
		 * used only for testing purposes.
		 */
		private static final long BRIDGE_WAITING_TIME = 1500;

		/**
		 * <code>true</code> if two visitors can be at the bridge at once,
		 * <code>false</code> otherwise
		 */
		private boolean twoVisitors;
		
		public CrossBridgeBehaviourAgrawala(boolean twoVisitors) {
			this.twoVisitors = twoVisitors;
		}
		
		@Override
		public void action() {
			if(tryCS){
				// process request to enter the critical-section
				// sending messages to every other process involved
				// in the system.
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.setContent(ProtocolConstants.MSGTYPE_REQUEST);
				request.addUserDefinedParameter(
						ProtocolConstants.TWOVISITORSPROTOCOL_LASTLOCATION,
						lastLocation.toString());
				timestamp = System.currentTimeMillis();
				request.addUserDefinedParameter(ProtocolConstants.TIMESTAMP,
						String.valueOf(timestamp));
				for (String target : persons){
					// will only send messages to agents different than himself
					if(!getName().contains(target)){							
						request.addReceiver(new AID(target, AID.ISLOCALNAME));
					}
				}
				send(request);
				tryCS = false;
				want = true;
			}
	
			if(acksNumber == persons.length-1){
				// process enters CS
				in = true;
				// asks the bridge to add himself to the queue
				bridge.enterBridge(myAgent.getLocalName());
				// sets the new location variables
				lastLocation = location;
				location = Location.BRIDGE;
				
				want = false;
				try {
					Thread.sleep(BRIDGE_WAITING_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(in && !want){
				// process left CS - reset variables and locations
				in = false;
				acksNumber = 0;
				location = Location.getOppositeLocation(lastLocation);
				bridge.leftBridge(myAgent.getLocalName(), location);
				
				// send ack to the buffered request to keep
				// the protocol going
				ACLMessage ack = new ACLMessage(ACLMessage.REQUEST);
				ack.setContent(ProtocolConstants.MSGTYPE_ACK);
				ack.addUserDefinedParameter(ProtocolConstants.TIMESTAMP,
						String.valueOf(System.currentTimeMillis()));
				for (String p : acksMap.keySet()) {
					Boolean val = acksMap.get(p);
					if(val){
						ack.addReceiver(new AID(p, AID.ISLOCALNAME));
						val = false;
						acksMap.put(p, val);
					}
				}
				send(ack);
				
				tryCS = true;
				try {
					Thread.sleep(BRIDGE_WAITING_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ACLMessage msg = receive();
			if (msg != null){
				String content = msg.getContent();
				
				if(content.equals(ProtocolConstants.MSGTYPE_REQUEST)) {
					// if a request was received, this agent will need
					// to check the priority of the processes to define
					// who is going to enter the CS
					String locString = msg.getUserDefinedParameter(
							ProtocolConstants.TWOVISITORSPROTOCOL_LASTLOCATION);
					Location senderLastLocation = Location.valueOf(locString);
					
					if(!want || compareTimestamps(msg, timestamp)){
						sendAck(msg);
					}else{
						// if two visitors are allowed on the bridge (depending
						// on the chosen protocol), the last location of the requester
						// must be the checked to be the same as this agents',
						// otherwise they are not allowed to enter the bridge
						if(twoVisitors && senderLastLocation == lastLocation){
							sendAck(msg);
						}else{
							// if not allowed to enter the bridge, buffer the request
							String sender = msg.getSender().getLocalName();
							acksMap.put(sender, true);
						}
					}
				}else if(content.equals(ProtocolConstants.MSGTYPE_ACK)) {
					// if a message received was an ACK, increment the counter
					// to perform a later check when trying to enter the CS
					acksNumber = acksNumber + 1;
				}else{
					// if the message was neither a request nor an ACK,
					// put it back on the buffer so it can be read later
					myAgent.putBack(msg);
				}
			}
		}
		
		/**
		 * Build an ACK reply to a given {@link ACLMessage} and send it
		 * @param msg Message to reply to
		 */
		private void sendAck(ACLMessage msg) {
			ACLMessage ack = msg.createReply();
			ack.setContent(ProtocolConstants.MSGTYPE_ACK);
			ack.addUserDefinedParameter(ProtocolConstants.TIMESTAMP,
					String.valueOf(System.currentTimeMillis()));
			ack.addUserDefinedParameter(
					ProtocolConstants.TWOVISITORSPROTOCOL_LASTLOCATION,
					lastLocation.toString());
			send(ack);
		}
		
		/**
		 * Check to see which is the priority process to enter the CS
		 * based on their timestamp.
		 * 
		 * @param msg Message whose timestamp will be checked
		 * @param localTimestamp Timestamp of the local process
		 * @return <code>true</code> if this {@link Person}'s priority is
		 * higher, or <code>false</code> otherwise.
		 */
		private boolean compareTimestamps(ACLMessage msg, long localTimestamp){
			long msgTimestamp = Long.valueOf(
					msg.getUserDefinedParameter(ProtocolConstants.TIMESTAMP)
					).longValue();
			if ( msgTimestamp < localTimestamp ){
				return true;
			} else if( msgTimestamp == localTimestamp ){
				return getLocalName().compareTo(
						msg.getSender().getLocalName()) > 0;
			} else {
				return false;
			}
		}
	}
}
