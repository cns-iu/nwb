package edu.iu.nwb.preprocessing.nwbfile_cerncoltmatrix;

import java.util.Map;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBFileToCernColtMatrixHandler extends NWBFileParserAdapter {
	private DoubleMatrix2D matrix;
	private String issue = "";
	
	boolean isWeighted;
	String weightAttribute;
	
	public NWBFileToCernColtMatrixHandler(
			int nodeCount, boolean isWeighted, String weightAttribute) {
		this.matrix = DoubleFactory2D.sparse.make(nodeCount, nodeCount);
		this.isWeighted = isWeighted;
		this.weightAttribute = weightAttribute;
	}
	
	public DoubleMatrix2D getMatrix() {
		return this.matrix;
	}
	
	public boolean hadIssue() {
		return !this.issue.equals("");
	}
	
	public String getIssue() {
		return this.issue;
	}
	
	public void addDirectedEdge(
			int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		this.addEdge(node1, node2, attributes);
		this.addEdge(node2, node1, attributes);
	}
	
	public boolean haltParsingNow() {
		return this.hadIssue();
	}
	
	private void addEdge(int sourceNode, int targetNode, Map attributes) {
		double weight;
		
		if (this.isWeighted) {
			weight =
				((Number)attributes.get(this.weightAttribute)).doubleValue();
		} else {
			weight = 1.0;
		}
		
		this.matrix.setQuick(sourceNode - 1, targetNode - 1, weight);
	}
}