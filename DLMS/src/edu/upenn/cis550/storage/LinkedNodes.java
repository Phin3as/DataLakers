package edu.upenn.cis550.storage;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
/**
 * Stores all the linked nodes
 * @author Jitesh
 *
 */
@Entity
public class LinkedNodes {
	
	@PrimaryKey
	private int nodeID;
	
	private List<Integer> linkedNodes = new ArrayList<Integer>();
	
	public LinkedNodes(){
		
	}
	
	public LinkedNodes(int nodeID){
		this.nodeID = nodeID;
	}
	
	public int getNodeID(){
		return nodeID;
	}
	public List<Integer> getLinkedNodes(){
		return linkedNodes;
	}
}
