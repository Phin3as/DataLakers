package edu.upenn.cis550.utils;

public class test {
	
	
	
	public static void main(String[] args){
		
		Stemmer s = new Stemmer();
		
		System.out.println(s.stemWord("Synchronous"));
		System.out.println(s.stemWord("Synchronizing"));
		System.out.println(s.stemWord("pissing"));
		
		
	} 
}