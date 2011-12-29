package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Iterator;
import java.util.Stack;

import prefuse.data.CascadedTable;
import prefuse.data.Graph;
import prefuse.data.Node;

public class StrongComponentClusteringThread extends Thread{
	private static String preOrderColumn = "preOrder";
	private static String strongComponentColumn = "strongComponentID";
	
	private Graph clusterGraph;
	
	private int clusters = 0;
	private int maxSize = 0;
	

	
	public StrongComponentClusteringThread(Graph g){
		this.clusterGraph = g;		
	}

	public void run() {
		this.strongComponentCalculation();
		
	}
	
	private void strongComponentCalculation(){
		int nodeCount = this.clusterGraph.getNodeCount();
		Stack firstStack = new Stack();
		firstStack.setSize(nodeCount);
		Stack secondStack = new Stack();
		secondStack.setSize(nodeCount);
		

		this.clusters = 0;

		Integer testCount = new Integer(0);
		
		int[] id = new int[nodeCount];
		int[] pre = new int[nodeCount];

		java.util.Arrays.fill(id, -1);
		java.util.Arrays.fill(pre, -1);

		
		CascadedTable statTable = new CascadedTable(this.clusterGraph.getNodeTable());
		statTable.addColumn(preOrderColumn, int.class, new Integer(-1));
		statTable.addColumn(strongComponentColumn, int.class, new Integer(-1));
		
		
		for (Iterator it = this.clusterGraph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			if(statTable.getInt(n.getRow(), preOrderColumn) == -1){
				recursiveStrongComponentCalculation(this.clusterGraph,n,statTable,firstStack,secondStack,testCount);
			}
		}
	}
	
	private void recursiveStrongComponentCalculation(final Graph g, Node n, CascadedTable ct, Stack firstStack, Stack secondStack, Integer count){
		int v;
		count = new Integer(count.intValue()+1);
		ct.setInt(n.getRow(), preOrderColumn, count.intValue());
		//preOrder[vertex] = count++;
		firstStack.push(new Integer(n.getRow()));
		secondStack.push(new Integer(n.getRow()));

		for(Iterator it = n.outNeighbors(); it.hasNext();){
			int outNode = ((Node)it.next()).getRow();
			if(ct.getInt(outNode, preOrderColumn) == -1) 
				recursiveStrongComponentCalculation(g, g.getNode(outNode),ct,firstStack,secondStack, count);
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
			ct.setInt(v, strongComponentColumn, this.clusters);
		} while (n.getRow() != v);
		
		if(size > this.maxSize){
			this.maxSize = size;
		}
		

		
		this.clusters++;

	}

	public int getClusters(){
		return this.clusters;
	}
	
	public int getMaxSize(){
		return this.maxSize;
	}
	
}
