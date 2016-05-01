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
//		System.out.println(e.extension());
		StorageAPI store = new StorageAPI(new File("E:/graph"));
		e.setDataBase(store);
		
		final File folder = new File("E:/sampleFiles");
		getFiles(folder, e);
		
		
		
		
//		File file = new File("E:/Java_Workspace/DLMS/Tiwari_Sanidhya.txt");
//		e.extract(file, 1);
//		File file2 = new File("E:/Java_Workspace/DLMS/sample.json");
//		e.extract(file2, 2);
//		File file3 = new File("E:/Java_Workspace/DLMS/sample.xml");
//		e.extract(file3, 3);
//		File file4 = new File("E:/Java_Workspace/DLMS/sample.csv");
//		e.extract(file4, 4);
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