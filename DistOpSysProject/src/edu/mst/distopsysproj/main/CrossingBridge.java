package edu.mst.distopsysproj.main;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class CrossingBridge extends Agent {

	@Override
	protected void setup() {
		System.out.println("Hello! " + getAID().getName() + " is ready!");
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("agenttwo", AID.ISLOCALNAME));
		msg.setLanguage("English");
		msg.setContent("Testing message passing");
		send(msg);
		System.out.println(getAID().getName() + " sent message");
	}
	
	@Override
	protected void takeDown() {
		System.out.println("Bye! " + getAID().getName() + " is shutting down!");
	}
}
