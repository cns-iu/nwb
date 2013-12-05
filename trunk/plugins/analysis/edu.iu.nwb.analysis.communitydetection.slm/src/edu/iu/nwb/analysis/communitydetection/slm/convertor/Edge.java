package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.util.Map;

public class Edge {

	Node s = null;
	Node t = null;
	Map<String, Object> attributes = null;

	public Edge(Node s, Node t, Map<String, Object> attributes) {
		this.s = s;
		this.t = t;
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

	
}
