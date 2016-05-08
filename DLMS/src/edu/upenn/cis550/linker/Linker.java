package edu.upenn.cis550.linker;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.upenn.cis550.storage.GraphNode;
import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.util.Constants;

public class Linker {
	
	static final int THREAPOOL_SIZE=20;
	
	boolean linker(Integer docID) {
		System.out.println("Linker.linker():BEGIN:");

		File storageDir = new File(Constants.DIR_PATH);
		StorageAPI store = new StorageAPI(storageDir);
		List<Integer> docNodes = store.getDocNodes(docID);
		
		//do stemming/synonyms
		GraphNode node=null;
		String name,value;
		HashSet<Integer> invertedIndex = null,linkedNodes = null;
		
		for (Integer nodeID : docNodes) {
			linkedNodes = new HashSet<Integer>();
			node = store.getGraphNode(nodeID);

			name = node.getName();
			if (name != null)
				invertedIndex = store.getInvertedIndex(name);
			if (invertedIndex != null)
				linkedNodes.addAll(invertedIndex);
			
			value = node.getValue();
			if (value != null)
				invertedIndex = store.getInvertedIndex(value);
			if (invertedIndex != null)
				linkedNodes.addAll(invertedIndex);
			
			linkedNodes.removeAll(docNodes);
			printNodes(store,node,linkedNodes);
		}
		//check for filename comparison
		//update dbs
		store.closeDB();
		System.out.println("Linker.linker()::END");
		return true;
	}

	boolean threadedLinker(Integer docID) {
		System.out.println("Linker.threadedLinker()::BEGIN");
		
		ExecutorService executor = Executors.newFixedThreadPool(THREAPOOL_SIZE);
		
		File storageDir = new File(Constants.DIR_PATH);
		StorageAPI store = new StorageAPI(storageDir);
		List<Integer> docNodes = store.getDocNodes(docID);
		
		for (Integer nodeID : docNodes) {
			Runnable worker = new WorkerThread(store,nodeID);
			executor.execute(worker);
		}
		
		executor.shutdown();
        while (!executor.isTerminated()) {}
        
		store.closeDB();
		System.out.println("Linker.threadedLinker()::END");
		return true;
	}
	private void printNodes(StorageAPI store, GraphNode node, HashSet<Integer> linkedNodes) {
//		GraphNode linkedNode=null;

//		System.out.println("New Link "+linkedNodes.size());
//		System.out.println("node ("+node.getNodeID()+") : "+node.getName()+","+node.getValue());
//		for (Integer nodeID : linkedNodes) {
//			linkedNode = store.getGraphNode(nodeID);
//			System.out.println("node ("+linkedNode.getNodeID()+") : "+linkedNode.getName()+","+linkedNode.getValue());
//		}

		System.out.println("New Link ("+node.getNodeID()+") : "+linkedNodes.size());
//		for (Integer nodeID : linkedNodes) {
//			System.out.println(nodeID);
//		}
//		System.out.println();
	}
}
