package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.upenn.cis550.storage.StorageAPI;

public class test {
	
	public static void main(String args[]) throws IOException, SAXException, TikaException, ParserConfigurationException{
		ExtractFields e = ExtractFields.getInstance();
		StorageAPI store = new StorageAPI(new File("E:/graph"));
		e.setDataBase(store);
		
		final File folder = new File("E:/sampleFiles");
		getFiles(folder, e);
		
//		store.showAllDocs();
//		store.showAllNodes();
//		store.showAllWords();
		
		
		store.closeDB();
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