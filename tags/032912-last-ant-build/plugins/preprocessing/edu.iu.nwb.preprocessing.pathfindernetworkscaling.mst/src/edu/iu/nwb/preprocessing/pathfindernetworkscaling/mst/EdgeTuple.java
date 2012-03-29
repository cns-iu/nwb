package edu.iu.nwb.preprocessing.pathfindernetworkscaling.mst;

/*
 * Used to create edge tuples having information about 
 * nodeID, node1, node2, weight.
 * */
public class EdgeTuple implements Comparable<EdgeTuple> {
	private int nodeID;
	private int node1;
    private int node2;
    private double weight;
    
    public EdgeTuple(int nodeID, int node1, int node2, double weight) {
    	
    	this.nodeID = nodeID;
    	this.node1 = node1;
    	this.node2 = node2;
    	this.weight = weight;
    
    }

    public int compareTo(EdgeTuple nextEdge) {
    	if(this.weight > nextEdge.weight) {
    		return 1;
    	}
    	else if(this.weight < nextEdge.weight) {
    		return -1;
    	}
    	else {
    		return 0;
    	}
    }

    /**
	 * @return the nodeID
	 */
	public int getNodeID() {
		return nodeID;
	}
	
    /**
	 * @return the node1
	 */
	public int getNode1() {
		return node1;
	}

	/**
	 * @return the node2
	 */
	public int getNode2() {
		return node2;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
}