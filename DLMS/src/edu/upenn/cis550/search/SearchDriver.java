package edu.upenn.cis550.search;

import java.util.ArrayList;
import java.util.List;

public class SearchDriver {

	public static void main(String[] args) {
		System.out.println("SearchDriver.main()::BEGIN");
		
		Search search = new Search();
		Object ret = search.searchGraph("","Jitesh");
		if (ret instanceof List<?>) {
			ArrayList<Integer> data = (ArrayList<Integer>)ret;
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
