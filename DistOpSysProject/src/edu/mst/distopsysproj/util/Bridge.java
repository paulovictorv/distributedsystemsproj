package edu.mst.distopsysproj.util;

import java.util.LinkedList;
import java.util.Queue;

import edu.mst.distopsysproj.person.Location;

public class Bridge {
	private static final String SEPARATOR = "#";
	
	private static Bridge instance;

	private Queue<String> queue;

	protected Bridge() {
		queue = new LinkedList<String>();
	}
	
	public static Bridge getInstance(){
		if(instance == null) {
			instance = new Bridge();
		}
		return instance;
	}
	
	public void enterBridge(String id) {
		queue.add(id + SEPARATOR + Location.BRIDGE.toString());
	}
	
	public void leftBridge(String id, Location toLocation) {
		queue.add(id + SEPARATOR + toLocation.toString());
	}
	
	public String[] pollBridge(){
		if(queue.isEmpty()) return null;
		
		String element = queue.poll();
		String[] data = new String[2];
		data[0] = element.substring(0, element.indexOf(SEPARATOR));
		element = element.substring(element.indexOf(SEPARATOR) + SEPARATOR.length());
		data[1] = element;
		return data;
	}
	
	@Override
	public String toString() {
		return queue.toString();
	}
}
