package edu.upenn.cis550.storage;

import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class StemmedInvertedIndex {
	@PrimaryKey
	private String word;
	
	private HashSet<Integer> graphNodes = new HashSet<Integer>();
	
	public StemmedInvertedIndex(){
		//BerkleyDB requires a default constructor
	}
	
	public StemmedInvertedIndex(String word, int nodeID){
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
