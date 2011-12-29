package edu.iu.nwb.analysis.java.strongcomponentclustering.components;

import java.util.Iterator;
import java.util.Stack;

import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.CascadedTable;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;


public class AnnotateStrongComponents {
	ProgressMonitor progMonitor;
	int progress = 0;
	private int strongComponentClusters;
	private static final String preOrderColumn = "preOrder";
	private static final String strongComponentColumn = "strongComponentID";
	
	public AnnotateStrongComponents(ProgressMonitor pm){
		this.progMonitor = pm;
	}
	
	
	public Graph strongComponentCalculation(final Graph sourceGraph){
		int nodeCount = sourceGraph.getNodeTable().getRowCount();
		Stack firstStack = new Stack();
		firstStack.setSize(nodeCount);
		Stack secondStack = new Stack();
		secondStack.setSize(nodeCount);
		
		Graph targetGraph = createTargetGraph(sourceGraph);
		
		CascadedTable ct = new CascadedTable(sourceGraph.getNodeTable());
		ct.addColumn(strongComponentColumn, int.class, new Integer(-1));
		ct.addColumn(preOrderColumn, int.class, new Integer(-1));
		
		this.strongComponentClusters = 0;

		Integer testCount = new Integer(0);
		
		for (int v = nodeCount-1; v >= 0; v--){
			if(ct.getInt(v, preOrderColumn) == -1){
				Node n = sourceGraph.getNode(v);
				if(n.getString("label") != null){
					copyTuple(sourceGraph.getNodeTable(),targetGraph.getNodeTable(),v);
					recursiveStrongComponentCalculation(sourceGraph,targetGraph,n,ct,firstStack,secondStack,testCount);
				}
			}
		}
		
		return targetGraph;
	}
	
	private void recursiveStrongComponentCalculation(final Graph sourceGraph, Graph targetGraph, Node n, CascadedTable ct, Stack firstStack, Stack secondStack, Integer count){
		int v;
		count = new Integer(count.intValue()+1);
		ct.setInt(n.getRow(), preOrderColumn, count.intValue());
		//preOrder[vertex] = count++;
		firstStack.push(new Integer(n.getRow()));
		secondStack.push(new Integer(n.getRow()));

		for(Iterator it = n.outNeighbors(); it.hasNext();){
			int outNode = ((Node)it.next()).getRow();
			int edgeRow = sourceGraph.getEdge(n.getRow(), outNode);
			copyTuple(sourceGraph.getEdgeTable(),targetGraph.getEdgeTable(),edgeRow);
			if(ct.getInt(outNode, preOrderColumn) == -1){
				copyTuple(sourceGraph.getNodeTable(),targetGraph.getNodeTable(),outNode);
				recursiveStrongComponentCalculation(sourceGraph, targetGraph, sourceGraph.getNode(outNode),ct,firstStack,secondStack, count);
			}
			else if (ct.getInt(outNode, strongComponentColumn)  == -1){
				while (ct.getInt(((Integer)secondStack.peek()).intValue(),preOrderColumn) > ct.getInt(outNode, preOrderColumn)){ 
					secondStack.pop();	
				}
				
			}
		}
		
		if(((Integer)secondStack.peek()).intValue() == n.getRow()){ 
			secondStack.pop();
		}
		else{
			return;
		}
		int size = 0;
		do {
			size++;
			v = ((Integer)firstStack.pop()).intValue();
			ct.setInt(v, strongComponentColumn, this.strongComponentClusters);
			targetGraph.getNode(v).setInt(strongComponentColumn, this.strongComponentClusters);
		} while (n.getRow() != v);
		
		
		this.strongComponentClusters++;

	}
	
	private static Graph createTargetGraph(final Graph originalGraph){
		Schema originalNodeSchema = originalGraph.getNodeTable().getSchema();
		Schema originalEdgeSchema = originalGraph.getEdgeTable().getSchema();
		
		Schema targetNodeSchema = copySchema(originalNodeSchema);
		targetNodeSchema.addColumn(AnnotateStrongComponents.strongComponentColumn, int.class);
		Schema targetEdgeSchema = copySchema(originalEdgeSchema);
		
		return new Graph(targetNodeSchema.instantiate(originalGraph.getNodeCount()),targetEdgeSchema.instantiate(originalGraph.getEdgeCount()), originalGraph.isDirected());
	}
	
	private static Schema copySchema(final Schema sourceSchema){
		Schema copy = new Schema();
		
		for(int i = 0; i < sourceSchema.getColumnCount(); i++){
			copy.addColumn(sourceSchema.getColumnName(i), sourceSchema.getColumnType(i));
		}
		
		return copy;
	}

	private static void copyTuple(final Table sourceTable, Table targetTable, int rowNumber){
		for(int i = 0; i < sourceTable.getColumnCount(); i++){
			targetTable.set(rowNumber, i, sourceTable.get(rowNumber, i));
		}
	}
	
	public static void updateProgress(AnnotateStrongComponents asc, int progress){
		asc.progress+=progress;
		asc.progMonitor.worked(asc.progress);
	}

}
