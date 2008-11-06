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
	
	public NWBSimplifier(OutputStream outputStream, int numberOfNodes, int numberOfEdges) {
		this.output = new PrintWriter(outputStream);
		this.writeHeader(this.output, numberOfNodes, numberOfEdges);
	}
	
	public void addNode(int id, String label, Map attributes) {
		this.nodeIds.put(new Integer(id), new Integer(nodeCount));
		this.nodeCount++;
	}
	
	private void writeHeader(PrintWriter output, int numberOfNodes, int numberOfEdges) {
		output.println(numberOfNodes);
		output.println(numberOfEdges);
	}
	
	private void writeEdge(PrintWriter output, int source, int target) {
		output.print(source);
		output.print(" ");
		output.println(target);
	}
	
	private void addEdge(int source, int target) {
		int fakeSource = ((Integer) this.nodeIds.get(new Integer(source))).intValue();
		int fakeTarget = ((Integer) this.nodeIds.get(new Integer(target))).intValue();
		this.writeEdge(output, fakeSource, fakeTarget);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		addEdge(sourceNode, targetNode);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		addEdge(node1, node2);
	}
}
