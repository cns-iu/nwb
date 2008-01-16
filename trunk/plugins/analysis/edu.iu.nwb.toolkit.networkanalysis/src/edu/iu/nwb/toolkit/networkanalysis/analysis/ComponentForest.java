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
				}
			
			
			}
		
		this.weakComponentClusters = cluster;
		this.maxWeakConnectedNodes = maxNodes;
		
	}
	
	public void strongComponentCalculation(final Graph grph){
		
		
		Stack finishedNodes = new Stack();
		finishedNodes.addAll(GraphSearchAlgorithms.directedDepthFirstSearch(grph, null, false, false));
		
		Graph g2 = GraphSearchAlgorithms.reverseGraph(grph);
		
		while(!finishedNodes.isEmpty()){
			int nodeNumber = ((Integer)finishedNodes.pop()).intValue();
		
			LinkedHashSet component = GraphSearchAlgorithms.directedDepthFirstSearch(g2, new Integer(nodeNumber), false, false);
			for(Iterator it = component.iterator(); it.hasNext();){
				Integer discoveredNode = (Integer)it.next();
				g2.removeNode(discoveredNode.intValue());
			}
			
			finishedNodes.removeAll(component);
		}
		
		
		int maxNodes = 0;
		
		int numberOfClusters = 0;
		
		
				
		
		
		
		this.strongComponentClusters = numberOfClusters;
		this.maxStrongConnectedNodes = maxNodes;
		
		
		
	}

	private void calculateConnectedness(final Graph graph){

		this.weakComponentCalculation(graph);		
		
		if(graph.isDirected()){
			this.strongComponentCalculation(graph);
		}
	}
	
	
}
