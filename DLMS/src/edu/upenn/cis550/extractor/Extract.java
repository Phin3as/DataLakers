package edu.upenn.cis550.extractor;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.utils.Constants;

public class Extract extends Thread {
	
	private String docName;
	private String accessType;
	private String user;
//	private boolean isFolder;
	
	public Extract(String docName, String accessType, String user){
		this.docName = docName;
		this.accessType = accessType;
		this.user = user;
//		this.isFolder = isFolder;
	}
	
	public void run(){
		
		ExtractFields e = ExtractFields.getInstance();
		StorageAPI store = new StorageAPI(new File(Constants.PATH_STORAGE));
		String file = Constants.PATH_FILES + "/" + docName;
		final File folder = new File(file);
		try {
			getFiles(folder, e, store);
		} catch (IOException | SAXException | TikaException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			System.out.println("Not compatible");
		}
		
		store.closeDB();
	}
	
	
	public static void main(String args[]) {
		
		(new Extract("", "PUBLIC", "ADMIN")).start();
		
	}
	
	public void getFiles(final File folder, ExtractFields e, StorageAPI store) throws JsonProcessingException, IOException, SAXException, TikaException, ParserConfigurationException {
	    
		if (folder.listFiles() == null){
	    	
			System.out.println("Reading file : " + folder.getName());
			int docID = e.extract(folder, store);
			store.putDocument(docID, accessType, docName, user);
			store.putUserDoc(user, docID);
	    	
	    } else {
			for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            getFiles(fileEntry, e, store);
		        } else {
		        	System.out.println("Reading file : " + fileEntry.getName());
		        	int docID = e.extract(fileEntry, store);
		            store.putDocument(docID, accessType, docName, user);
		            store.putUserDoc(user, docID);
		        }
		    }
	    }
	}
}
