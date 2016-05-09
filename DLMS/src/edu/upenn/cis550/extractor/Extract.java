package edu.upenn.cis550.extractor;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.upenn.cis550.storage.StorageAPI;

public class Extract extends Thread {
	
	public void run(){
		
		ExtractFields e = ExtractFields.getInstance();
		StorageAPI store = new StorageAPI(new File("E:/graph10"));
//		e.setDataBase(store);
		
		final File folder = new File("E:/sampleFiles/1.json");
		try {
			getFiles(folder, e, store);
		} catch (IOException | SAXException | TikaException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		store.showAllDocs();
//		store.showAllNodes();
//		store.showAllWords();
		
		store.closeDB();
	}
	
	
	public static void main(String args[]) {
		
		(new Extract()).start();
		
	}
	
	public static void getFiles(final File folder, ExtractFields e, StorageAPI store) throws JsonProcessingException, IOException, SAXException, TikaException, ParserConfigurationException {
	    if (folder.listFiles() == null){
	    	e.extract(folder, store);
	    } else {
			for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            getFiles(fileEntry, e, store);
		        } else {
		        	System.out.println("Reading file : " + fileEntry.getName());
		            e.extract(fileEntry, store);
		        }
		    }
	    }
	}
}
