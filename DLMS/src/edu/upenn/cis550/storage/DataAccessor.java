package edu.upenn.cis550.storage;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class DataAccessor {
	/** Accessors **/
	public PrimaryIndex<Integer,GraphNode> nodeByID;
	
	/**
	 * Open the indices
	 * @param store
	 * @throws DatabaseException
	 */
	public DataAccessor(EntityStore store) 
			throws DatabaseException{
		
		/** primary key for graph node class **/
		nodeByID = store.getPrimaryIndex(Integer.class, GraphNode.class);
	}
}
