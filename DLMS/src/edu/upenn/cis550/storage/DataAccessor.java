package edu.upenn.cis550.storage;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class DataAccessor {
	/** Accessors **/
	public PrimaryIndex<Integer,GraphNode> nodeByID;
	public PrimaryIndex<String, InvertedIndex> wordByValue; 
	
	/**
	 * Open the indices
	 * @param store
	 * @throws DatabaseException
	 */
	public DataAccessor(EntityStore store) 
			throws DatabaseException{
		
		/** primary key for graph node class **/
		nodeByID = store.getPrimaryIndex(Integer.class, GraphNode.class);
		wordByValue = store.getPrimaryIndex(String.class, InvertedIndex.class);
	}
}
