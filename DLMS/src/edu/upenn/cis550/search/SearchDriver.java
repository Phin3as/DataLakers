package edu.upenn.cis550.search;

import java.util.HashSet;
import java.util.Set;

public class SearchDriver {

	public static void main(String[] args) {
		System.out.println("SearchDriver.main()::BEGIN");
		
		Search search = new Search();
		Object ret = search.searchGraph("","Washington");
		if (ret instanceof Set<?>) {
			HashSet<Integer> data = (HashSet<Integer>)ret;
			for (Integer nodeID : data) {
				System.out.println(nodeID);
			}
			System.out.println("frequency : "+data.size());
			if ( data.contains(78827) ) {
				System.out.println("FOUND!");
			}
		}
		System.out.println("SearchDriver.main()::END");
	}

}
