package edu.iu.nwb.analysis.sampling.edge;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.iu.nwb.analysis.sampling.common.JungSampler;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.utils.GraphUtils;

public class EdgeSampler implements JungSampler {

	private static final String NUMBER_PARAMETER_KEY = "edges";
	
	
	public Graph sample(Graph graph, Dictionary parameters) {
		int totalNeeded = ((Integer) parameters.get(NUMBER_PARAMETER_KEY)).intValue();
		
		List edges = new ArrayList(graph.getEdges());
		
		Random randomizer = new Random();
		
		Set sampledEdges = new HashSet();
		
		while(edges.size() > 0 && sampledEdges.size() < totalNeeded) {
			sampledEdges.add(edges.remove(randomizer.nextInt(edges.size())));
		}
		
		return GraphUtils.edgeSetToGraph(sampledEdges, false);
	}

	public String getSampleName() {
		return "Edge Sample";
	}

}
