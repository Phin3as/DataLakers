package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class test {
	
	public static void main(String args[]) throws IOException, SAXException, TikaException, ParserConfigurationException{
		ExtractFields e = ExtractFields.getInstance();
//		System.out.println(e.extension());
		File file = new File("E:/Java_Workspace/DLMS/Tiwari_Sanidhya.txt");
		e.extract(file, 1);
	}
}