package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This class handles the File, identify suitable parser 
 * and extract nodes for the graph accordingly
 * This is a singleton class. Use getInstance method to get an object.
 * For extracting the nodes, only object of this class is needed.
 * @author Sanidhya
 *
 */
public class ExtractFields {
	
	private static ExtractFields instance = new ExtractFields( );
	
	private static int id = 0; // this is for node id, change it to sensible name
	private int documentID;
	private File file;
	
	//Extractor Objects
	private ExtractJson json = new ExtractJson();
	private ExtractText text = new ExtractText();
	private ExtractXml xml = new ExtractXml();
	private ExtractCsv csv = new ExtractCsv();
	
	private ExtractFields(){ }
	   
	public static ExtractFields getInstance( ) {
		if (instance == null){
			instance = new ExtractFields();
		}
		return instance;	
	}
	   
	public int getDocumentID() {
		return documentID;
	}

	public File getFile() {
		return file;
	}
	
	public int getId() {
		return id;
	}
	
	public String extension(){
		Tika tika = new Tika();
		try {
			String temp = tika.detect(file);
			return temp.split("/")[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "NA";
	}
	
	public void extract(File f, int dID) throws JsonProcessingException, IOException, SAXException, TikaException, ParserConfigurationException{
		this.file = f;
		this.documentID = dID;
		
		String ext = extension();
		if(ext.equals("json")){
			json.extractNode(documentID, file, extension());
		} else if(ext.equals("plain")){
			text.extractNode(documentID, file, extension());
		} else if(ext.equals("html")){
			xml.extractNode(documentID, file, extension());
//			System.out.println(extension());
		}else if(ext.equals("csv")){
			csv.extractNode(documentID, file, extension());
		}else {
			System.out.println(extension() + " Not Supported at the moment! Try back Later!");
		}
	}
	
	public static int generateId(){
		id = id + 1;
		return id;
	}
	
}
