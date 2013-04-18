package edu.mst.distopsysproj.main;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ReceiveMessageBehaviour extends Behaviour {

	private static final long serialVersionUID = -4335481543559741074L;

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null){
			System.out.println(myAgent.getAID().getName() + " received : " + msg.getContent());
			System.out.println("From: " + msg.getSender().getName());
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
