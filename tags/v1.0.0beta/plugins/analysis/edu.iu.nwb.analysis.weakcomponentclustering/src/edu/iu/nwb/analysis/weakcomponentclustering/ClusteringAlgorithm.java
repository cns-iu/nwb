package edu.iu.nwb.analysis.weakcomponentclustering;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.algorithms.cluster.ClusterSet;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author Russell Duhon
 *
 */
public class ClusteringAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    

    /**
     * Construct with the appropriate parameters
     * @param data This contains the input graph
     * @param parameters This contains r and q
     * @param context And this allows access to some additional capabilities
     */
    public ClusteringAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	
    	LogService log = (LogService) context.getService(LogService.class.getName());
    	
    	Graph inputGraph = (Graph) data[0].getData();
    	
    	ClusterSet clusters = new WeakComponentClusterer().extract(inputGraph);
    	
    	clusters.sort();
    	
    	int desiredClusters = ((Integer) this.parameters.get("n")).intValue();
    	
    	int totalClusters;
    	
    	if(desiredClusters == 0 || desiredClusters > clusters.size()) {
    		totalClusters = clusters.size();
    	} else {
    		totalClusters = desiredClusters;
    	}
    	
    	log.log(LogService.LOG_INFO, "" + clusters.size() + " clusters found, generating graphs for the top " + totalClusters + " clusters.");
    	
    	Data[] output = new Data[totalClusters];
    	
    	for(int clusterIndex = 0; clusterIndex < totalClusters; clusterIndex++) {
    		Graph cluster = clusters.getClusterAsNewSubGraph(clusterIndex);
			output[clusterIndex] = new BasicData(cluster, Graph.class.getName());
    		Dictionary attributes = output[clusterIndex].getMetadata();
    		
    		attributes.put(DataProperty.MODIFIED, new Boolean(true));
    		attributes.put(DataProperty.PARENT, data[0]);
    		attributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    		attributes.put(DataProperty.LABEL, "Weak Component Cluster of " + cluster.numVertices() + " nodes");
    	}
    	
        return output;
    }
}