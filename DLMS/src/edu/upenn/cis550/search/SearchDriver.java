package edu.upenn.cis550.search;

import java.util.ArrayList;
import java.util.List;

public class SearchDriver {

	public static void main(String[] args) {
		System.out.println("SearchDriver.main()::BEGIN");
		
		Search search = new Search();
		Object ret = search.searchGraph("","Systems");
		if (ret instanceof List<?>) {
			List<List<Integer>> paths = (List<List<Integer>>)ret;
			
			for (List<Integer> path : paths) {
				System.out.print("Output Path : ");
				for (Integer data : path) {
					System.out.print(data+" ");
				}
				System.out.println();
			}
			System.out.println("frequency : "+paths.size());
		}
		System.out.println("SearchDriver.main()::END");
	}

}
