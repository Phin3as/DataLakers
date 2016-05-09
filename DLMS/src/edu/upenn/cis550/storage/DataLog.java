package edu.upenn.cis550.storage;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class DataLog {
	
	@PrimaryKey
	private String log = "log";
	private int lastNodeID;
	private int lastDocID;
	
	public DataLog(){
		
	}
	
	public DataLog(String log, int lastNodeID, int lastDocID){
		this.log = log;
		this.setLastDocID(lastDocID);
		this.setLastNodeID(lastNodeID);
	}

	public int getLastNodeID() {
		return lastNodeID;
	}

	public void setLastNodeID(int lastNodeID) {
		this.lastNodeID = lastNodeID;
	}

	public int getLastDocID() {
		return lastDocID;
	}

	public void setLastDocID(int lastDocID) {
		this.lastDocID = lastDocID;
	}
}
