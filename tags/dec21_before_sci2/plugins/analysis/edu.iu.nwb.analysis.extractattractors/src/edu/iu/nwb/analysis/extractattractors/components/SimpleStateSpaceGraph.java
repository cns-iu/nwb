package edu.iu.nwb.analysis.extractattractors.components;

import java.math.BigInteger;
import java.util.HashSet;

import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigHashSetVector;

public abstract class SimpleStateSpaceGraph {
	int labelSize;
	int nodeStates;
	BigInteger maxSize = BigInteger.ZERO;
	
	BigInteger graphSize = BigInteger.ZERO;
	
	
	
	public SimpleStateSpaceGraph(int nodeStates, int labelSize){
		this.labelSize = labelSize;
		this.nodeStates = nodeStates;
		this.maxSize = new BigInteger(new Integer(nodeStates).toString()).pow(labelSize);
	}
	
	public void createEdgeLists(BigInteger size){
		this.graphSize = size;
	}
	
	public void setLabelSize(int size){
		this.labelSize = size;
	}

	
	public abstract void addEdge(BigInteger source, BigInteger target);
	
	public abstract BigBigIntegerArray getOutNeighbors();
	
	public abstract int[] getSmallOutNeighbors();
	
	public abstract BigInteger getOutNeighbor(BigInteger source);
	
	
	public abstract HashSet<Integer> getSmallInNeighbors(int target);
	
	public abstract BigHashSetVector getBigInNeighbors(BigInteger target);
	
	public BigInteger getSize(){
		return this.graphSize;
	}
	

	

}
