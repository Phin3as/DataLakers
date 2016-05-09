package edu.upenn.cis550.search;

import java.util.List;

public class SearchDriver {

	public static void main(String[] args) {
		System.out.println("SearchDriver.main()::BEGIN");
		
		long startTime = System.currentTimeMillis();

		Search search = new Search();
		Object ret = search.searchGraph("","obliterate mani");
		if (ret instanceof List<?>) {
			List<List<String>> paths = (List<List<String>>)ret;
			
			for (List<String> path : paths) {
				System.out.print("Output Path : ");
				for (String data : path) {
					System.out.println(data);
				}
				System.out.println();
			}
			System.out.println("frequency : "+paths.size());
		}
		System.out.println("SearchDriver.main()::END");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);

	}

}
