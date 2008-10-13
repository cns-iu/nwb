package edu.iu.nwb.converter.edgelist.nwbwritable;

public class UnweightedEdge extends Edge {

	private int source;
	private int target;
	
	public UnweightedEdge(int source, int target) {
		this.source = source;
		this.target = target;
	}

	public String getNWBLine() {
		return "" + source + " " + target + "\n";
	}
	
	
}
