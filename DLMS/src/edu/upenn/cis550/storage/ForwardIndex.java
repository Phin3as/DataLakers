package edu.upenn.cis550.storage;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * Entity for storing docID -> list of nodes in it
 * @author Jitesh
 *
 */
@Entity
public class ForwardIndex {
	
	@PrimaryKey
	private int docID;
	
	private List<Integer> docNodes = new ArrayList<Integer>();
	
	public ForwardIndex(){
		
	}
	
	public ForwardIndex(int docID, List<Integer> docNodes){
		this.docID = docID;
		this.setDocNodes(docNodes);
	}

	public int getDocID(){
		return docID;
	}
	
	public List<Integer> getDocNodes() {
		return docNodes;
	}

	public void setDocNodes(List<Integer> docNodes) {
		this.docNodes = docNodes;
	}
}
