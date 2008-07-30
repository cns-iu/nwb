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
		int degreeType = na.getDegreeType();
		
		String label = null;
		if(degreeType == DegreeType.inDegree){
			label = "inDegree";
		}
		if(degreeType == DegreeType.outDegree){
			label = "outDegree";
		}
		if(degreeType == DegreeType.totalDegree){
			label = "totalDegree";
		}
		Table sourceNodeTable = this.originalGraph.getNodeTable();
		Table targetNodeTable = this.targetGraph.getNodeTable();
		
		int tenPercent = (int)Math.ceil((end-start)/10);
		if(tenPercent == 0){
			tenPercent = 1;
		}
		int counter = 1;
		for(int i = start; i < end; i++){
			if(counter % tenPercent == 0){
				NodeDegreeAnnotator.updateProgress(this.na, counter);
			}
			
			for(int j = 0; j < sourceNodeTable.getColumnCount();j++){
				targetNodeTable.set(i, j, sourceNodeTable.get(i, j));
			}
			
			if(degreeType == DegreeType.totalDegree)
				targetNodeTable.setInt(i, label, originalGraph.getDegree(i));
			else if(degreeType == DegreeType.inDegree)
				targetNodeTable.setInt(i, label, originalGraph.getInDegree(i));
			else if(degreeType == DegreeType.outDegree)
				targetNodeTable.setInt(i, label, originalGraph.getOutDegree(i));
			counter++;
		}
		
		
	}
	
	
	

}
