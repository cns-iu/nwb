package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.util.Map;

public class Edge {
	public static String VOS_SEPARATOR = "\t";
	private Node s = null;
	private Node t = null;
	private double weight = 1;
	private Map<String, Object> attributes = null;

	public Edge(Node s, Node t, Map<String, Object> attributes) {
		this.s = s;
		this.t = t;
		this.attributes = attributes;
	}
	
	public Edge(Node s, Node t, Map<String, Object> attributes, double weight) {
		this.s = s;
		this.t = t;
		this.weight = weight;
		this.attributes = attributes;
	}

	public Node getSource() {
		return s;
	}

	public Node getTarget() {
		return t;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public String toVosString() {
		return s.getNewID() + VOS_SEPARATOR + t.getNewID() + VOS_SEPARATOR + weight;
	}

	
}
