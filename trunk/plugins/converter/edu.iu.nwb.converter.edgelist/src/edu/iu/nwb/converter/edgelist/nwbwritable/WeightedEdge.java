package edu.iu.nwb.converter.edgelist.nwbwritable;

public class WeightedEdge extends Edge {

	private int source;
	private int target;
	private double weight;

	public WeightedEdge(int source, int target, double weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
	
	public String getNWBLine() {
		return "" + source + " " + target + " " + weight + "\n";
	}
}
