package edu.iu.nwb.analysis.extractattractors.components;

import java.util.LinkedHashSet;

public class SimpleStateSpaceGraph {
	int[] outNeighbor;
	LinkedHashSet[] inNeighbors;
	int labelSize;
	
	public void createEdgeLists(int size){
		outNeighbor = new int[size];
		inNeighbors = new LinkedHashSet[size];
	}
	
	public void setLabelSize(int size){
		this.labelSize = size;
	}
	public Integer getOutNeighbor(int index){
		return new Integer(outNeighbor[index]);
	}
	
	public void addEdge(int source, int target){
		outNeighbor[source] = target;
		LinkedHashSet inNeighborList = inNeighbors[target];
		if(inNeighborList == null){
			inNeighborList = new LinkedHashSet();
		}
		inNeighborList.add(new Integer(source));
		
		inNeighbors[target] = inNeighborList;
		
	}

	
	public int getSize(){
		return inNeighbors.length;
	}
	

}
