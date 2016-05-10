package edu.upenn.cis550.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

import edu.upenn.cis550.utils.Constants;

/**
 * A test class for testing the berkley DB content
 * @author Jitesh
 *
 */
public class BerkleyDBTest {
	private static File myDBEnvPath;

	public static void main(String args[]){
		//myDBEnvPath = new File(args[0]);
		myDBEnvPath = new File(Constants.PATH_STORAGE);
		StorageAPI storage = new StorageAPI(myDBEnvPath);
		BerkleyDBTest dbTest = new BerkleyDBTest();
		try {
			dbTest.showAllUsers();
			dbTest.showAllDocs();
			//dbTest.showAllNodes();
			//dbTest.showAllWords();
		} 
		catch (DatabaseException dbe) {
			System.err.println("DatabaseTest: " + dbe.toString());
			dbe.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
		}
		storage.closeDB();
		System.out.println("All done.");
	}
	
	public void showAllNodes() throws DatabaseException, IOException{
		/** Get a cursor that will walk every
		 *  node object in the store
		 */
		EntityCursor<GraphNode> items = StorageAPI.da.nodeByID.entities();
		
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
//		File file = new File(outputDir + "Nodes.txt");
//		if(file.exists()){
//			file.delete();
//		}
//		file.createNewFile();
//		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
//		
//		for(URLinks item: items){
//			//System.out.println(item.getURLink());
//			out.println(item.getURLink());
//		}
//		out.close();
	}
	
	public void showAllWords() throws DatabaseException{
		EntityCursor<InvertedIndex> items = StorageAPI.da.wordByValue.entities();
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
	
	public void showAllUsers() throws DatabaseException{
		EntityCursor<User> items = StorageAPI.da.userByName.entities();
		for(User item: items){
			System.out.println("<------->");
			String user = item.getUser();
			System.out.println("\nUser:- " + user);
			HashSet<Integer> docIDs = item.getDocs();
			Iterator<Integer> iter = docIDs.iterator();
			while(iter.hasNext()){
				System.out.print(iter.next());
				System.out.print("\t");
			}
			System.out.println("");
		}
		items.close();
	}
	
	public void showAllDocs() throws DatabaseException{
		EntityCursor<Document> items = StorageAPI.da.documentByID.entities();
		for(Document item: items){
			System.out.println("<------->");
			int docID = item.getDocID();
			System.out.println("\nDocID:- " + docID);
			HashSet<String> docIDs = item.getUsers();
			Iterator<String> iter = docIDs.iterator();
			while(iter.hasNext()){
				System.out.print(iter.next());
				System.out.print("\t");
			}
			System.out.println("");
		}
		items.close();
	}
}
