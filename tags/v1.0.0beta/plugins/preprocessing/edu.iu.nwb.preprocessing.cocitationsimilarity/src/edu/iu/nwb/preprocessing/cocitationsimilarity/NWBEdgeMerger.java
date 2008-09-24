package edu.iu.nwb.preprocessing.cocitationsimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Handler class that merges edges from a weighted edge file and
 * an associated NWB File, outputting a merged NWB file.
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class NWBEdgeMerger implements NWBFileParserHandler {
	private NWBFileWriter out;
	private NWBFileParser parser;
	private File edgeFile;
	private boolean startedEdges;
	
	public NWBEdgeMerger(File edgeFile,File srcNWBFile,File outputNWBFile) throws IOException {
		this.edgeFile = edgeFile;
		
		startedEdges = false;
		out = new NWBFileWriter(outputNWBFile);
		parser = new NWBFileParser(srcNWBFile);
	}
	
	public void merge() throws IOException, ParsingException {
		parser.parse(this);
	}

	
	public void setNodeCount(int numberOfNodes) {		
		out.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		out.setNodeSchema(schema);
	}
	
	public void addNode(int id, String label, Map attributes) {
		out.addNode(id, label, attributes);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {}
	public void setDirectedEdgeCount(int numberOfEdges) {}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		startedEdges = true;
	}
	public void setUndirectedEdgeCount(int numberOfEdges) {}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		startedEdges = true;
	}

	public void addComment(String comment) {
		if (!startedEdges) {
			out.addComment(comment);
		}
	}
	
	public void finishedParsing() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(edgeFile),"UTF-8"));
			
			LinkedHashMap schema = new LinkedHashMap();
			schema.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
			schema.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
			schema.put("weight",NWBFileProperty.TYPE_REAL);
			out.setUndirectedEdgeSchema(schema);
			
			String line = in.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line.trim());
				
				if (st.countTokens() > 1) {
					int sourceNode = Integer.parseInt(st.nextToken());
					int targetNode = Integer.parseInt(st.nextToken());
					Double similarity = new Double(st.nextToken());
					
					Map attributes = new HashMap();
					attributes.put("weight", similarity);
					
					out.addUndirectedEdge(sourceNode, targetNode, attributes);
				}
				
				line = in.readLine();
			}
			
			out.finishedParsing();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean haltParsingNow() {
		return startedEdges;
	}
}
