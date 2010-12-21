package edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects;

import java.util.LinkedHashMap;

public class Node {

	private int nodeID;
	private String nodeLabel;
	private LinkedHashMap <String, Object> attributeNameToValue = 
			new LinkedHashMap<String, Object>();
	
	public Node(int nodeID, String nodeLabel) {
		this.nodeID = nodeID;
		this.nodeLabel = nodeLabel;
	}
	
	public void addAttribute(String attributeName, Object attributeValue) {
		this.attributeNameToValue.put(attributeName, attributeValue);
	}
	
	public int getID() {
		System.err.println(nodeID);
		return nodeID;
	}

	public String getLabel() {
		if (nodeLabel != null) {
			return nodeLabel;
		} else {
			return "NA";
		}
	}
	
	public LinkedHashMap<String, Object> getAttributes() {
		return attributeNameToValue;
	}
	
	public Object getAttribute(String attributeName) {
		return attributeNameToValue.get(attributeName);
	}
	
}
