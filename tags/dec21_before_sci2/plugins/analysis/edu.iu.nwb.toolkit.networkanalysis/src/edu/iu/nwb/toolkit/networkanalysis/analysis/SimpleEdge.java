package edu.iu.nwb.toolkit.networkanalysis.analysis;

import prefuse.data.Edge;

public class SimpleEdge {
	int source;
	int target;
	
	public SimpleEdge(int i, int j){
		source = i;
		target = j;
	}
	
	public SimpleEdge(Edge e){
		source = e.getSourceNode().getRow();
		target = e.getTargetNode().getRow();
	}
	
	public boolean equals(Object o){
		boolean b = false;
		
		if(o.getClass().equals(this.getClass())){
			SimpleEdge se = (SimpleEdge)o;
			
			if((se.source == this.source) && (se.target == this.target))
				b = true;
		}
		
		return b;
			
	}
	
	public int hashCode(){
		String s = source + " " + target;
		return s.hashCode();
	}
	
	public int getSource(){
		return source;
	}
	
	public int getTarget(){
		return target;
	}
	
	public String toString(){
		String s = "Source: " + source + " Target: " + target;
		return s;
	}
	
}
