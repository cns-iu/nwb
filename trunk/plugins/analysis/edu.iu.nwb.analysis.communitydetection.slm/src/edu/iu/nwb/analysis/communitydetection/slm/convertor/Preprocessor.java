package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class Preprocessor extends NWBFileParserAdapter {
	private double maximumWeightFound = -1.0; 

	private NetworkInfo networkInfo;
	private String weightAttribute;
	private boolean isWeighted;
	
	public Preprocessor(NetworkInfo networkInfo, String weightAttribute, boolean isWeighted) {
		this.networkInfo = networkInfo;
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;
	}

	public double getMaximumWeightFound() {
		return this.maximumWeightFound;
	}
	
	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	@Override
	public void addUndirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
		this.networkInfo.accumulateEdgeCountsForOutput();
		this.networkInfo.setMaximumWeight(this.maximumWeightFound);
	}
	
	private void addEdge(int sourceNodeID, int targetNodeID, Map<String, Object> attributes) {
		Node sourceNode = Node.getOrCreateNode(sourceNodeID, this.networkInfo);
		sourceNode.incrementEdgeCount(this.networkInfo);
		
		Node targetNode = Node.getOrCreateNode(targetNodeID, this.networkInfo);
		targetNode.incrementEdgeCount(this.networkInfo);
		
		if (this.isWeighted) {
			double weight = processWeight(attributes);
			this.networkInfo.addEdge(new Edge(sourceNode, targetNode, attributes, weight));
		} else {
			this.networkInfo.addEdge(new Edge(sourceNode, targetNode, attributes));
		}
	}

	private double processWeight(Map<String, Object> attributes) {
		double weight = 0;
		
		try {
			Number weightNumber = (Number) attributes.get(this.weightAttribute);
			weight = weightNumber.doubleValue();
		} catch (Exception e) {
			String exceptionMessage =
					"An edge with invalid weight specified was found.  " +
					"All edges must have a weight if you specify a weight column.";
			throw new PreprocessorException(exceptionMessage);
		}
		
		if (weight < 0) {
			String exceptionMessage =
				"An edge with a negative weight was found.  " +
				"All edges must have a positive weight if you specify a weight column.";
			throw new PreprocessorException(exceptionMessage);
		}
		this.maximumWeightFound = Math.max(weight, this.maximumWeightFound);
		
		return weight;
	}
}