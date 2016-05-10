package edu.upenn.cis550.servlet;

import java.util.List;

public class SearchResult {
	
	private String name;
	private List<SearchResult> children;
	
	public SearchResult(String name, List<SearchResult> children){
		this.setName(name);
		this.setChildren(children);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SearchResult> getChildren() {
		return children;
	}

	public void setChildren(List<SearchResult> children) {
		this.children = children;
	}
}
