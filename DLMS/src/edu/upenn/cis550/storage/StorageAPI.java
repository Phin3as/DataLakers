package edu.upenn.cis550.storage;

import java.io.File;

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
		GraphNode graphNode = da.nodeByID.get(node.getId());
		if(graphNode != null){
			graphNode = new GraphNode(node.getId(),node.getDocumentID(),node.getName(),node.getType(),
									node.getValue(),node.getParent(),node.getChildren());
		}
	}
	
}
