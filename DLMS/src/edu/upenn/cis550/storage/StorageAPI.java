package edu.upenn.cis550.storage;

import java.io.File;
import java.util.StringTokenizer;

import com.sleepycat.je.DatabaseException;

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
			InvertedIndex index = da.wordByValue.get(tokenizer.nextToken());
			if(index == null){
				index = new InvertedIndex(tokenizer.nextToken(), nodeID);
			}else{
				index.getGraphNodes().add(nodeID);
			}
			da.wordByValue.put(index);
		}
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
