package edu.iu.nwb.analysis.pagerank.weighted;

public class Edge {

	private int source;
	private int target;
	private double weight;

	public Edge(int source, int target, double weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

	public double getWeight() {
		return weight;
	}

	
}
