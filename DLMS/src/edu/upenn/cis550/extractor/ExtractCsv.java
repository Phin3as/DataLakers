package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

/**
 * This class reads CSV files and extracts nodes 
 * and store them in database 
 * @author Sanidhya
 *
 */
public class ExtractCsv {
	
	private int dID;
	private File f;
	private String extension;
	private HashMap<Integer, Struct> map = new HashMap<Integer, Struct>();
	private CSVParser parser;
	
	public void extractNode(int dID, File f, String ext) throws IOException, ParserConfigurationException, SAXException{
		this.dID = dID;
		this.f = f;
		this.extension = ext;
		parseCsv();
		
	}
	
	private void parseCsv() throws IOException{
		Reader reader = new FileReader(f);
		parser = new CSVParser(reader, CSVFormat.DEFAULT.withAllowMissingColumnNames());
		List<CSVRecord> list = parser.getRecords();
//		Iterator<CSVRecord> records = parser.iterator();
		
		ArrayList<Integer> fileChildren = new ArrayList<Integer>();
		ArrayList<Integer> headChildren = new ArrayList<Integer>();
		Struct fileNode = new Struct(ExtractFields.generateId(), 
									 dID, 
									 f.getName(),
									 extension, 
									 null,
									 -1,
									 null);
		
		
		for(int i = 0; i < list.size(); i++){
			CSVRecord record = list.get(i);
			String type;
			String name;
			
			if(i == 0){
				type = "HEAD";
				name = "Head";
			}else {
				type = "RECORD";
				name = "REC:" + Integer.toString(i);
			}
			
			Struct headNode = new Struct(ExtractFields.generateId(), 
					 					 dID, 
					 					 name,
					 					 type, 
					 					 null, //value
					 					 fileNode.getId(),
					 					 null);
			
			fileChildren.add(headNode.getId());
			
			Iterator<String> values = record.iterator();
			int j = 0;
			while(values.hasNext()){
				Struct recNode = new Struct(ExtractFields.generateId(), 
	 					 					dID, 
	 					 					values.next().toString(),
	 					 					"RECORDVALUE", 
	 					 					Integer.toString(j), //value
	 					 					headNode.getId(),
	 					 					null);
				map.put(recNode.getId(), recNode);
				headChildren.add(recNode.getId());
				j = j + 1;
			}
			
			headNode.setChildren(headChildren);
			map.put(headNode.getId(), headNode);
			headChildren = new ArrayList<Integer>();
		}
		
		fileNode.setChildren(fileChildren);
		map.put(fileNode.getId(), fileNode);
		for (CSVRecord rec : parser) {
			System.out.println(rec.toString());
		}
		
		// Visualize
		for(int i : map.keySet()){
			System.out.println(i + "------->");
			System.out.println("\tNode id :"  + map.get(i).getId());
			System.out.println("\tDoc Id :"  + map.get(i).getDocumentID());
			System.out.println("\tName :"  + map.get(i).getName());
			System.out.println("\tType :"  + map.get(i).getType());
			System.out.println("\tValue :"  + map.get(i).getValue());
			System.out.println("\tParent :"  + map.get(i).getParent());
			System.out.print("\tChildren : ");
			
			if(!(map.get(i).getChildren() == null)){
				for (int j : map.get(i).getChildren()){
					System.out.print(j + " " );
				}
				System.out.println("\t");
			}else{
				System.out.println("None");
			}
			
		}
		
		// Visualization ends
		
	}
	
}
