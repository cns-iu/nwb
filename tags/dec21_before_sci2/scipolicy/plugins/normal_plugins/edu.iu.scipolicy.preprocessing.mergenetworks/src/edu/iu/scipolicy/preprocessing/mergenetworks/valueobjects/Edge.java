package edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects;

import java.util.LinkedHashMap;

public class Edge {

	private int sourceID;
	private int targetID;
	private LinkedHashMap <String, Object> attributeNameToValue = 
			new LinkedHashMap<String, Object>();
	
	public Edge(int sourceID, int targetID) {
		this.sourceID = sourceID;
		this.targetID = targetID;
	}
	
	public void addAttribute(String attributeName, Object attributeValue) {
		this.attributeNameToValue.put(attributeName, attributeValue);
	}
	
	public int getSourceID() {
		return this.sourceID;
	}

	public int getTargetID() {
		return this.targetID;
	}
	
	public LinkedHashMap<String, Object> getAttributes() {
		return attributeNameToValue;
	}
	
	public Object getAttribute(String attributeName) {
		return attributeNameToValue.get(attributeName);
	}
	
}
