package edu.mst.distopsysproj.util;

import jade.core.Agent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.mst.distopsysproj.person.Location;

/**
 * Queue encapsulator to represent the protocol's critical
 * section, maintaining a singleton of the queue, and
 * providing access to adding or polling members of the queue
 * only through API-defined methods.
 * 
 * @author Julio Zynger, Paulo Victor Melo
 *
 */
public class Bridge {
	private static final String SEPARATOR = "#";
	
	private static Bridge instance;

	private Queue<String> queue;

	protected Bridge() {
		// since using a distributed protocol,
		// synchronization is a requirement to
		// guarantee concurrency
		queue = new ConcurrentLinkedQueue<String>();
	}
	
	/**
	 * Singleton's pattern method
	 * @return Singleton instance of {@link Bridge}
	 */
	public static Bridge getInstance(){
		if(instance == null) {
			instance = new Bridge();
		}
		return instance;
	}
	
	/**
	 * Adds an {@link Agent} to the end of the queue,
	 * following a defined pattern to determine entering
	 * the {@link Bridge}.
	 * @param id {@link Agent}'s identifier
	 */
	public void enterBridge(String id) {			
		String entry = id + SEPARATOR + Location.BRIDGE.toString();
		queue.add(entry);
	}
	
	/**
	 * Adds an {@link Agent} to the end of the queue,
	 * following a defined pattern to determine leaving
	 * the {@link Bridge}.
	 * @param id {@link Agent}'s identifier
	 * @param toLocation {@link Location} to where this
	 * {@link Agent} is moving to.
	 */
	public void leftBridge(String id, Location toLocation) {			
		String entry = id + SEPARATOR + toLocation.toString();
		queue.add(entry);
	}
	
	/**
	 * Removes and returns the head of the queue,
	 * following a defined pattern of array of Strings.
	 * @return A String[] with <code>length = 2</code>,
	 * where the first item is the {@link Agent}'s id
	 * and the second item is the {@link Location} he is
	 * moving from/to
	 */
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
