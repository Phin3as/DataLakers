package edu.upenn.cis550.servlet;

public class ListDocuments {
	
	private int docID;
	private String docName;
	
	public ListDocuments(int docID, String docName){
		this.docID = docID;
		this.docName = docName;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
