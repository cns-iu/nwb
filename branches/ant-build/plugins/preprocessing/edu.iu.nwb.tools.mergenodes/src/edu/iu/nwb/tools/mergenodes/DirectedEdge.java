package edu.iu.nwb.tools.mergenodes;

import prefuse.data.Node;

public class DirectedEdge {
	private Node source;
	private Node target;
	
	public DirectedEdge(Node source, Node target){
		this.source = source;
		this.target = target;
	}
	
	public int hashCode(){
		//???
		return source.hashCode()+ 66*target.hashCode();
	}
	
	public Node getSouceNode(){
		return source;
	}
	
	public Node getTargetNode(){
		return target;
	}
	
	/* 
     * Overwrite equals(Object obj) method in the Object class
     */
	public boolean equals(Object obj){
		DirectedEdge edge=(DirectedEdge)obj;
		if (source.equals(edge.getSouceNode() )&& 
			target.equals(edge.getTargetNode()))
			return true;
		else
			return false;
	}
}