package edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin;

import java.util.Map;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.Node;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes.NWBToBINConversionException;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class Preprocessor extends NWBFileParserAdapter {
	private NWBToBINConversionException exceptionThrown;
	private boolean shouldHaltParsing = false;
	private double maximumWeightFound = -1.0; 

	private NetworkInfo networkInfo;
	private String weightAttribute;
	private boolean isWeighted;
	
	public Preprocessor(NetworkInfo networkInfo, String weightAttribute, boolean isWeighted) {
		this.networkInfo = networkInfo;
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;
	}

	public NWBToBINConversionException getExceptionThrown() {
		return this.exceptionThrown;
	}

	public double getMaximumWeightFound() {
		return this.maximumWeightFound;
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	@Override
	public void addUndirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
		this.networkInfo.accumulateEdgeCountsForOutput();
		this.networkInfo.setMaximumWeight(this.maximumWeightFound);
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		Node sourceNode = Node.getOrCreateNode(sourceNodeID, this.networkInfo);
		sourceNode.incrementEdgeCount(this.networkInfo);
		
		Node targetNode = Node.getOrCreateNode(targetNodeID, this.networkInfo);
		targetNode.incrementEdgeCount(this.networkInfo);

		processWeight(attributes);
	}

	@SuppressWarnings("unchecked")
	private void processWeight(Map attributes) {
		if (this.isWeighted) {
			Object weightAttribute = attributes.get(this.weightAttribute);
			if (weightAttribute == null) {
				String exceptionMessage =
					"An edge with no weight specified was found.  " +
					"All edges must have a weight if you specify a weight column.";
				this.exceptionThrown = new NWBToBINConversionException(exceptionMessage);
				this.shouldHaltParsing = true;
			} else {
				Number weightNumber = (Number)weightAttribute;
				double weight = weightNumber.doubleValue();

				if (weight < 0.0) {
					String exceptionMessage =
						"An edge with a negative weight was found.  " +
						"All edges must have a positive weight if you specify a weight column.";
					this.exceptionThrown = new NWBToBINConversionException(exceptionMessage);
					this.shouldHaltParsing = true;
				} else {
					this.maximumWeightFound = Math.max(weight, this.maximumWeightFound);
				}
			}
		}
	}
}