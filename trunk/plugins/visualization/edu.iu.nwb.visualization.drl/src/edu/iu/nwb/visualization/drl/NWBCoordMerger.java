package edu.iu.nwb.visualization.drl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import cern.colt.map.OpenIntDoubleHashMap;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Handler class that merges coordinates from a coord file and
 * an associated NWB File, outputting a merged NWB file.
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class NWBCoordMerger implements NWBFileParserHandler {
	private String xpos;
	private String ypos;
	private NWBFileWriter out;
	private NWBFileParser parser;
	private File coordFile;
	
	private int numNodes = -1;
	private OpenIntDoubleHashMap idToXMap;
	private OpenIntDoubleHashMap idToYMap;
	
	public NWBCoordMerger(File coordFile,File srcNWBFile,String xpos,String ypos,File outputNWBFile) throws IOException {
		this.xpos = xpos;
		this.ypos = ypos;
		this.coordFile = coordFile;
		
		out = new NWBFileWriter(outputNWBFile);
		parser = new NWBFileParser(srcNWBFile);
	}
	
	public void merge() throws IOException, ParsingException {
		parser.parse(this);
	}

	private void readCoords() throws IOException {
		idToXMap = new OpenIntDoubleHashMap();
		idToYMap = new OpenIntDoubleHashMap();
		
		if (numNodes > 0) {
			idToXMap.ensureCapacity(numNodes);
			idToYMap.ensureCapacity(numNodes);
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(coordFile),"UTF-8"));
		
		String line = in.readLine();
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line.trim());
			
			if (st.countTokens() > 1) {
				int id = Integer.parseInt(st.nextToken());
				float x = Float.parseFloat(st.nextToken());
				float y = Float.parseFloat(st.nextToken());
				
				idToXMap.put(id, x);
				idToYMap.put(id, y);
			}
			
			line = in.readLine();
		}	
	}
	
	public void setNodeCount(int numberOfNodes) {
		numNodes = numberOfNodes;
		
		out.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		schema.put(xpos, NWBFileProperty.TYPE_FLOAT);
		schema.put(ypos, NWBFileProperty.TYPE_FLOAT);
		
		out.setNodeSchema(schema);
		
		if (idToXMap == null) {
			try {
				readCoords();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void addNode(int id, String label, Map attributes) {
		if (idToXMap.containsKey(id)) {
			attributes.put(xpos,""+idToXMap.get(id));
			attributes.put(ypos,""+idToYMap.get(id));
		}
		out.addNode(id, label, attributes);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		out.addDirectedEdge(sourceNode, targetNode, attributes);
	}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		out.addUndirectedEdge(node1, node2, attributes);
	}
	public void setDirectedEdgeCount(int numberOfEdges) {
		out.setDirectedEdgeCount(numberOfEdges);
	}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		out.setDirectedEdgeSchema(schema);
	}
	public void setUndirectedEdgeCount(int numberOfEdges) {
		out.setUndirectedEdgeCount(numberOfEdges);
	}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		out.setUndirectedEdgeSchema(schema);
	}

	public void addComment(String comment) {
		out.addComment(comment);
	}
	
	public void finishedParsing() {
		out.finishedParsing();
	}

	public boolean haltParsingNow() {
		return false;
	}
}
