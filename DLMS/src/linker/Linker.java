package linker;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import edu.upenn.cis550.storage.GraphNode;
import edu.upenn.cis550.storage.StorageAPI;

public class Linker {
	boolean linker(Integer docID) {
		System.out.println("Linker.linker():BEGIN:");

		File storageDir = new File("C:\\Users\\Sajal\\git\\DataLakers\\graph");
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
		return false;
	}

	private void printNodes(StorageAPI store, GraphNode node, HashSet<Integer> linkedNodes) {
		GraphNode linkedNode=null;
		System.out.println("New Link "+linkedNodes.size());
		System.out.println("node ("+node.getNodeID()+") : "+node.getName()+","+node.getValue());
//		for (Integer nodeID : linkedNodes) {
//			linkedNode = store.getGraphNode(nodeID);
//			System.out.println("node ("+linkedNode.getNodeID()+") : "+linkedNode.getName()+","+linkedNode.getValue());
//		}
		
	}
}
