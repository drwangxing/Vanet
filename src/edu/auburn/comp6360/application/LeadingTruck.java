package edu.auburn.comp6360.application;

import java.util.LinkedList;

import edu.auburn.comp6360.network.Header;
import edu.auburn.comp6360.network.Packet;
import edu.auburn.comp6360.network.VehicleInfo;
import edu.auburn.comp6360.utilities.VehicleHandler;

public class LeadingTruck extends Vehicle {	

	public static final double TRUCK_WIDTH = 4;
	public static final double TRUCK_LENGTH = 10;
	public static double INIT_X = 10;
	public static double INIT_Y = 0; // in the Right Lane
	public static double INIT_V = 30;
	
	private LinkedList<Integer> roadTrainList;
	
	public LeadingTruck(int nodeID) {
		super(nodeID);
		this.setGPS(new GPS(INIT_X, INIT_Y));
		this.setWidth(TRUCK_WIDTH);
		this.setLength(TRUCK_LENGTH);
		this.setVelocity(INIT_V);
		
		roadTrainList = new LinkedList<Integer>();
		roadTrainList.add(nodeID);
	}

	
  /**
   * Obtain the acceleration for the Leading Vehicle (every 10 ms)
   * 
   * @param
   * @return a random number between -1 and 1
   */	
	@Override
	public void setAcceleration() {
		this.setAcceleration(Math.random() * 2 - 1);
	}
	
	public void startAll() {
//		super.startAll();
//		RoadTrainHandlerThread train = new RoadTrainHandlerThread();
//		train.start();
		
		bt = new BroadcastThread();
		ct = new ConfigThread();
		st = new ServerThread(SERVER_PORT+nodeID);

		bt.start();
		ct.start();
		st.run();
		
	}
	
	
	public void receivePacket(Packet packetReceived) {
		Header header = packetReceived.getHeader();
		int prevHop = header.getPrevHop();

		
		
		int source = header.getSource();
		int sn = header.getSeqNum();
		String packetType = header.getPacketType();
		if (packetType.equals("normal"))  {
			if (cache.updatePacketSeqNum(source, packetType, sn, getNodeID())) {
				VehicleInfo vInfo = packetReceived.getVehicleInfo();
				selfNode = VehicleHandler.updateNeighborsFromPacket(selfNode, source, vInfo.getGPS());			
				sendPacket(packetReceived, source, sn, prevHop);
			}
		} else {		// in the case that of not normal packets
			if ( (header.getDest() != this.nodeID) && (cache.updatePacketSeqNum(source, packetType, sn, getNodeID())) )
				sendPacket(packetReceived, source, sn, prevHop);
		}
	}
	
	
	@Override
	public void processJoinRequest(int source) {
		if (!roadTrainList.contains(source)) {
			int lastInTrain = roadTrainList.getLast();		// Tell the source (the one who wanna join) to follow the last in road train
			initPacket("ackJoin", source, lastInTrain);	// TODO LATER: implement as the nearest ahead node for source
		}
	}
	
	@Override
	public void processAckJoin(int source, int info) {
		roadTrainList.addLast(source);
	}
	
	@Override
	public void processLeaveRequest(int source) {
		if (roadTrainList.contains(source)) {
			initPacket("ackLeave", source, 0);
		}
	}
	
	@Override
	public void processAckLeave(int source, int info) {
		int i = roadTrainList.indexOf(Integer.valueOf(source));
		int dest = roadTrainList.get(i + 1);	// The one behind i should be notified
		int prev = roadTrainList.get(i - 1);	// Tell it to follow the one previously before i
		roadTrainList.remove(i);				// Update roadTrainList
		initPacket("nofifyLeave", dest, prev);		
	}
	
	
}
