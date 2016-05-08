package edu.upenn.cis550.utils;

import edu.smu.tspell.wordnet.*; 

public class Synonyms {
	
	public static void main(String[] args){ 
	
		NounSynset nounSynset; 
		NounSynset[] hyponyms; 
	
		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		Synset[] synsets = database.getSynsets("fly", SynsetType.NOUN); 
		System.out.println("Not Woorking");
		for (int i = 0; i < synsets.length; i++) { 
		    
			nounSynset = (NounSynset)(synsets[i]); 
		    hyponyms = nounSynset.getHyponyms(); 
		    System.err.println(nounSynset.getWordForms()[0] + 
		            ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms"); 
		
		}
	
	}
}
