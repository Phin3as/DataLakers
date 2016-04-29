package edu.upenn.cis550.extractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.tika.Tika;
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
 * This class reads plain/text files and pdfs
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
		tokenize();
		
	}
	
	private StringReader parseText() throws IOException, SAXException, TikaException{
		
		Tika tika = new Tika();
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
	
	private void tokenize() throws IOException, SAXException, TikaException{
		
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer(parseText(), new CoreLabelTokenFactory(), "");
	    while (ptbt.hasNext()) {
	      CoreLabel label = ptbt.next();
	      System.out.println(label);
	    }
	    
//	    System.out.println("Metadata===========================");
	    
//	    String[] metadataNames = metadata.names();
//	    System.out.println(Arrays.asList(metadataNames));

//	      for(String name : metadataNames) {		        
//	         System.out.println(name + ": " + metadata.get(name));
//	      }
	}

	
}
