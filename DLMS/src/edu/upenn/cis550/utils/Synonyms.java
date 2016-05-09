package edu.upenn.cis550.utils;

import java.util.Arrays;
import java.util.HashSet;

import edu.smu.tspell.wordnet.*; 

public class Synonyms {
	
	private WordNetDatabase database = WordNetDatabase.getFileInstance(); 
	
	public HashSet<String> getSynonyms(String word){
		
		
		try {
		System.setProperty("wordnet.database.dir", Constants.PATH_DICT);
		} catch (Exception e){
			return null;
		}
		
		
		HashSet<String> result = new HashSet<String>();
		Synset[] synsets = database.getSynsets(word); 
		if(isSingleWord(word))
			result.add(word.toLowerCase());
		for(Synset synonym : synsets){
			for(String foo : synonym.getWordForms()){
				if(isSingleWord(foo))
					result.add(foo.toLowerCase());
			}
		}
		
		System.out.println(Arrays.toString(result.toArray()));
		return result;
	}
	
	private boolean isSingleWord(String word){
		
		return word.split(" ").length < 2;
		
	}
	
}
