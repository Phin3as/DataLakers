package edu.upenn.cis550.utils;

import java.util.Arrays;
import java.util.HashSet;

import edu.smu.tspell.wordnet.*; 

public class Synonyms {
	
	private WordNetDatabase database = WordNetDatabase.getFileInstance(); 
	
	public HashSet<String> getSynonyms(String word){
		
		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		HashSet<String> result = new HashSet<String>();
		Synset[] synsets = database.getSynsets(word); 
		
		for(Synset synonym : synsets){
			for(String foo : synonym.getWordForms()){
				result.add(foo);
			}
		}
		
		System.out.println(Arrays.toString(result.toArray()));
		return result;
	}
	
}
