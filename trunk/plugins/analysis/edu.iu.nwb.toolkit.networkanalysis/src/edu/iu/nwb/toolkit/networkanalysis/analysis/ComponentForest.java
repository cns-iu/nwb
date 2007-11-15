package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import prefuse.data.Graph;
import prefuse.data.Node;

public class ComponentForest{
	//Map forest;
	int weakComponentClusters = 0;
	int maxWeakConnectedNodes = 0;

	//int strongComponentClusters = 0;
	//int maxStrongConnectedNodes = 0;

	int weakNodes;
	//int strongNodes;

	BigDecimal averageWeakConnectedness = new BigDecimal(0.0);
	//BigDecimal averageStrongConnectedness = new BigDecimal(0.0);

	public ComponentForest(){

	}


	public int getMaximumConnectedNodes(){
		return this.maxWeakConnectedNodes;
	}

	public double getAverageConnectedness(){
		return averageWeakConnectedness.doubleValue();
	}

	public int getComponentClusters(){
		return this.weakComponentClusters;
	}
	
	public boolean isWeaklyConnected(){
		if(this.weakComponentClusters == 1){
			return true;
		}
		else
			return false;
	}

	public HashMap weakComponentCalculation(final Graph grph, NetworkProperties np){
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
		
		this.weakComponentClusters = clusters.keySet().size();
		this.maxWeakConnectedNodes = maxNodes;
		return clusters;
	}


	
}
