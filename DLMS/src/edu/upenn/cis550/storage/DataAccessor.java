package edu.upenn.cis550.storage;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class DataAccessor {
	/** Accessors **/
	public PrimaryIndex<Integer,GraphNode> nodeByID;
	public PrimaryIndex<String, InvertedIndex> wordByValue; 
	public PrimaryIndex<Integer,ForwardIndex> docByID;
	public PrimaryIndex<String, User> userByName;
	public PrimaryIndex<Long, Document> documentByID;
	
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
		docByID = store.getPrimaryIndex(Integer.class, ForwardIndex.class);
		userByName = store.getPrimaryIndex(String.class, User.class);
		documentByID = store.getPrimaryIndex(Long.class, Document.class);
	}
}
