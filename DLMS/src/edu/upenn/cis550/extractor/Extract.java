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
		StorageAPI store = new StorageAPI(new File("E:/graph4"));
		e.setDataBase(store);
		
		final File folder = new File("E:/sampleFiles");
		try {
			getFiles(folder, e);
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
	
	public static void getFiles(final File folder, ExtractFields e) throws JsonProcessingException, IOException, SAXException, TikaException, ParserConfigurationException {
	    int i = 0;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            getFiles(fileEntry, e);
	        } else {
	        	System.out.println("Reading file : " + fileEntry.getName());
	            e.extract(fileEntry, i);
	        }
	        i = i + 1;
	    }
	}
}
