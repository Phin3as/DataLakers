package edu.upenn.cis550.search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.upenn.cis550.storage.GraphNode;
import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.util.Constants;


public class Search {

	Object searchGraph(String uid, String query) {
		Object ret_value=null;
		
		String[] queryKeys = query.split(" ");
		if (queryKeys.length == 1) {
			ret_value=searchQuery(uid,queryKeys[0]);
		}
		else if (queryKeys.length == 2) {
			ret_value = searchQuery(uid, queryKeys[0], queryKeys[1]);
			
		}
		else {
			System.out.println("Query length out of bounds : " + queryKeys.length);
		}
		return ret_value;
	}

	private List<List<Integer>> searchQuery(String uid, String string, String string2) {
		
		return null;
	}

	private List<Integer> searchQuery(String uid, String string) {
		HashSet<Integer> invertedIndex = null;
		List<Integer> searchResults = new ArrayList<Integer>();
		List<Integer> initialResults = new ArrayList<Integer>();
		List<Integer> intermediateResults = null;
		
		File storageDir = new File(Constants.DIR_PATH);
		StorageAPI store = new StorageAPI(storageDir);
	
		invertedIndex = store.getInvertedIndex(string);
		
		initialResults.addAll(invertedIndex);
		checkPermission(uid,initialResults);
		
		searchResults.addAll(initialResults);
		
		for (Integer nodeID : initialResults) {
			intermediateResults=getConnectedNodes(store,nodeID);
			checkPermission(uid,intermediateResults);
			
			if (intermediateResults!=null && intermediateResults.size()!=0) {
				searchResults.addAll(intermediateResults);
			}
		}
		store.closeDB();
		return searchResults;
	}

	private void checkPermission(String uid, List<Integer> results) {
		//TODO : check permissions
		
	}

	private List<Integer> getConnectedNodes(StorageAPI store, Integer nodeID) {
		List<Integer> connectedNodes = new ArrayList<Integer>();
		
		GraphNode node = store.getGraphNode(nodeID);
		
		//Parent
		connectedNodes.add(node.getParent());
		
		//Children
		if (node.getChildren()!=null)
			connectedNodes.addAll(node.getChildren());
		
		//linked nodes
		//connectedNodes.addAll(store.getLinkedNodes(nodeID))
		
		return connectedNodes;
	}
}
