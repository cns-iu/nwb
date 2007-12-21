package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import prefuse.data.Graph;
import prefuse.data.Node;

public class ComponentForest{

	int weakComponentClusters = 0;
	int maxWeakConnectedNodes = 0;

	int strongComponentClusters = 0;
	int maxStrongConnectedNodes = 0;


	public ComponentForest(){

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

	/*public double getAverageConnectedness(){
		return averageWeakConnectedness.doubleValue();
	}*/

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

	public void weakComponentCalculation(final Graph grph, NetworkProperties np){
		HashMap clusters = new HashMap();
		HashSet seenNodes = new HashSet();
		int maxNodes = 0;
		int cluster = 0;
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if(!seenNodes.contains(i)){

				LinkedHashSet tree = np.uDFS(grph, i);
		
					seenNodes.addAll(tree);
					if(tree.size() > maxNodes)
						maxNodes = tree.size();
					
					clusters.put(new Integer(cluster), new HashSet(tree)); 
					cluster++;
				}
			
			
			}
		
		this.weakComponentClusters = cluster;
		this.maxWeakConnectedNodes = maxNodes;
		
	}
	
	public void strongComponentCalculation(final Graph grph, NetworkProperties np){
		//HashMap clusters = new HashMap();
		boolean[] seenNodes = new boolean[grph.getNodeCount()];
		java.util.Arrays.fill(seenNodes, false);
		int maxNodes = 0;
		
		int numberOfClusters = 0;
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer nodeRow = new Integer(n.getRow());
			if(!seenNodes[nodeRow.intValue()]){
				seenNodes[nodeRow.intValue()] = true;
				LinkedList preOrderSearch = new LinkedList(np.dDFS(grph, nodeRow, true, false));
				
				while(!preOrderSearch.isEmpty()){
				LinkedHashSet postOrderSearch = np.dDFS(grph, (Integer)preOrderSearch.get(preOrderSearch.size()-1), false, false);
				LinkedHashSet component = new LinkedHashSet(postOrderSearch);
				component.retainAll(preOrderSearch);
				
				
			
				
				preOrderSearch.removeAll(component);
				for(Iterator componentIT = component.iterator(); componentIT.hasNext();){
					seenNodes[((Integer)componentIT.next()).intValue()] = true;
				}
				
				if(component.size() > maxNodes)
					maxNodes = component.size();
				
		
				numberOfClusters++;
		
			}
			
		
		}
		}
		
		this.strongComponentClusters = numberOfClusters;
		this.maxStrongConnectedNodes = maxNodes;
		
		
		
	}


	
}
