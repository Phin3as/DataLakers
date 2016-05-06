package edu.upenn.cis550.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
	
	/**
	 * Close the BerkleyDB environment
	 */
	public void closeDB(){
		myDBEnv.close();
	}
	
	/**
	 * Put graphNode in the database
	 * @param node
	 */
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
	
	/**
	 * Create and put inverted index in the database
	 * @param word
	 * @param nodeID
	 */
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
	
	/**
	 * Create and put forward index in the database
	 * @param docID
	 * @param docNodes
	 */
	public void putForwardIndex(int docID, List<Integer> docNodes){
		ForwardIndex forwardIndex = new ForwardIndex(docID,docNodes);
		da.docByID.put(forwardIndex);
	}
	
	/**
	 * Putting a new document in the database
	 * @param documentID
	 * @param type
	 * @param user
	 */
	public void putDocument(long documentID, String type, String user){
		Document document = new Document(documentID, type, user);
		da.documentByID.put(document);
	}
	
	/**
	 * Get all nodes present in a doc
	 * @param docID
	 * @return
	 */
	public List<Integer> getDocNodes(int docID){
		return da.docByID.get(docID).getDocNodes();
	}
	
	/**
	 * Validate name parameter
	 * @param type
	 * @return
	 */
	public boolean checkName(String type){
		if(type.equals("RECORD")){
			return false;
		}
		return true;
	}
	
	/**
	 * Validate value parameter
	 * @param value
	 * @param type
	 * @return
	 */
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
	 * Helper function for testing inverted index
	 * @throws DatabaseException
	 */
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
	
	/**
	 * Helper function for testing forward index
	 * @throws DatabaseException
	 */
	public void showAllDocs() throws DatabaseException{
		EntityCursor<ForwardIndex> items = da.docByID.entities();
		for(ForwardIndex item: items){
			System.out.println("<------->");
			int docID = item.getDocID();
			System.out.println("\nDocID:- " + docID);
			List<Integer> nodeIDs = item.getDocNodes();
			for(int i=0; i<nodeIDs.size(); i++){
				System.out.print(nodeIDs.get(i));
				System.out.print("\t");
			}
			System.out.println("");
		}
		items.close();
	}
	
	/**
	 * Helper function for testing graph nodes
	 * @throws DatabaseException
	 * @throws IOException
	 */
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
}