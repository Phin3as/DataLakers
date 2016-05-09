package edu.upenn.cis550.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

import edu.upenn.cis550.extractor.Struct;
import edu.upenn.cis550.linker.LinkerObject;
import edu.upenn.cis550.utils.Stemmer;
/**
 * Main Storage API to access the database
 * @author Jitesh
 *
 */
public class StorageAPI {
	public static BerkleyDBEnvironment myDBEnv = new BerkleyDBEnvironment();
	public static DataAccessor da;
	public static String logKey = "log";
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
	
	public int getLastNodeID(){
		DataLog log = da.dataLogByValue.get(logKey);
		int lastNodeID = 0;
		if(log == null){
			log = new DataLog(logKey,0,0);
			da.dataLogByValue.put(log);
		}else{
			lastNodeID = log.getLastNodeID();
		}
		return lastNodeID;
	}
	
	public int getLastDocID(){
		DataLog log = da.dataLogByValue.get(logKey);
		int lastDocID = 0;
		if(log == null){
			log = new DataLog(logKey,0,0);
			da.dataLogByValue.put(log);
		}else{
			lastDocID = log.getLastDocID();
		}
		return lastDocID;
	}
	
	
	/**
	 * Put graphNode in the database
	 * @param node
	 */
	public void putGraphNode(Struct node){
		GraphNode graphNode = new GraphNode(node.getId(),node.getDocumentID(),node.getName(),node.getType(),
									node.getValue(),node.getParent(),node.getChildren());
		if(checkName(node.getType())){
			putInvertedIndex(node.getName().toLowerCase(), node.getId());
			putStemmedInvertedIndex(node.getName().toLowerCase(), node.getId());
		}
		
		if(checkValue(node.getValue(), node.getType())){
			putInvertedIndex(node.getValue().toLowerCase(), node.getId());
			putStemmedInvertedIndex(node.getValue().toLowerCase(), node.getId());
		}
		da.nodeByID.put(graphNode);
	}
	
	/**
	 * Returns the Graph Node for the given nodeID
	 * @param nodeID
	 * @return
	 */
	public GraphNode getGraphNode(int nodeID){
		GraphNode node = da.nodeByID.get(nodeID);
		if(node == null){
			return null;
		}
		return node;
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
	 * Returns all nodes for a given word
	 * @param word
	 * @return
	 */
	public HashSet<Integer> getInvertedIndex(String word){
		InvertedIndex invertedIndexSet = da.wordByValue.get(word);
		if(invertedIndexSet == null){
			return null;
		}
		return invertedIndexSet.getGraphNodes();
	}
	
	/**
	 * Stem and put word with inverted index in the database
	 * @param word
	 * @param nodeID
	 */
	public void putStemmedInvertedIndex(String word, int nodeID){
		StringTokenizer tokenizer = new StringTokenizer(word, " \t\n\r\f,.:;'?![]");
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			Stemmer wordStemmer = new Stemmer();
			String stemmedWord = wordStemmer.stemWord(token);
			StemmedInvertedIndex index = da.stemIndexByValue.get(stemmedWord);
			if(index == null){
				index = new StemmedInvertedIndex(stemmedWord, nodeID);
			}else{
				index.getGraphNodes().add(nodeID);
			}
			da.stemIndexByValue.put(index);
		}
	}
	
	public HashSet<Integer> getStemmedInvertedIndex(String word){
		StemmedInvertedIndex stemInvertedIndexSet = da.stemIndexByValue.get(word);
		if(stemInvertedIndexSet == null){
			return null;
		}
		return stemInvertedIndexSet.getGraphNodes();
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
	 * Get all nodes present in a doc
	 * @param docID
	 * @return
	 */
	public List<Integer> getDocNodes(int docID){
		return da.docByID.get(docID).getDocNodes();
	}
	
	/**
	 * Update Links in the database
	 * @param links
	 */
	public void updateLinks(List<LinkerObject> links){
		for(int i=0; i<links.size(); i++){
			int node1 = links.get(i).getNode1();
			int node2 = links.get(i).getNode2();
			putLinkedNode(node1,node2);
			putLinkedNode(node2,node1);
		}
	}
	
	/**
	 * Put Linked Nodes in the database
	 * @param node1
	 * @param node2
	 */
	public void putLinkedNode(int node1, int node2){
		LinkedNodes node = da.nodeLinkerByID.get(node1);
		if(node == null){
			node = new LinkedNodes(node1);
		}
		node.getLinkedNodes().add(node2);
		da.nodeLinkerByID.put(node);
	}
	
	/**
	 * Returns a list of linked nodes to the input nodeID
	 * @param nodeID
	 * @return
	 */
	public List<Integer> getLinkedNodes(int nodeID){
		if (da.nodeLinkerByID.get(nodeID)==null)
			return null;
		return da.nodeLinkerByID.get(nodeID).getLinkedNodes();
	}
	
	/**
	 * Putting a new document in the database
	 * @param documentID
	 * @param type
	 * @param user
	 */
	public void putDocument(int documentID, String type, String user){
		Document document = new Document(documentID, type, user);
		da.documentByID.put(document);
	}
	
	/**
	 * Check if a user has permission to access a docID
	 * @param docID
	 * @param user
	 * @return
	 */
	public boolean checkPermission(int docID, String user){
		Document document = da.documentByID.get(docID);
		if(document.getDocType().equals("PUBLIC")){
			return true;
		}
		
		if(document.getUsers().contains(user)){
			return true;
		}
		return false;
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
	 * Returns all the graph nodeIDs
	 * @return
	 */
	public List<Integer> getAllNodes(){
		List<Integer> nodesList = new ArrayList<Integer>();
		EntityCursor<GraphNode> items = da.nodeByID.entities();
		
		for(GraphNode item: items){
			nodesList.add(item.getNodeID());
		}
		return nodesList;
	}
	
	/**
	 * Returns all the docIDs
	 * @return
	 */
	public List<Integer> getAllDocs(){
		List<Integer> docsList = new ArrayList<Integer>();
		EntityCursor<ForwardIndex> items = da.docByID.entities();
		for(ForwardIndex item: items){
			docsList.add(item.getDocID());
		}
		return docsList;
	}
	
	/************************ Testing Functions *****************************/
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