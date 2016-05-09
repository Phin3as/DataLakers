package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upenn.cis550.storage.StorageAPI;

/**
 * This class reads JSON files and extracts nodes 
 * and store them in database 
 *
 * @author Sanidhya
 *
 */
public class ExtractJson {
	
	private int dID;
	private File f;
	private String extension;
	private StorageAPI store;
	private HashMap<Integer, Struct> map = new HashMap<Integer, Struct>();
	private ArrayList<Integer> nodes = new ArrayList<Integer>();
	
	public ArrayList<Integer> extractNode(int dID, File f, String ext, StorageAPI db) throws JsonProcessingException, IOException{
		this.dID = dID;
		this.f = f;
		this.extension = ext;
		this.store = db;
		parseJson();
		return nodes;
	}
	
	private void parseJson() throws JsonProcessingException, IOException{
		JsonFactory fac = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(fac);
		JsonNode root = mapper.readTree(f);
		
		Struct node = new Struct(ExtractFields.generateId(), 
	 			 			 	 dID, 
	 			 			 	 f.getName(),
	 			 			 	 extension, 
	 			 			 	 null,
	 			 			 	 -1,
	 			 			 	 null);
    	node.setChildren(extractJsonNode(root, node.getId(), dID));
    	store.putGraphNode(node);
    	nodes.add(node.getId());
    	map.put(node.getId(), node);
		
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
	}
	
	private ArrayList<Integer> extractJsonNode(JsonNode json, int parentId, int docId) throws JsonProcessingException, IOException{
		
		if(json.isArray()){

			ObjectMapper mapper = new ObjectMapper();
			JsonNode[] valset = mapper.readValue(json.toString(), JsonNode[].class);
			
			ArrayList<Integer> children = new ArrayList<Integer>();
			for(JsonNode val : valset){
				
				String stringName = val.toString();
		    	stringName = stringName.replaceAll("^\"|\"$", "");
		    	
				Struct node = new Struct(ExtractFields.generateId(), 
						 			 	 docId, 
						 			 	 stringName,
						 			 	 val.getNodeType().toString(), 
						 			 	 null,
						 			 	 parentId,
						 			 	 null);
				node.setChildren(extractJsonNode(val, node.getId(), docId));
				children.add(node.getId());
		    	store.putGraphNode(node);
		    	nodes.add(node.getId());
		    	map.put(node.getId(), node);
			}
			
			return children;

		}
		
		if(json.getNodeType().toString().equals("STRING")){
			return null;
		}
		
		if(json.getNodeType().toString().equals("BOOLEAN")){
			return null;
		}
		
		if(json.isNull()){
			return null;
		}
		
		JsonFactory fac = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(fac);
		JsonNode root = mapper.readTree(json.toString());
		
		ArrayList<Integer> children = new ArrayList<Integer>();
		Iterator<Map.Entry<String,JsonNode>> fieldsIterator = root.fields();
	    while (fieldsIterator.hasNext()) {
	    	Map.Entry<String,JsonNode> field = fieldsIterator.next();
	    	String stringValue = field.getValue().toString();
	    	stringValue = stringValue.replaceAll("^\"|\"$", "");
	    	String stringName = field.getKey();
	    	stringName = stringName.replaceAll("^\"|\"$", "");
	    	Struct node = new Struct(ExtractFields.generateId(), 
	    						 	 docId, 
	    						 	 stringName,
	    						 	 field.getValue().getNodeType().toString(), 
	    						 	 stringValue,
	    						 	 parentId,
	    						 	 null);
	    	
	    	children.add(node.getId());
	    	node.setChildren(extractJsonNode(field.getValue(), node.getId(), docId));
	    	store.putGraphNode(node);
	    	nodes.add(node.getId());
	    	map.put(node.getId(), node);
	    }
	    
	    return children;
	}

}
