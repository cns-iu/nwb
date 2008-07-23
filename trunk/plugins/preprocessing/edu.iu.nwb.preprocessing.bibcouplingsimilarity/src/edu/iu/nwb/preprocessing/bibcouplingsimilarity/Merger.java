package edu.iu.nwb.preprocessing.bibcouplingsimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class Merger implements NWBFileParserHandler {

	private NWBFileWriter out;
	private NWBFileParser parser;
	private File edgeFile;
	private boolean startedEdges;
	private Map citationCount;
	private Map cocitationCount;
	
	public Merger(File edgeFile,File srcNWBFile,File outputNWBFile) throws IOException {
		this.edgeFile = edgeFile;
		
		startedEdges = false;
		out = new NWBFileWriter(outputNWBFile);
		parser = new NWBFileParser(srcNWBFile);
	}
	
	public Merger(CocitationComputation computation, File outputNWBFile) throws IOException {
		startedEdges = false;
		out = new NWBFileWriter(outputNWBFile);
		citationCount = computation.citationCount;
		cocitationCount = computation.cocitationCount;
	}

	
	public void setNodeCount(int numberOfNodes) {		
		out.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		schema.put("cited", NWBFileProperty.TYPE_INT);
		out.setNodeSchema(schema);
	}
	
	public void addNode(int id, String label, Map attributes) {
		Integer citations = (Integer) citationCount.get(new Integer(id));
		if(citations == null) {
			citations = new Integer(0);
		}
		attributes.put("cited", citations);
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
		LinkedHashMap schema = new LinkedHashMap();
		schema.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
		schema.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
		schema.put("weight",NWBFileProperty.TYPE_INT);
		out.setUndirectedEdgeSchema(schema);
		
		Iterator pairs = cocitationCount.keySet().iterator();
		while(pairs.hasNext()) {
			Set pair = (Set) pairs.next();
			int pairCount = ((Integer) cocitationCount.get(pair)).intValue();
			Integer[] both = (Integer[]) pair.toArray(new Integer[]{});
			//int firstCount = ((Integer) citationCount.get(both[0])).intValue();
			//int secondCount = ((Integer) citationCount.get(both[1])).intValue();
			
			Map attributes = new HashMap();
			attributes.put("weight", new Integer(pairCount));
			
			out.addUndirectedEdge(both[0].intValue(), both[1].intValue(), attributes);
		}
		
		out.finishedParsing();
	}

	public boolean haltParsingNow() {
		return startedEdges;
	}
}
