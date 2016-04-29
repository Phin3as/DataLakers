package edu.upenn.cis550.storage;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

/**
 * This class is responsible for the opening and closing 
 * of database store and environment
 * @author Jitesh Gupta
 *
 */
public class BerkleyDBEnvironment {
	
	private Environment myEnv;
	private EntityStore store;
	
	/**
	 * Default Constructor
	 */
	public BerkleyDBEnvironment(){
		
	}
	/**
	 * Opens the environment and store
	 * @param envHome
	 * @param readOnly
	 * @throws DatabaseException
	 */
	public void setup(File envHome, boolean readOnly) 
			throws DatabaseException{
		/** create storage directory (envHome) if does not already exist **/
		if(!envHome.exists()){
			System.out.println("Creating Directory:-" + envHome);
			boolean result = false;
			
			try{
				envHome.mkdirs();
				result = true;
			}catch(SecurityException e){
				e.printStackTrace();
				System.out.println("Cannot create the storage (DB env) directory");
				System.exit(-1);
			}
			
			if(result){
				System.out.println("Directory Created");
			}
		}
		
		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		StoreConfig myStoreConfig = new StoreConfig();
		
		myEnvConfig.setReadOnly(readOnly);
		myEnvConfig.setReadOnly(readOnly);
		/**
		 * If the environment is opened for write,
		 * create environment and entity store if 
		 * they do not exist already
		 */
		myEnvConfig.setAllowCreate(!readOnly);
		myStoreConfig.setAllowCreate(!readOnly);
		
		/** Open the environment and entity store **/
		myEnv = new Environment(envHome, myEnvConfig);
		store = new EntityStore(myEnv, "EntityStore", myStoreConfig);	
	}
	
	/**
	 * Returns a handle to the entity store
	 * @return
	 */
	public EntityStore getEntityStore(){
		return store;
	}
	
	/**
	 * Returns a handle to the environment
	 * @return
	 */
	public Environment getEnvironment(){
		return myEnv;
	}
	
	/**
	 * Close the store and environment
	 */
	public void close(){
		if(store != null){
			try {
				store.close();
			} catch(DatabaseException dbe) {
				System.err.println("Error closing store: " + dbe.toString());
				System.exit(-1);
			}
		}
		if (myEnv != null) {
			try {
				// Finally, close the environment.
				myEnv.close();
			} catch(DatabaseException dbe) {
				System.err.println("Error closing MyDbEnv: " + dbe.toString());
				System.exit(-1);
			}
		}
	}
}
