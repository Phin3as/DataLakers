package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import edu.upenn.cis550.storage.StorageAPI;

public class test {
	
	public static void main(String args[]) throws IOException, SAXException, TikaException, ParserConfigurationException{
		ExtractFields e = ExtractFields.getInstance();
//		System.out.println(e.extension());
		StorageAPI store = new StorageAPI(new File("E:/graph"));
		e.setDataBase(store);
		File file = new File("E:/Java_Workspace/DLMS/Tiwari_Sanidhya.txt");
		e.extract(file, 1);
		File file2 = new File("E:/Java_Workspace/DLMS/sample.json");
		e.extract(file2, 2);
		File file3 = new File("E:/Java_Workspace/DLMS/sample.xml");
		e.extract(file3, 3);
		File file4 = new File("E:/Java_Workspace/DLMS/sample.csv");
		e.extract(file4, 4);
		store.closeDB();
	}
}