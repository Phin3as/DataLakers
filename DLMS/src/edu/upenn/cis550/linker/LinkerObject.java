package edu.upenn.cis550.linker;

public class LinkerObject {
	Integer node1;
	Integer node2;
	
	public LinkerObject(Integer node1, Integer node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Integer getNode1() {
		return node1;
	}
	public void setNode1(Integer node1) {
		this.node1 = node1;
	}
	public Integer getNode2() {
		return node2;
	}
	public void setNode2(Integer node2) {
		this.node2 = node2;
	}
}
