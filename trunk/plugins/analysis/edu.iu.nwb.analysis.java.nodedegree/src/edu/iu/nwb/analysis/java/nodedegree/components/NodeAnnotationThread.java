package edu.iu.nwb.analysis.java.nodedegree.components;

import prefuse.data.Graph;
import prefuse.data.Table;

public class NodeAnnotationThread extends Thread{
	final private Graph originalGraph;
	private Graph targetGraph;
	private int start;
	private int end;
	private NodeDegreeAnnotator na;
	
	public NodeAnnotationThread(Graph orgGraph, Graph targetGraph, int start,int end, NodeDegreeAnnotator na){
		super();
		this.originalGraph = orgGraph;
		this.targetGraph = targetGraph;
		this.start = start;
		this.end = end;
		this.na = na;
	}

	public void run() {
		super.run();
		Table sourceNodeTable = this.originalGraph.getNodeTable();
		Table targetNodeTable = this.targetGraph.getNodeTable();
		
		int tenPercent = (int)Math.ceil((end-start)/10);
		int counter = 1;
		for(int i = start; i < end; i++){
			if(counter % tenPercent == 0){
				NodeDegreeAnnotator.updateProgress(this.na, counter);
			}
			
			for(int j = 0; j < sourceNodeTable.getColumnCount();j++){
				targetNodeTable.set(i, j, sourceNodeTable.get(i, j));
			}
			
			targetNodeTable.setInt(i, "degree", originalGraph.getDegree(i));
			counter++;
		}
		
		
	}
	
	
	

}
