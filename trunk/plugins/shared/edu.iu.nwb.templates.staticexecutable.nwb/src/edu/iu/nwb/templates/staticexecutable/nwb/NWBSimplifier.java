package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBSimplifier extends NWBFileParserAdapter {
	private int nodeCount = 0;
	private PrintWriter output;
	private Map nodeIds = new HashMap();
	private String weightAttribute;
	private boolean ignoreWeightAttribute;
	private String reason = "";
	private boolean haltParsing = false;
	
	public NWBSimplifier(OutputStream outputStream, int numberOfNodes, int numberOfEdges, String weightAttribute, boolean ignoreWeightAttribute) {
		this.output = new PrintWriter(outputStream, true);
		this.writeHeader(this.output, numberOfNodes, numberOfEdges);
		this.weightAttribute = weightAttribute;
		this.ignoreWeightAttribute = ignoreWeightAttribute;
	}
	
	public void addNode(int id, String label, Map attributes) {
		this.nodeIds.put(new Integer(id), new Integer(nodeCount));
		this.nodeCount++;
	}
	
	private void writeHeader(PrintWriter output, int numberOfNodes, int numberOfEdges) {
		this.output.println(numberOfNodes);
		this.output.println(numberOfEdges);
	}
	
	private void writeEdge(PrintWriter output, int source, int target, double value) {
		this.output.print(source);
		this.output.print(" ");
		this.output.print(target);
		this.output.print(" ");
		this.output.println(value);
	}
	
	private void addEdge(int source, int target, double value) {
		int fakeSource = ((Integer) this.nodeIds.get(new Integer(source))).intValue();
		int fakeTarget = ((Integer) this.nodeIds.get(new Integer(target))).intValue();
		this.writeEdge(output, fakeSource, fakeTarget, value);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		double weight;
		if(ignoreWeightAttribute) {
			weight = 1;
		} else {
			weight = ((Number) attributes.get(weightAttribute)).doubleValue();
		}
		
		if(weight == 0) { //TODO: fix the NWB parser so this crap isn't necessary, and I can throw a real exception.
			reason = "Zero weights are not allowed. To use this algorithm, preprocess your network further.";
			haltParsing = true;
		} else {
			addEdge(sourceNode, targetNode, weight);
		}
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		addDirectedEdge(node1, node2, attributes);
	}
	
	public boolean haltParsingNow() {
		return haltParsing;
	}
	
	public boolean hadIssue() {
		return !"".equals(reason);
	}
	
	public String getReason() {
		return reason;
	}
}
