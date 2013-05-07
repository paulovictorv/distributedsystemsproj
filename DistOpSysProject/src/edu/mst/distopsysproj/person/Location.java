package edu.mst.distopsysproj.person;

public enum Location {
	A, B, BRIDGE;

	public static Location getOppositeLocation(Location lastLocation) {
		if(lastLocation == A) return B;
		else if(lastLocation == B) return A;
		else return lastLocation;
	}
}
