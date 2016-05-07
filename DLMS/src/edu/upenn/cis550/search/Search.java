package edu.upenn.cis550.search;

import java.io.File;
import java.util.HashSet;

import edu.upenn.cis550.storage.StorageAPI;


public class Search {

	Object searchGraph(String uid, String query) {
		Object ret_value=null;
		
		String[] queryKeys = query.split(" ");
		if (queryKeys.length == 1) {
			ret_value=searchQuery(uid,queryKeys[0]);
		}
		else if (queryKeys.length == 2) {
			searchQuery(uid, queryKeys[0], queryKeys[1]);
			
		}
		else {
			System.out.println("Query length out of bounds : " + queryKeys.length);
		}
		return ret_value;
	}

	private void searchQuery(String uid, String string, String string2) {
		
	}

	private HashSet<Integer> searchQuery(String uid, String string) {
		HashSet<Integer> invertedIndex = null;

		File storageDir = new File("C:\\Users\\Sajal\\git\\DataLakers\\graph");
		StorageAPI store = new StorageAPI(storageDir);
	
		invertedIndex = store.getInvertedIndex(string);
		
		return invertedIndex;
	}
}
