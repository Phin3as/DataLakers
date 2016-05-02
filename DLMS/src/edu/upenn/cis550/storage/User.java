package edu.upenn.cis550.storage;

import java.util.HashSet;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class User {
	@PrimaryKey
	private String user;
	
	private HashSet<Long> docs = new HashSet<Long>();
	
	public User(){
		
	}
	
	public User(String user, Long docID){
		this.user = user;
		this.docs.add(docID);
	}
	
	public String getUser(){
		return user;
	}
	
	public HashSet<Long> getDocs(){
		return docs;
	}
}
