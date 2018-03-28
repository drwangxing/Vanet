package edu.auburn.comp6360.network;

import java.io.Serializable;

public class Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -645034248475856092L;	// Automatically generated by Eclipse 

	private int seqNum;
	private int source;
	private int prevHop;
	
	private String packetType; // "NORMAL", "JOIN", "LEAVE", "ACKJOIN", "ACKLEAVE"
	private int dest;
	private int extraInfo;
	
	public Header(String type, int source, int sn, int prevHop) {
		this.packetType = type;
		this.source = source;
		this.seqNum = sn;		
		this.prevHop = prevHop;
		this.dest = -1;
		this.extraInfo = -1;
	}

	public Header(String type, int source, int sn, int prevHop, int dest, int extraInfo) {
		this.packetType = type;
		this.source = source;
		this.seqNum = sn;		
		this.prevHop = prevHop;
		this.dest = dest;
		this.extraInfo = extraInfo;
	}
	
	public int getSeqNum() {
		return this.seqNum;
	}
	
	public int getSource() {
		return this.source;
	}
	
	public int getDest() {
		return this.dest;
	}
	
	public int getPrevHop() {
		return this.prevHop;
	}
	
	public String getPacketType() {
		return this.packetType;
	}
	
	public int getExtraInfo() {
		return this.extraInfo;
	}

	public void setPrevHop(int nodeID) {
		this.prevHop = nodeID;
	}
	
	public void setDest(int dest) {
		this.dest = dest;
	}
	
	public void setExtraInfo(int extraInfo) {
		this.extraInfo = extraInfo;
	}
	
//	public void setPacketType(int type) {
//		this.packetType = type;
//	}
	
}
