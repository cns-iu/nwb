package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class InMemoryGraph extends Graph {

	private List<Edge> edges;

	public InMemoryGraph(File nwbFile, final WeightAccessor weightAccessor, int numberOfNodes,
			int numberOfDirectedEdges) throws AlgorithmExecutionException {
		this.numberOfNodes = numberOfNodes;
		this.edges = accumulateEdges(nwbFile, weightAccessor,
				numberOfDirectedEdges);
		
	}

	private List<Edge> accumulateEdges(File nwbFile, final WeightAccessor weightAccessor,
			int numberOfDirectedEdges) throws AlgorithmExecutionException {
		final List<Edge> edges = new ArrayList<Edge>(numberOfDirectedEdges + 1);
		WeightedPagerank.parseNWBFile(nwbFile, new NWBFileParserAdapter() {
			public void addDirectedEdge(int source, int target, Map attributes) {
				edges.add(new Edge(source, target, weightAccessor.getWeight(attributes)));
			}
		});
		return edges;
	}

	@Override
	public void performEdgePass(EdgeHandler handler) {
		for(Edge edge: edges) {
			handler.handleEdge(edge);
		}
	}

	

}
