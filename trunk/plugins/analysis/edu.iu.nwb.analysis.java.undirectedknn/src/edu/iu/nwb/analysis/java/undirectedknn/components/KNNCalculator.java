package edu.iu.nwb.analysis.java.undirectedknn.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;

public class KNNCalculator {
	private ProgressMonitor progMonitor;
	private int progress = 0;
	
	public KNNCalculator(ProgressMonitor pm){
		this.progMonitor = pm;
	}
	
	public Graph calculateKNN(final Graph originalGraph, Graph targetGraph) throws InterruptedException{
		final int numProcessors = Math.min(originalGraph.getNodeCount(),Math.max(Runtime.getRuntime().availableProcessors(),10));
		int start;
		int end;
		Map degreeToSumOfNeighborsDegree = Collections.synchronizedMap(new HashMap());
		Map degreeToTotalNumberOfDegree = Collections.synchronizedMap(new HashMap());
		
		updateProgress(this,originalGraph.getNodeCount());
		
		Thread[] pool = new Thread[numProcessors];
		
		int nodesPerThread = originalGraph.getNodeCount()/numProcessors;
		int remainingNodes = originalGraph.getNodeCount() - (nodesPerThread*numProcessors);
		
		for(int i = 0; i < numProcessors; i++){
			start = (i*nodesPerThread);
			end = ((i+1)*nodesPerThread);
			
			if(i== numProcessors-1){
				end += remainingNodes;
			}
			
			pool[i] = new KNNCalculatorThread(originalGraph,targetGraph,start,end,this,degreeToSumOfNeighborsDegree,degreeToTotalNumberOfDegree);
			pool[i].start();
		}
		copyEdges(originalGraph,targetGraph);
			
		
		for(int i = 0; i < numProcessors; i++){
			pool[i].join();
		}
		
		return targetGraph;
		
		
	}
	
	public Graph createAnnotatedGraph(final Graph originalGraph) throws AlgorithmExecutionException{
		final Schema originalGraphNodeSchema = originalGraph.getNodeTable().getSchema();
		final Schema originalGraphEdgeSchema = originalGraph.getEdgeTable().getSchema();

		Schema annotatedGraphNodeSchema = copySchema(originalGraphNodeSchema);
	
		annotatedGraphNodeSchema = appendKNNColumn(annotatedGraphNodeSchema);
		Schema annotatedGraphEdgeSchema = copySchema(originalGraphEdgeSchema);

		Table newNodeTable = annotatedGraphNodeSchema.instantiate(originalGraph.getNodeTable().getRowCount());
		Table newEdgeTable = annotatedGraphEdgeSchema.instantiate(originalGraph.getEdgeTable().getRowCount());

		Graph annotatedGraph = new Graph(newNodeTable,newEdgeTable,originalGraph.isDirected());

		return annotatedGraph;

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
	
	public static void updateProgress(KNNCalculator kc, int work){
		kc.progress+=work;
		kc.progMonitor.worked(kc.progress);
	}
	
	private static Schema copySchema(final Schema original){
		Schema copy = new Schema();

		for(int i = 0; i < original.getColumnCount(); i++){
			copy.addColumn(original.getColumnName(i), original.getColumnType(i));
		}

		return copy;
	}
	
	private static Schema appendKNNColumn(Schema targetSchema) throws AlgorithmExecutionException{
		if(targetSchema.getColumnIndex("knn") < 0){
			targetSchema.addColumn("knn", float.class);
		}else{
			throwException("knn");
		}
		return targetSchema;
	}
	
	private static void throwException(String type) throws AlgorithmExecutionException{
		throw new AlgorithmExecutionException("Attribute: " + type + " already exists. Please rename the attribute before rerunning this algorithm.");
	}
	
}
