package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

/**
 * This class reads plain/text files and pdfs and extracts nodes 
 * and store them in database 
 * @author Sanidhya
 *
 */
public class ExtractText {
	
	private int dID;
	private File f;
	private String extension;
	private HashMap<Integer, Struct> map = new HashMap<Integer, Struct>();
	
	public void extractNode(int dID, File f, String ext) throws IOException, SAXException, TikaException{
		this.dID = dID;
		this.f = f;
		this.extension = ext;
		parseText();
		
	}
	
	private void parseText() throws IOException, SAXException, TikaException{
		
		Struct root = new Struct(ExtractFields.generateId(), 
 			 	 				 dID, 
 			 	 				 f.getName(),
 			 	 				 extension, 	
 			 	 				 null,
 			 	 				 -1,	
 			 	 				 null);
		
		root.setChildren(tokenize(root.getId()));
		map.put(root.getId(), root);
		
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
	
	
	
	private StringReader getReader() throws IOException, SAXException, TikaException{
		
//		Tika tika = new Tika();
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(f);
		ParseContext context =new ParseContext();
		
		//Go through the reference for detailed explanation of each argument
		parser.parse(inputstream, handler, metadata, context);
		StringReader reader = new StringReader(handler.toString());
		return reader;
	
	}
	
	private ArrayList<Integer> tokenize(int parentId) throws IOException, SAXException, TikaException{
		
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(getReader(), new CoreLabelTokenFactory(), "");
	    int i = 0;
	    ArrayList<Integer> children = new ArrayList<Integer>();
		while (ptbt.hasNext()) {
	      CoreLabel label = ptbt.next();
	      
	      Struct node = new Struct(ExtractFields.generateId(), 
	 			 	 			   dID, 
	 			 	 			   label.toString(),
	 			 	 			   "TOKEN", 
	 			 	 			   Integer.toString(i),
	 			 	 			   parentId,
	 			 	 			   null);
	      i = i + 1;
	      map.put(node.getId(), node);
	      children.add(node.getId());
//	      System.out.println(label);
	    }
	    return children;

	    
//	    System.out.println("Metadata===========================");
	    
//	    String[] metadataNames = metadata.names();
//	    System.out.println(Arrays.asList(metadataNames));

//	      for(String name : metadataNames) {		        
//	         System.out.println(name + ": " + metadata.get(name));
//	      }
	
	}
	
}
