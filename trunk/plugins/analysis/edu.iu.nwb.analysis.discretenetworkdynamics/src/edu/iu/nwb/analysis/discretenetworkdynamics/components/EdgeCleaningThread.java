package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.util.Iterator;

import prefuse.data.Edge;
import prefuse.data.Graph;


public class EdgeCleaningThread extends Thread{

	private Graph stateGraph;
	
	public EdgeCleaningThread(Graph g){
		this.stateGraph = g;
	}
	
	public void run(){
		for(Iterator it = this.stateGraph.edges(); it.hasNext();){
			Edge e = (Edge)it.next();
			if(e.getInt("source") == -1){
				this.stateGraph.removeEdge(e);
			}
		}
	}
}
