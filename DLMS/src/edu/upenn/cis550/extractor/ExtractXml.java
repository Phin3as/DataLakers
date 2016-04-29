package edu.upenn.cis550.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ExtractXml {
	
	private int dID;
	private File f;
	private String extension;
	private HashMap<Integer, Struct> map = new HashMap<Integer, Struct>();
	
	public void extractNode(int dID, File f, String ext) throws IOException, ParserConfigurationException, SAXException{
		this.dID = dID;
		this.f = f;
		this.extension = ext;
		parseXml();
		
	}
	
	private void parseXml() throws ParserConfigurationException, SAXException, IOException{
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
//		Element root = doc.getDocumentElement();
//		root.normalize();
		
		//Document Structure
		Struct node = new Struct(ExtractFields.generateId(), 
	 			 			 	 dID, 
	 			 			 	 f.getName(),
	 			 			 	 extension, 
	 			 			 	 null,
	 			 			 	 -1,
	 			 			 	 null);
		
		Node in = (Node) doc;
		node.setChildren(extractXmlNode(in, node.getId(), dID));
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
	
	private ArrayList<Integer> extractXmlNode(Node root, int parentId, int docId){
		
		if(!hasElementNode(root)){
			return null;
		}
		
		NodeList field = root.getChildNodes();
		ArrayList<Integer> children = new ArrayList<Integer>();
		for(int i = 0; i < field.getLength(); i++){
			field.item(i).normalize();
			Node temp = field.item(i);
			if(temp.getNodeType() == Node.ELEMENT_NODE){
				
				Element element = (Element) temp;
				temp.normalize();
				String value;
				if(hasElementNode(element)){
					value = null;
				} else {
					value = element.getTextContent();
				}
				
				Struct node = new Struct(ExtractFields.generateId(), 
						 			 	 docId, 
						 			 	 element.getNodeName(),
						 			 	 "ELEMENT", 
						 			 	 value,
						 			 	 parentId,
						 			 	 null);
				children.add(node.getId());
				
				ArrayList<Integer> attribChildren = extractAttributes(element, node.getId(), docId); 
				ArrayList<Integer> localChildren = extractXmlNode(element, node.getId(), docId); 
				if(localChildren == null && attribChildren == null){
					node.setChildren(null);
				}else if (localChildren != null && attribChildren == null){
					node.setChildren(localChildren);
				} else if (localChildren == null && attribChildren != null){
					node.setChildren(attribChildren);
				} else {
					localChildren.addAll(attribChildren);
					node.setChildren(localChildren);
				}
				map.put(node.getId(), node);
				System.out.println(node.getId() +" --- "+ map.get(node.getId()).getName());
				
			}
		}
		
		return children;
		
	}
	
	private ArrayList<Integer> extractAttributes(Element element, int parentId, int docId){
		
		//you could check this above as well
		if(!element.hasAttributes()){
			return null;
		}
		
		NamedNodeMap attrib = element.getAttributes();
		ArrayList<Integer> children = new ArrayList<Integer>();
		for(int i = 0; i < attrib.getLength(); i++){
			
			Struct node = new Struct(ExtractFields.generateId(), 
		 			 			 	 docId, 
		 			 			 	 attrib.item(i).getNodeName(),
		 			 			 	 "ATTRIBUTE", 
		 			 			 	 attrib.item(i).getNodeValue(),
		 			 			 	 parentId,
		 			 			 	 null);
			children.add(node.getId());
			map.put(node.getId(), node);
			
		}
		return children;
		
	}
	
	private boolean hasElementNode(Node element){
		if(!element.hasChildNodes()){
			return false;
		}
		
		NodeList temp = element.getChildNodes();
		for(int i = 0; i < temp.getLength(); i++){
			if(temp.item(i).getNodeType() == Node.ELEMENT_NODE){
				return true;
			}
		}
		
		return false;
	}
		
}
