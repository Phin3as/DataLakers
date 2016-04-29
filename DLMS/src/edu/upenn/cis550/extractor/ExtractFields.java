package edu.upenn.cis550.extractor;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tika.*;
import org.apache.tika.exception.*;
import org.apache.tika.io.*;
import org.apache.tika.parser.*;
import org.apache.tika.sax.*;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tika.metadata.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.simple.*;

/**
 * This is the main class for extracting nodes from the document
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
