package edu.upenn.cis550.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.upenn.cis550.linker.Linker;
import edu.upenn.cis550.storage.StorageAPI;

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
	private static int id; // this is for node id, change it to sensible name
	private int documentID;
	private File file;
	private StorageAPI store;
	
	//Extractor Objects
	private ExtractJson json = new ExtractJson();
	private ExtractText text = new ExtractText();
	private ExtractXml xml = new ExtractXml();
	private ExtractCsv csv = new ExtractCsv();
	
	//Linker Objects
	private Linker link = new Linker();
	
	private ExtractFields(){ }
	   
	public static ExtractFields getInstance( ) {
		if (instance == null){
			instance = new ExtractFields();
		}
		return instance;	
	}
	   
	public void setDataBase(StorageAPI db){
		this.store = db;
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
	
	public void extract(File f, StorageAPI store) throws JsonProcessingException, IOException, SAXException, TikaException, ParserConfigurationException{
		this.file = f;
		this.store = store;
//		this.documentID = dID;
		this.documentID = store.getLastDocID() + 1;
		this.id = store.getLastNodeID();
		
		String ext = extension();
		if(ext.equals("json")){
			ArrayList<Integer> nodes = json.extractNode(documentID, file, extension(), store);
			store.putForwardIndex(documentID, nodes);
			link.linker(store, documentID);
		} else if(ext.equals("plain") || ext.equals("pdf")){
			ArrayList<Integer> nodes = text.extractNode(documentID, file, extension(), store);
			store.putForwardIndex(documentID, nodes);
			link.linker(store, documentID);
		} else if(ext.equals("xml") || ext.equals("html")){
			ArrayList<Integer> nodes = xml.extractNode(documentID, file, extension(), store);
			store.putForwardIndex(documentID, nodes);
			link.linker(store, documentID);
		} else if(ext.equals("csv")){
			ArrayList<Integer> nodes = csv.extractNode(documentID, file, extension(), store);
			store.putForwardIndex(documentID, nodes);
			link.linker(store, documentID);
		} else {
			
			System.out.println(extension() + " Not Supported at the moment! Try back Later!");
		}
		
		store.putLastDocID(documentID);
		store.putLastNodeID(id);
	}
	
	public static int generateId(){
		id = id + 1;
		return id;
	}
	
}
