package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.analysis.pagerank.weighted.WeightAccessor.InvalidWeightException;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class InMemoryGraph extends Graph {

	private List<Edge> edges;

	public InMemoryGraph(File nwbFile, final WeightAccessor weightAccessor, int numberOfNodes,
			int numberOfDirectedEdges) throws AlgorithmExecutionException {
		this.numberOfNodes = numberOfNodes;
		this.edges = accumulateEdges(nwbFile, weightAccessor,
				numberOfDirectedEdges);
		
	}

	private static List<Edge> accumulateEdges(File nwbFile, final WeightAccessor weightAccessor,
			int numberOfDirectedEdges) throws AlgorithmExecutionException {
		final List<Edge> edges = new ArrayList<Edge>(numberOfDirectedEdges + 1);
		WeightedPagerank.parseNWBFile(nwbFile, new NWBFileParserAdapter() {
			@Override
			public void addDirectedEdge(int source, int target, Map<String, Object> attributes) {
				try {
					edges.add(new Edge(source, target, weightAccessor.getWeight(attributes)));
				} catch (InvalidWeightException e) {
					throw new RuntimeException(e);
				}
			}
		});
		return edges;
	}

	@Override
	public void performEdgePass(EdgeHandler handler) {
		for(Edge edge: this.edges) {
			handler.handleEdge(edge);
		}
	}

	

}
