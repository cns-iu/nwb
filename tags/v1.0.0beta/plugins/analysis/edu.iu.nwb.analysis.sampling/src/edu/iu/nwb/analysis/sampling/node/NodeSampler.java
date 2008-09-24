package edu.iu.nwb.analysis.sampling.node;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.iu.nwb.analysis.sampling.common.JungSampler;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.filters.impl.DropSoloNodesFilter;
import edu.uci.ics.jung.utils.GraphUtils;

public class NodeSampler implements JungSampler {

	private static final String NUMBER_PARAMETER_KEY = "nodes";
	
	
	public Graph sample(Graph graph, Dictionary parameters) {
		int totalNeeded = ((Integer) parameters.get(NUMBER_PARAMETER_KEY)).intValue();
		
		List vertices = new ArrayList(graph.getVertices());
		
		Random randomizer = new Random();
		
		Set sampledVertices = new HashSet();
		
		while(vertices.size() > 0 && sampledVertices.size() < totalNeeded) {
			sampledVertices.add(vertices.remove(randomizer.nextInt(vertices.size())));
		}
		
		Graph sampledGraph = GraphUtils.vertexSetToGraph(sampledVertices);
		//according to the algorithm, drop unattached nodes, even if that gets rid of all of them
		return DropSoloNodesFilter.getInstance().filter(sampledGraph).assemble();
	}

	public String getSampleName() {
		return "Node Sample";
	}

}
