package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import prefuse.data.Graph;
import prefuse.data.Node;

public class WeakComponentClusteringThread extends Thread{
	private Graph clusterGraph;

	private int clusters;
	private int maxSize;

	private HashMap nodeDistribution = new HashMap();
	private HashMap sizeDistribution = new HashMap();

	public WeakComponentClusteringThread(Graph g){
		this.clusterGraph = g;
	}

	public void run() {
		this.weakComponentCalculation(this.clusterGraph);
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

				if(this.sizeDistribution.get(new Integer(tree.size())) == null){
					this.sizeDistribution.put(new Integer(tree.size()), new Integer(1));
				}else{
					int count = ((Integer)this.sizeDistribution.get(new Integer(tree.size()))).intValue();
					count++;
					this.sizeDistribution.put(new Integer(tree.size()), new Integer(count));
				}

				cluster++;
				this.nodeDistribution.put(new Integer(cluster), new Float(tree.size()/grph.getNodeCount()));
				tree = null;
			}			
		}

		this.clusters = cluster;
		this.maxSize = maxNodes;

		for(Iterator it = this.sizeDistribution.keySet().iterator(); it.hasNext();){
			Integer key = ((Integer)it.next());
			Integer value = (Integer)this.sizeDistribution.get(key);

			float newValue = value.floatValue()/cluster;

			this.sizeDistribution.put(key, new Float(newValue));

		}

	}

	public int getClusters(){
		return this.clusters;
	}

	public int getMaxSize(){
		return this.maxSize;
	}

}
