package edu.upenn.cis550.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.upenn.cis550.storage.GraphNode;
import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.utils.Constants;
import edu.upenn.cis550.utils.Stemmer;

public class Search {

	Object searchGraph(String uid, String query) {
		List<List<Integer>> ret_value=null;
		
		String[] queryKeys = query.split(" ");
		if (queryKeys.length == 1) {
			ret_value=searchQuery(uid,queryKeys[0]);
		}
		else if (queryKeys.length == 2) {
			ret_value = searchQuery(uid, queryKeys[0], queryKeys[1]);
			Collections.sort(ret_value, new Comparator<List<Integer>>() {

				@Override
				public int compare(List<Integer> o1, List<Integer> o2) {
					return o1.size()-o2.size();
				}
			});
		}
		else {
			System.out.println("Query length out of bounds : " + queryKeys.length);
		}
		checkForDuplicates(ret_value);
		return ret_value;
	}

	private void checkForDuplicates(List<List<Integer>> ret_value) {
		if (ret_value==null)
			return;
		boolean status;
		Set<Integer> pathsToRemove = new LinkedHashSet<Integer>();
		for (int i=0 ; i<ret_value.size(); i++) {
			for (int j=i ; j<ret_value.size(); j++) {
				if (i==j || pathsToRemove.contains(j)) {
					continue;
				}
				status = compareLists(ret_value.get(i),ret_value.get(j));
				if (status==true) {
					pathsToRemove.add(j);
				}
			}
		}
		
		List<Integer> pathsToRemoveList = new ArrayList<Integer>(pathsToRemove);
		Collections.sort(pathsToRemoveList);
		Collections.reverse(pathsToRemoveList);
		
		for (int index : pathsToRemoveList) {
			ret_value.remove(index);
		}
		
	}

	private boolean compareLists(List<Integer> list, List<Integer> list2) {
		if (list.size()!=list2.size())
			return false;
		for (int i = 0; i<list.size();i++) {
			if (list.get(i)!=list2.get(i)) {
				return false;
			}
		}
		return true;
	}

	private List<List<Integer>> searchQuery(String uid, String string1, String string2) {
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		if (Constants.IS_STEM) {
			Stemmer st = new Stemmer();
			string1 = st.stemWord(string1);
			string2 = st.stemWord(string2);
		}
		List<List<Integer>> paths = new ArrayList<List<Integer>>();
		
		GraphNode node = null;
		HashSet<Integer> invertedIndex = null;
		List<GraphNode> nodesForString1 = new ArrayList<GraphNode>();
		List<GraphNode> nodesForString2 = new ArrayList<GraphNode>();
		List<Integer> nodeIDsForString1 = new ArrayList<Integer>();
		List<Integer> nodeIDsForString2 = new ArrayList<Integer>();
		
		File storageDir = new File(Constants.PATH_DIR);
		StorageAPI store = new StorageAPI(storageDir);
		
		//generating list of nodes for string1
		invertedIndex = store.getInvertedIndex(string1);
		nodeIDsForString1.addAll(invertedIndex);
		checkPermission(store, uid, nodeIDsForString1);
		for (Integer nodeID : nodeIDsForString1) {
			node = store.getGraphNode(nodeID);
			if (node!=null) {
				nodesForString1.add(node);
			}
		}
		System.out.println("String : "+string1+"\nNodes : "+nodeIDsForString1.size());
		
		//generating list of nodes for string2
		invertedIndex = store.getInvertedIndex(string2);
		nodeIDsForString2.addAll(invertedIndex);
		checkPermission(store, uid, nodeIDsForString2);
		for (Integer nodeID : nodeIDsForString2) {
			node = store.getGraphNode(nodeID);
			if (node!=null) {
				nodesForString2.add(node);
			}
		}
		System.out.println("String : "+string2+"\nNodes : "+nodeIDsForString2.size());
		
		//algo to reach any node for string2 from any node in string1
		List<Integer> tempPath = new ArrayList<Integer>();
		for (Integer gNodeID : nodeIDsForString1) {
			tempPath.add(gNodeID);
			findGraphPathRecursive(0,gNodeID,nodeIDsForString2,Constants.DEPTH,tempPath,paths,uid,store);
			tempPath.remove(gNodeID);
		}
		store.closeDB();
		
		return paths;
	}

	private void findGraphPathRecursive(int currentDepth, Integer gNodeID, List<Integer> nodeIDsForString2, int depth, List<Integer> tempPath, List<List<Integer>> paths, String uid, StorageAPI store) {
		if (currentDepth>=depth)
			return;
		List<Integer> connectedNodes = new ArrayList<Integer>();
		connectedNodes = getConnectedNodes(store, gNodeID);
		checkPermission(store, uid, connectedNodes);
		for (Integer connectedNodeID : connectedNodes) {
			if (tempPath.contains(connectedNodeID)) {
				continue;
			}
			tempPath.add(connectedNodeID);
			if ( nodeIDsForString2.contains(connectedNodeID) ) {
				paths.add(new ArrayList<Integer>(tempPath));
			}
			else {
				findGraphPathRecursive(currentDepth+1, connectedNodeID, nodeIDsForString2, Constants.DEPTH, tempPath, paths, uid, store);
			}
			tempPath.remove(tempPath.size()-1);
		}
	}

	private List<List<Integer>> searchQuery(String uid, String string) {
		string = string.toLowerCase();
		if (Constants.IS_STEM) {
			Stemmer st = new Stemmer();
			string = st.stemWord(string);
		}
		
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		
		HashSet<Integer> invertedIndex = null;
		List<Integer> searchResults = new ArrayList<Integer>();
		List<Integer> initialResults = new ArrayList<Integer>();
		List<Integer> intermediateResults = null;
		
		File storageDir = new File(Constants.PATH_DIR);
		StorageAPI store = new StorageAPI(storageDir);
	
		invertedIndex = store.getInvertedIndex(string);
		
		initialResults.addAll(invertedIndex);
		checkPermission(store,uid,initialResults);
		
		searchResults.addAll(initialResults);
		
		for (Integer nodeID : initialResults) {
			intermediateResults=getConnectedNodes(store,nodeID);
			checkPermission(store,uid,intermediateResults);
			
			if (intermediateResults!=null && intermediateResults.size()!=0) {
				searchResults.addAll(intermediateResults);
			}
		}
		List<Integer> finalResults = new ArrayList<Integer>(new LinkedHashSet<Integer>(searchResults));
		List<Integer> path;
		for (Integer nodeID : finalResults) {
			path = getPathFromRoot(store,nodeID);
			output.add(path);
			printList(store,path);
		}
		store.closeDB();
		return output;
	}

	private void printList(StorageAPI store, List<Integer> path) {
		System.out.println("New Path:");
		GraphNode node;
		for (Integer data : path) {
			node = store.getGraphNode(data);
			System.out.println(data+":("+node.getName()+","+node.getValue()+","+node.getType()+")");
		}
		
	}

	private List<Integer> getPathFromRoot(StorageAPI store, Integer nodeID) {
		List<Integer> pathFromRoot = new ArrayList<Integer>();
		GraphNode node = store.getGraphNode(nodeID);
		pathFromRoot.add(nodeID);
		
		Integer pId;
		while(node.getParent()!=-1) {
			pId = node.getParent();
			pathFromRoot.add(pId);
			node = store.getGraphNode(pId);
		}
//		findPath(node.getNodeID(),nodeID,store,pathFromRoot);
		Collections.reverse(pathFromRoot);
		return pathFromRoot;
	}

	private boolean findPath(Integer documentID, Integer nodeID, StorageAPI store, List<Integer> pathFromRoot) {
		pathFromRoot.add(documentID);
		if (documentID==nodeID) {
			return true;
		}
		boolean status;
		
		GraphNode docNode = store.getGraphNode(documentID);
		if (docNode.getChildren()==null) {
			pathFromRoot.remove(documentID);
			return false;
		}
		for (Integer child : docNode.getChildren()) {
			status = findPath(child, nodeID, store, pathFromRoot);
			if (status) {
				return true;
			}
		}
		pathFromRoot.remove(documentID);
		return false;
	}

	private void checkPermission(StorageAPI store, String uid, List<Integer> results) {
		GraphNode node;
		boolean status;
		for (Integer nodeID : results) {
			node = store.getGraphNode(nodeID);
//			status = store.checkPermission(uid,node.getDocumentID());
//			if (!status) {
//				results.remove(nodeID);
//			}
		}
	}

	private List<Integer> getConnectedNodes(StorageAPI store, Integer nodeID) {
		List<Integer> connectedNodes = new ArrayList<Integer>();
		
		GraphNode node = store.getGraphNode(nodeID);
		
		//Parent
		if (node.getParent()!=-1)
			connectedNodes.add(node.getParent());
		
		//Children
		if (node.getChildren()!=null)
			connectedNodes.addAll(node.getChildren());
		
		//linked nodes
		if (store.getLinkedNodes(nodeID)!=null)
			connectedNodes.addAll(store.getLinkedNodes(nodeID));
		
		return connectedNodes;
	}
}
