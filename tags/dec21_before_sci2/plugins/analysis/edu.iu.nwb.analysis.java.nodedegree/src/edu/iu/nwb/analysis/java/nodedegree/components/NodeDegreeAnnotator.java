package edu.iu.nwb.analysis.java.nodedegree.components;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;

public class NodeDegreeAnnotator {
	private ProgressMonitor progMonitor;
	private int progress = 0;
	private int degreeType;
	
	public NodeDegreeAnnotator(ProgressMonitor pm, int degreeType){
		this.progMonitor = pm;
		this.degreeType = degreeType;	
	}
	
	
	public Graph annotateGraph(final Graph orgGraph, Graph targetGraph) throws InterruptedException {
		final int numProcessors = Runtime.getRuntime().availableProcessors();
		int start;
		int end;
		
		updateProgress(this,orgGraph.getNodeCount());
		
		Thread[] pool = new Thread[numProcessors];
		
		int nodesPerThread = orgGraph.getNodeCount()/numProcessors;
		int remainingNodes = orgGraph.getNodeCount() - (nodesPerThread*numProcessors);
		
		for(int i = 0; i < numProcessors; i++){
			start = (i*nodesPerThread);
			end = ((i+1)*nodesPerThread);
			
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
	
	public Graph createAnnotatedGraph(final Graph originalGraph) throws AlgorithmExecutionException{
		final Schema originalGraphNodeSchema = originalGraph.getNodeTable().getSchema();
		final Schema originalGraphEdgeSchema = originalGraph.getEdgeTable().getSchema();

		Schema annotatedGraphNodeSchema = copySchema(originalGraphNodeSchema);
		if(annotatedGraphNodeSchema == null){
			if(this.degreeType == DegreeType.totalDegree)
				throwException("totalDegree");
			if(this.degreeType == DegreeType.inDegree)
				throwException("inDegree");
			if(this.degreeType == DegreeType.outDegree)
				throwException("outDegree");
		}
			annotatedGraphNodeSchema = appendDegreeAnnotation(annotatedGraphNodeSchema, this.degreeType);
		Schema annotatedGraphEdgeSchema = copySchema(originalGraphEdgeSchema);

		Table newNodeTable = annotatedGraphNodeSchema.instantiate(originalGraph.getNodeTable().getRowCount());
		Table newEdgeTable = annotatedGraphEdgeSchema.instantiate(originalGraph.getEdgeTable().getRowCount());

		Graph annotatedGraph = new Graph(newNodeTable,newEdgeTable,originalGraph.isDirected());

		return annotatedGraph;

	}

	private static Schema copySchema(final Schema original){
		Schema copy = new Schema();

		for(int i = 0; i < original.getColumnCount(); i++){
			copy.addColumn(original.getColumnName(i), original.getColumnType(i));
		}

		return copy;
	}

	private Schema appendDegreeAnnotation(Schema targetSchema, int degreeType) throws AlgorithmExecutionException{
		int index = -1;
		String label = null;
		
		
		if(degreeType == DegreeType.totalDegree){
			index = targetSchema.getColumnIndex("totalDegree");
			label = "totalDegree";
		}
		if(degreeType == DegreeType.inDegree){
			index = targetSchema.getColumnIndex("inDegree");
			label = "inDegree";
		}
		if(degreeType == DegreeType.outDegree){
			index = targetSchema.getColumnIndex("outDegree");
			label = "outDegree";	
		}

		if(index < 0){
			if(degreeType == DegreeType.totalDegree)
				targetSchema.addColumn("totalDegree", int.class);
			if(degreeType == DegreeType.inDegree)
				targetSchema.addColumn("inDegree", int.class);
			if(degreeType == DegreeType.outDegree)
				targetSchema.addColumn("outDegree",int.class);
			
		}
		else{
			
			throw new AlgorithmExecutionException("This graph already has an attribute named " + label +". Rename this attribute if you wish to continue.");
		}

		return targetSchema;
	}
	
	private static void throwException(String type) throws AlgorithmExecutionException{
		throw new AlgorithmExecutionException("Attribute: " + type + " already exists. Please rename the attribute before rerunning this algorithm.");
	}
	
	public int getDegreeType(){
		return this.degreeType;
	}

}
