package edu.upenn.cis550.linker;

import java.util.HashSet;

import edu.upenn.cis550.storage.GraphNode;
import edu.upenn.cis550.storage.StorageAPI;

public class WorkerThread implements Runnable{

	int nodeID;
	StorageAPI store;
	
	public WorkerThread(StorageAPI store,Integer nodeID) {
		this.nodeID = nodeID;
		this.store = store;
	}
	
	@Override
	public void run() {			
		GraphNode node=null;
		String name,value;
		HashSet<Integer> invertedIndex = null,linkedNodes = new HashSet<Integer>();
		
		node = store.getGraphNode(nodeID);
		
		name = node.getName();
		if (name!=null)
			invertedIndex = store.getInvertedIndex(name);
		if (invertedIndex!=null)
			linkedNodes.addAll(invertedIndex);
		
		value = node.getName();
		if (value!=null)
			invertedIndex = store.getInvertedIndex(value);
		if (invertedIndex!=null)
			linkedNodes.addAll(invertedIndex);
		
		LinkerUtil.addToDB(Thread.currentThread().getId(),nodeID,linkedNodes);
	}
}
