/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu/).
 * 
 * Created on May 5, 2005 at Indiana University.
 */
package edu.iu.iv.clustering.betweennesscentrality;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.importance.EdgeRanking;
import edu.uci.ics.jung.graph.Graph;

public class BC {

	private static LogService logger;
    public static Graph clusterGraph(Graph graph, double threshold, boolean normalize) {
        double maxCentrality = Float.MAX_VALUE;
        
        
        BetweennessCentrality bc = new BetweennessCentrality(graph, false);
        int iter = 1;
        while (maxCentrality > threshold) {
            bc.evaluate();
            // rankings are given in descending order here
            // so get the edge corresponding to the highest
            // ranking and remove it
            EdgeRanking er = (EdgeRanking) (bc.getRankings().iterator().next());
            maxCentrality = er.rankScore;
            if (normalize) {
                int n = graph.getVertices().size();
                maxCentrality = maxCentrality / ((n - 1) * (n - 2) / 2);
            }

            iter++;
            // remove the edge with the highest betweenness
            graph.removeEdge(er.edge);
        }
        logger.log(LogService.LOG_INFO, "Removed " + iter + " edges.\n"
                + "Maximum Centrality of remnant graph:" + maxCentrality);
        return graph;
    }
    public static void setLogger(LogService logger){
		BC.logger = logger;
    		
	}
    
    
}


