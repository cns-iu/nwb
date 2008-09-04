package edu.iu.nwb.analysis.extractattractors.components;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class SimpleStateSpaceGraph {
	HashMap outNeighbor;
	HashMap inNeighbors;
	int labelSize;
	
	public void createEdgeLists(int size){
		outNeighbor = new HashMap(size);
		inNeighbors = new HashMap(size);
	}
	
	public void setLabelSize(int size){
		this.labelSize = size;
	}
	public Integer getOutNeighbor(int index){
		return (Integer)outNeighbor.get(new Integer(index));
	}
	
	public void addEdge(int source, int target){
		outNeighbor.put(new Integer(source), new Integer(target));
		LinkedHashSet inNeighborList = (LinkedHashSet)inNeighbors.get(new Integer(target));
		if(inNeighborList == null){
			inNeighborList = new LinkedHashSet();
		}
		inNeighborList.add(new Integer(source));
		
		inNeighbors.put(new Integer(target), inNeighborList);
		
	}
	
	public HashMap getOutNeighbors(){
		return this.outNeighbor;
	}
	
	public LinkedHashSet getInNeighbors(int target){
		return (LinkedHashSet)this.inNeighbors.get(new Integer(target));
	}

	
	public int getSize(){
		return this.outNeighbor.size();
	}
	

}
