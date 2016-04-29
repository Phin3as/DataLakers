package edu.upenn.cis550.storage;

import java.util.ArrayList;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * Main class to store the node structure
 * @author Jitesh
 *
 */
@Entity
public class GraphNode {
	
	@PrimaryKey
	private int nodeID;
	
	private int documentID;
	private String name;
	private String type;
	private String value;
	private int parent;
	private ArrayList<Integer> children = new ArrayList<Integer>();
	
	public GraphNode(){
		//BerkleyDB requires a default constructor
	}
	
	public GraphNode(int nodeID, int documentID, String name, String type, String value, int parent, ArrayList<Integer> children){
		this.nodeID = nodeID;
		this.documentID = documentID;
		this.name = name;
		this.type = type;
		this.value = value;
		this.parent = parent;
		this.children = children;
	}
	
	public int getDocumentID() {
		return documentID;
	}

	public void setDocumentID(int documentID) {
		this.documentID = documentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public ArrayList<Integer> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Integer> children) {
		this.children = children;
	}
}