package edu.upenn.cis550.utils;

public class test {
	
	
	
	public static void main(String[] args){
		
		Stemmer s = new Stemmer();
		Synonyms sy = new Synonyms();
		
		
		
		System.out.println(s.stemWord("Synchronous"));
		System.out.println(s.stemWord("killing"));
		System.out.println(s.stemWord("pissing"));
		
		
		sy.getSynonyms("pepsi");
		
	} 
}
