package edu.upenn.cis550.storage;

import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Document {

	@PrimaryKey
	private int docID;
	private String docType;
	private HashSet<String> users = new HashSet<String>();
	
	public Document(){
		
	}
	
	public Document(int docID, String type, String user){
		this.docID = docID;
		this.docType = type;
		if(type.equals("PRIVATE")){
			this.users.add(user);
		}
	}
	
	public int getDocID(){
		return docID;
	}
	
	public String getDocType(){
		return docType;
	}
	
	public HashSet<String> getUsers(){
		return users;
	}
}
