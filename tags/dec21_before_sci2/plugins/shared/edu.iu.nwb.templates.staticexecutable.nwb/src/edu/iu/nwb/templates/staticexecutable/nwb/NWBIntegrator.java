package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class NWBIntegrator implements NWBFileParserHandler {
	
	private NWBFileWriter output;
	private AttributeAdder nodeAttributeAdder;
	private AttributeAdder edgeAttributeAdder;
	
	public NWBIntegrator(FileOutputStream outputStream, List forNodes,
			List forEdges) throws IOException {
		this.output = new NWBFileWriter(outputStream);
		
		this.nodeAttributeAdder = new AttributeAdder(forNodes, ".nodes");
		System.err.println("New attributes for nodes: " + forNodes.size());
		this.edgeAttributeAdder = new AttributeAdder(forEdges, ".edges");
		
		
	}

	public void addComment(String comment) {
		output.addComment(comment);
	}

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.output.addDirectedEdge(sourceNode, targetNode, this.edgeAttributeAdder.addNext(attributes));
	}

	public void addNode(int id, String label, Map attributes) {
		this.output.addNode(id, label, this.nodeAttributeAdder.addNext(attributes));
		
	}

	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		this.output.addUndirectedEdge(node1, node2, this.edgeAttributeAdder.addNext(attributes));
		
	}

	public void finishedParsing() {
		this.output.finishedParsing();
		
	}

	public boolean haltParsingNow() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDirectedEdgeCount(int numberOfEdges) {
		this.output.setDirectedEdgeCount(numberOfEdges);
		
	}

	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		this.output.setDirectedEdgeSchema(this.edgeAttributeAdder.updateSchema(schema));
	}

	public void setNodeCount(int numberOfNodes) {
		this.output.setNodeCount(numberOfNodes);
	}

	public void setNodeSchema(LinkedHashMap schema) {
		this.output.setNodeSchema(this.nodeAttributeAdder.updateSchema(schema));
	}

	public void setUndirectedEdgeCount(int numberOfEdges) {
		this.output.setUndirectedEdgeCount(numberOfEdges);
	}

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		this.output.setUndirectedEdgeSchema(this.edgeAttributeAdder.updateSchema(schema));
	}

}
