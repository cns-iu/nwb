package edu.iu.nwb.preprocessing.bibcouplingsimilarity;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToEdgeFileHandler extends NWBFileParserAdapter {
	private PrintWriter out;
	
	public NWBToEdgeFileHandler(OutputStream output) {
		out = new PrintWriter(output);
	}
		
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		if (node1 != node2) {
			out.println(node1+"\t"+node2+"\n");
			out.println(node2+"\t"+node1+"\n");
		}
	}
	
	public void addDirectedEdge(int node1, int node2, Map attributes) {
		if (node1 != node2) {
			out.println(node1+"\t"+node2+"\n");
		}
	}
	
	public void finishedParsing() {
		out.close();
	}
}
