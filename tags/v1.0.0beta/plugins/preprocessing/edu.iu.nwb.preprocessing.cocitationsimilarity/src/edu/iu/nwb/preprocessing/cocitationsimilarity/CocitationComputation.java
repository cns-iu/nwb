package edu.iu.nwb.preprocessing.cocitationsimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class CocitationComputation implements NWBFileParserHandler {
	//private NWBFileWriter out;
	public Map citationCount;
	private Map adjacencyList;
	public Map cocitationCount;
	
	
	public CocitationComputation() throws IOException {
		//out = new NWBFileWriter(outputNWBFile);
		citationCount = new HashMap();
		adjacencyList = new HashMap();
	}

	
	public void setNodeCount(int numberOfNodes) {		
		//out.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		//schema.put("citations", NWBFileProperty.TYPE_INT);
		//out.setNodeSchema(schema);
	}
	
	
	
	public void addNode(int id, String label, Map attributes) {
		//out.addNode(id, label, attributes);
	}


	private void updateCiteCount(Integer nodeid) {
		if(citationCount.containsKey(nodeid)) {
			citationCount.put(nodeid, new Integer(((Integer) citationCount.get(nodeid)).intValue() + 1));
		} else {
			citationCount.put(nodeid, new Integer(1));
		}
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		Integer source = new Integer(sourceNode);
		Integer target = new Integer(targetNode);
		updateCiteCount(target);
		if(!adjacencyList.containsKey(source)) {
			adjacencyList.put(source, new HashSet());
		}
		((Set) adjacencyList.get(source)).add(target);
	}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {}
	public void setDirectedEdgeCount(int numberOfEdges) {}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {}
	public void setUndirectedEdgeCount(int numberOfEdges) {}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {}

	public void addComment(String comment) {}
	
	private Set pairs(Collection values) {
		Set pairs = new HashSet();
		List vals = new ArrayList(values);
		int number = vals.size();
		for(int ii = 0; ii < number; ii++) {
			for(int jj = ii + 1; jj < number; jj++) {
				Set pair = new HashSet();
				pair.add(vals.get(ii));
				pair.add(vals.get(jj));
				pairs.add(pair);
			}
		}
		return pairs;
	}
	
	public void finishedParsing() {
		cocitationCount = new HashMap();
		Iterator adjacencyIterator = adjacencyList.values().iterator();
		while(adjacencyIterator.hasNext()) {
			Set adjacentNodes = (Set) adjacencyIterator.next();
			Iterator pairs = pairs(adjacentNodes).iterator();
			while(pairs.hasNext()) {
				Object pair = pairs.next();
				if(cocitationCount.containsKey(pair)) {
					cocitationCount.put(pair, new Integer(((Integer) cocitationCount.get(pair)).intValue() + 1));
				} else {
					cocitationCount.put(pair, new Integer(1));
				}
			}
		}
		adjacencyList = null;
		
		/* LinkedHashMap schema = new LinkedHashMap();
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
		*/
		
	}

	public boolean haltParsingNow() {
		return false;
	}
}
