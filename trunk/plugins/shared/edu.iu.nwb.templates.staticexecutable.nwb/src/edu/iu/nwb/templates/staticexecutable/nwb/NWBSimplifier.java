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
	
	public NWBSimplifier(OutputStream outputStream, int numberOfNodes, int numberOfEdges, String weightAttribute) {
		this.output = new PrintWriter(outputStream, true);
		this.writeHeader(this.output, numberOfNodes, numberOfEdges);
		this.weightAttribute = weightAttribute;
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
		double weight = ((Number) attributes.get(weightAttribute)).doubleValue();
		addEdge(sourceNode, targetNode, weight);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		addDirectedEdge(node1, node2, attributes);
	}
}
