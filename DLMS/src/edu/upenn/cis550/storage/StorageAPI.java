package edu.upenn.cis550.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

import edu.upenn.cis550.extractor.Struct;

/**
 * Main Storage API to access the database
 * @author Jitesh
 *
 */
public class StorageAPI {
	public static BerkleyDBEnvironment myDBEnv = new BerkleyDBEnvironment();
	public static DataAccessor da;
	
	private File storageDir;
	
	public StorageAPI(File storageDir){
		this.storageDir = storageDir;
		setUp();
	}
	
	/**
	 * Setup BerkleyDB environment
	 * @throws DatabaseException
	 */
	public void setUp() throws DatabaseException{
		myDBEnv.setup(storageDir, false);
		da = new DataAccessor(myDBEnv.getEntityStore());
	}
	
	
	public void putGraphNode(Struct node){
		GraphNode graphNode = new GraphNode(node.getId(),node.getDocumentID(),node.getName(),node.getType(),
									node.getValue(),node.getParent(),node.getChildren());
		if(checkName(node.getType())){
			putInvertedIndex(node.getName(), node.getId());
		}
		
		if(checkValue(node.getValue(), node.getType())){
			putInvertedIndex(node.getValue(), node.getId());
		}
		da.nodeByID.put(graphNode);
	}
	
	public void putInvertedIndex(String word, int nodeID){
		StringTokenizer tokenizer = new StringTokenizer(word, " \t\n\r\f,.:;'?![]");
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			InvertedIndex index = da.wordByValue.get(token);
			if(index == null){
				index = new InvertedIndex(token, nodeID);
			}else{
				index.getGraphNodes().add(nodeID);
			}
			da.wordByValue.put(index);
		}
	}
	
	public void showAllWords() throws DatabaseException{
		EntityCursor<InvertedIndex> items = da.wordByValue.entities();
		for(InvertedIndex item: items){
			System.out.println("<------->");
			String word = item.getWord();
			System.out.println("\nWord:- " + word);
			HashSet<Integer> nodeIDs = item.getGraphNodes();
			Iterator<Integer> iter = nodeIDs.iterator();
			while(iter.hasNext()){
				System.out.print(iter.next());
				System.out.print("\t");
			}
			System.out.println("");
		}
		items.close();
	}
	
	public void showAllNodes() throws DatabaseException, IOException{
		/** Get a cursor that will walk every
		 *  node object in the store
		 */
		EntityCursor<GraphNode> items = da.nodeByID.entities();
		
		for(GraphNode item: items){
			System.out.println("<------->");
			System.out.println("\tNode id :"  + item.getNodeID());
			System.out.println("\tDoc Id :"  + item.getDocumentID());
			System.out.println("\tName :"  + item.getName());
			System.out.println("\tType :"  + item.getType());
			System.out.println("\tValue :"  + item.getValue());
			System.out.println("\tParent :"  + item.getParent());
			System.out.print("\tChildren : ");
			
			if(!(item.getChildren() == null)){
				for (int j : item.getChildren()){
					System.out.print(j + " " );
				}
				System.out.println("\t");
			}else{
				System.out.println("None");
			}
		}
		items.close();
	}
	
	public boolean checkName(String type){
		if(type.equals("RECORD")){
			return false;
		}
		return true;
	}
	
	public boolean checkValue(String value, String type){
		if(value == null){
			return false;
		}
		
		if(type.equals("RECORDVALUE")){
			return false;
		}
		
		if(type.equals("VALUE")){
			return false;
		}
		
		if(type.equals("TOKEN")){
			return false;
		}
		
		return true;
	}
	/**
	 * Close the BerkleyDB environment
	 */
	public void closeDB(){
		myDBEnv.close();
	}
	
}
