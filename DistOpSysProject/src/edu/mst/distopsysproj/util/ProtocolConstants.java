package edu.mst.distopsysproj.util;

/**
 * Class of constants to be used in the protocol,
 * including exchange messaging time, message's
 * defined parameters, and command-line arguments
 * 
 * @author Julio Zynger, Paulo Victor Melo
 *
 */
public class ProtocolConstants {
	
	public final static Integer ASK_LOCATION_INTERVAL = 1 * 500; // 0.5 seconds
	
	public final static String INFORM_LOCATION_REQUEST = "InformLocation";
	public static final String INFORM_LOCATION_CONVID = "InformLocationID";

	public static final String TIMESTAMP = "Timestamp";

	public static final String MSGTYPE_REQUEST = "MessageTypeRequest";
	public static final String MSGTYPE_ACK = "MessageTypeAck";

	public static final String TWOVISITORSPROTOCOL_LASTLOCATION = "LastLocation";

	public static final String ARG_TWOVISITORS = "twovisitors";

}
