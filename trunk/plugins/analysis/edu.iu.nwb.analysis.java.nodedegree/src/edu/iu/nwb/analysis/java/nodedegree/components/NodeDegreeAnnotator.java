package edu.iu.nwb.analysis.java.nodedegree.components;

import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Table;

public class NodeDegreeAnnotator {
	private ProgressMonitor progMonitor;
	private int progress = 0;
	
	public NodeDegreeAnnotator(ProgressMonitor pm){
		this.progMonitor = pm;
	}
	
	
	public Graph annotateGraph(final Graph orgGraph, Graph targetGraph) throws InterruptedException {
		final int numProcessors = Math.max(Runtime.getRuntime().availableProcessors(),10);
		int start;
		int end;
		
		updateProgress(this,orgGraph.getNodeCount());
		
		Thread[] pool = new Thread[numProcessors];
		
		int nodesPerThread = orgGraph.getNodeCount()/numProcessors;
		int remainingNodes = orgGraph.getNodeCount() - (nodesPerThread*numProcessors);
		
		for(int i = 0; i < numProcessors; i++){
			start = (i*nodesPerThread);
			end = ((i+1)*nodesPerThread)-1;
			
			if(i== numProcessors-1){
				end += remainingNodes;
			}
			
			pool[i] = new NodeAnnotationThread(orgGraph,targetGraph,start,end,this);
			pool[i].start();
		}
		copyEdges(orgGraph,targetGraph);
			
		
		for(int i = 0; i < numProcessors; i++){
			pool[i].join();
		}
		
		return targetGraph;
	}
	
	public static void copyEdges(Graph source, Graph target){
		Table sourceEdgeTable = source.getEdgeTable();
		Table targetEdgeTable = target.getEdgeTable();
		
		for(int i = 0; i < sourceEdgeTable.getRowCount(); i++){
			for(int j = 0; j < sourceEdgeTable.getColumnCount(); j++){
				targetEdgeTable.set(i, j, sourceEdgeTable.get(i, j));
			}
		}
	}
	
	public static void updateProgress(NodeDegreeAnnotator na, int work){
		na.progress+=work;
		na.progMonitor.worked(na.progress);
	}

}
