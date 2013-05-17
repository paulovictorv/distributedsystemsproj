package edu.mst.distopsysproj.person;

/**
 *  Enum to define the location of an Agent in the environment
 *  either side A, side B or crossing the bridge.
 * 
 * @author Julio Zynger, Paulo Victor Melo
 */
public enum Location {
	A, B, BRIDGE;

	/**
	 * Return the opposite location's enum regarding to the bridge
	 * so, if the parameter <code>lastLocation</code> is A, the return
	 * will be B, and so on.
	 */
	public static Location getOppositeLocation(Location lastLocation) {
		if(lastLocation == A) return B;
		else if(lastLocation == B) return A;
		else return lastLocation;
	}
}
