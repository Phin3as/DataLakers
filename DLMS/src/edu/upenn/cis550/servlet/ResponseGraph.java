package edu.upenn.cis550.servlet;

import java.util.List;

public class ResponseGraph {
	private String name = "Top Results";
	private List<SearchResult> children;
	
	public ResponseGraph(String name, List<SearchResult> children){
		this.name = name;
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
