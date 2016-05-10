package edu.upenn.cis550.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upenn.cis550.search.Search;
import edu.upenn.cis550.utils.Constants;

public class SearchServlet extends HttpServlet {
	static final long serialVersionUID = 455555003;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("Search Request Received!!");
		String query = request.getParameter("query");
		String user = request.getParameter("user");
		System.out.println(query);
		System.out.println(user);
		
		Search search = new Search();
		List<List<String>> searchResults = search.searchGraph(user, query.trim());
		
		/** Creating Response Here **/
		ObjectMapper mapper = new ObjectMapper();
		List<SearchResult> results = getAllPaths(searchResults);
		ResponseGraph rg = new ResponseGraph("Top Results",results);
		response.setContentType("application/json");
		try {
			String responseJSON = mapper.writeValueAsString(rg);
			System.out.println(responseJSON);
			response.getWriter().write(responseJSON);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<SearchResult> getAllPaths(List<List<String>> results){
		List<SearchResult> paths = new ArrayList<SearchResult>();
		int count = 0;
		for(List<String> path : results){
			if(count >= Constants.MAX_RESULTS){
				break;
			}
			paths.add(createJSON(path, 0));
			count++;
		}
		return paths;
	}
	
	
	public SearchResult createJSON(List<String> results, int i){
		if(results.size() < i + 2){
			SearchResult s = new SearchResult(results.get(i), null);
			return s;
		}
		List<SearchResult> children = new ArrayList<SearchResult>();
		children.add(createJSON(results, i + 1));
		SearchResult s = new SearchResult(results.get(i), children);
		return s;
	}
	
}
