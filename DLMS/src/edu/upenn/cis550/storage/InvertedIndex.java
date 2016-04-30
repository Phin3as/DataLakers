package edu.upenn.cis550.storage;

import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * Stores the inverted index {word -> list of nodes}
 * @author Jitesh
 *
 */
@Entity
public class InvertedIndex {
	
	@PrimaryKey
	private String word;
	
	private HashSet<Integer> graphNodes = new HashSet<Integer>();
	
	public InvertedIndex(){
		//BerkleyDB requires a default constructor
	}
	
	public InvertedIndex(String word, int nodeID){
		this.word = word;
		this.graphNodes.add(nodeID);
	}
	
	public String getWord(){
		return word;
	}
	
	public HashSet<Integer> getGraphNodes(){
		return graphNodes;
	}
}
