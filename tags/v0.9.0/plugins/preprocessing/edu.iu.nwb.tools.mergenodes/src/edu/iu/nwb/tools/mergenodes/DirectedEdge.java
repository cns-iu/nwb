package edu.iu.nwb.tools.mergenodes;

import java.util.ArrayList;
import prefuse.data.Node;

public class DirectedEdge {
	private Node source;
	private Node target;
	
	public DirectedEdge(Node source, Node target){
		this.source = source;
		this.target = target;
	}
	
	public int hashCode(){
		return source.hashCode()+ 66*target.hashCode();
	}
	
	public Node getSouceNode(){
		return source;
	}
	
	public Node getTargetNode(){
		return target;
	}
	
	public boolean equals(DirectedEdge edge){
		if (source == edge.getSouceNode() && 
			target == edge.getTargetNode())
			return true;
		else
			return false;
	}
}