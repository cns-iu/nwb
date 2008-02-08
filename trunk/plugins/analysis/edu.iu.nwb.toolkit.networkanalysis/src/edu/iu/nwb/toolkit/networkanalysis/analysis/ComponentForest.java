package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Stack;

import prefuse.data.Graph;
import prefuse.data.Node;

public class ComponentForest{

	int weakComponentClusters = 0;
	int maxWeakConnectedNodes = 0;

	int strongComponentClusters = 0;
	int maxStrongConnectedNodes = 0;
	
	int count = 0;
	


	public ComponentForest(final Graph graph){
		this.calculateConnectedness(graph);
	}

	public int getMaximumStrongConnectedNodes(){
		return this.maxStrongConnectedNodes;
	}
	
	public int getStrongComponentClusters(){
		return this.strongComponentClusters;
	}
	
	public boolean isStronglyConnected(){
		if(this.strongComponentClusters == 1){
			return true;
		}
		else
			return false;
	}


	public int getMaximumWeakConnectedNodes(){
		return this.maxWeakConnectedNodes;
	}

	public int getWeakComponentClusters(){
		return this.weakComponentClusters;
	}
	
	public boolean isWeaklyConnected(){
		if(this.weakComponentClusters == 1){
			return true;
		}
		else
			return false;
	}

	public void weakComponentCalculation(final Graph grph){
		
		HashSet seenNodes = new HashSet();
		int maxNodes = 0;
		int cluster = 0;
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if(!seenNodes.contains(i)){

				LinkedHashSet tree = GraphSearchAlgorithms.undirectedDepthFirstSearch(grph, i);
		
					seenNodes.addAll(tree);
					if(tree.size() > maxNodes)
						maxNodes = tree.size();
					
					
					cluster++;
					tree = null;
				}
			
				
			}
		
		this.weakComponentClusters = cluster;
		this.maxWeakConnectedNodes = maxNodes;
		
		seenNodes = null;

		
		
	}
	


	public void strongComponentCalculation(final Graph grph){
		int nodeCount = grph.getNodeCount();
		Stack firstStack = new Stack();
		firstStack.setSize(nodeCount);
		Stack secondStack = new Stack();
		secondStack = new Stack();
		secondStack.setSize(nodeCount);

		count = 0;
		this.strongComponentClusters = 0;

		int[] id = new int[nodeCount];
		int[] pre = new int[nodeCount];

		java.util.Arrays.fill(id, -1);
		java.util.Arrays.fill(pre, -1);

		for (int v = nodeCount-1; v >= 0; v--){
			if (pre[v] == -1){ 
				scR(grph,v,pre,id,firstStack,secondStack);
			}
		}
	}
	

	private void calculateConnectedness(final Graph graph){

		this.weakComponentCalculation(graph);		
		
		if(graph.isDirected()){
			this.strongComponentCalculation(graph);
		}
	}
	

	private void scR(final Graph g, int vertex,int[] preOrder, int[] scID, Stack fs, Stack ss){
		int v;
		
		preOrder[vertex] = count++;
		fs.push(new Integer(vertex));
		ss.push(new Integer(vertex));

		for(Iterator it = g.getNode(vertex).outNeighbors(); it.hasNext();){
			int outNode = ((Node)it.next()).getRow();
			if(preOrder[outNode] == -1) 
				scR(g, outNode,preOrder, scID,fs,ss);
			else if (scID[outNode]  == -1){
				
				while (preOrder[((Integer)ss.peek()).intValue()] > preOrder[outNode]){ 
					ss.pop();
					
				}
				
			}
		}
		if(((Integer)ss.peek()).intValue() == vertex){ 
			ss.pop();
		}
		else{
			return;
		}
		int size = 0;
		do {
			size++;
			scID[v = ((Integer)fs.pop()).intValue()] = this.strongComponentClusters; 	
		} while (vertex != v);
		
		if(size > this.maxStrongConnectedNodes){
			this.maxStrongConnectedNodes = size;
		}
		
		this.strongComponentClusters++;

	}
	
	
}
