package edu.iu.nwb.converter.edgelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iu.nwb.converter.edgelist.nwbwritable.Node;
import edu.iu.nwb.converter.edgelist.nwbwritable.UnweightedEdge;
import edu.iu.nwb.converter.edgelist.nwbwritable.WeightedEdge;

public class EdgeListParser {
	private static String NODE_WITH_NO_QUOTES_REGEX = "[\\w_]+";
	private static String NODE_WITH_QUOTES_REGEX = "(\"[^\"]+\")|(\'[^\']+\')";
	
	private static String ANY_NODE_REGEX =
		"(" + NODE_WITH_NO_QUOTES_REGEX + "|" + NODE_WITH_QUOTES_REGEX + ")";
	
	private static String OPTIONAL_WEIGHT_REGEX =
		"(\\s+[+-]?(([0-9]+)?\\.)?[0-9]+)?";
	
	private static String EDGE_PATTERN_REGEX = ANY_NODE_REGEX + "\\s+" +
		ANY_NODE_REGEX + OPTIONAL_WEIGHT_REGEX;
	
	// Group indexes for EDGE_PATTERN_REGEX.
	private static int SOURCE_NODE_INDEX = 1;
	private static int TARGET_NODE_INDEX = 4;
	private static int EDGE_WEIGHT_INDEX = 7;
	
	private int totalNumOfNodes = 0;

	private List edges = new ArrayList();
	private List nodes = new ArrayList();
	
	private Map nodeNameToID = new HashMap();
	private int mapCount = 1;
	
	private static final boolean DEFAULT_WEIGHTEDNESS = false;
	private boolean isWeighted = DEFAULT_WEIGHTEDNESS;
	
	private static final boolean DEFAULT_DIRECTIONALITY = true;
	private boolean isUndirected = DEFAULT_DIRECTIONALITY;

	public EdgeListParser(File edgeFile) throws FileNotFoundException, IOException, InvalidEdgeListFormatException {
		parseEdgeFile(edgeFile);
	}

	public int getTotalNumOfNodes() {
		return this.totalNumOfNodes;
	}

	public boolean isWeighted() {
		return this.isWeighted;
	}

	public List getNodes() {
		return this.nodes;
	}
	
	public List getEdges() {
		return this.edges;
	}

	public boolean isUndirectedGraph() {
		return this.isUndirected;
	}

	public boolean isDirectedGraph() {
		return !this.isUndirected;
	}
	

	private void parseEdgeFile(File edgeList) throws InvalidEdgeListFormatException, IOException {
		BufferedReader edgeReader = new BufferedReader(new FileReader(edgeList));
		String line = edgeReader.readLine();
		if (line == null) { //empty file
			throw new InvalidEdgeListFormatException("The edgelist file was empty.");
		}
		
		line = line.trim();
		
		//if first line specifies directed or undirected, make a note
		if ("directed".equals(line)) {
			this.isUndirected = false;
			line = edgeReader.readLine();
		} else if ("undirected".equals(line)) {
			this.isUndirected = true;
			line = edgeReader.readLine();
		} else {
			//otherwise, network is treated as undirected by default
			this.isUndirected = DEFAULT_DIRECTIONALITY;
		}

		while (line != null) {

			//if the line is blank... 
			if (line.matches("^\\s*$")) { 
				//skip it.
			} else if (isValidEdgeLine(line)) {
				processEdge(line);
			} else {
				String errorMessage = "The line \n\t" + line + "\n was invalid.";
				throw new InvalidEdgeListFormatException(errorMessage);
			}
			line = edgeReader.readLine();
		}
	}

	/*
	 * This method saves information about the edge in this object
	 */
	private void processEdge(String line) {
		Pattern edgePattern = Pattern.compile(EDGE_PATTERN_REGEX);
		Matcher tokens = edgePattern.matcher(line);	
		tokens.matches();
		
		String possiblyQuotedSource = tokens.group(SOURCE_NODE_INDEX);
		String possiblyQuotedTarget = tokens.group(TARGET_NODE_INDEX);
		String weight = tokens.group(EDGE_WEIGHT_INDEX);
		
		// Strip all single- and double-quotes from the source and target.
		String source = stripAllQuotesFromString(possiblyQuotedSource);
		String target = stripAllQuotesFromString(possiblyQuotedTarget);
		
		//add nodes into map
		
		//add source node
		if (! nodeNameToID.containsKey(source)) {
			int sourceID = mapCount++;
			nodeNameToID.put(source, new Integer(sourceID));
			nodes.add(new Node(sourceID, source));
		}
		
		//add target node
		if (! nodeNameToID.containsKey(target)) {
			int targetID = mapCount++;
			nodeNameToID.put(target, new Integer(targetID));
			nodes.add(new Node(targetID, target));
		}
		
		//add edge
		
		int sourceID = ((Integer) nodeNameToID.get(source)).intValue();
		int targetID = ((Integer) nodeNameToID.get(target)).intValue();
		
		if (weight != null) {
			this.isWeighted = true;
			
			double weightAsDouble = Double.parseDouble(weight);
			edges.add(new WeightedEdge(sourceID, targetID, weightAsDouble));
		} else {
			this.isWeighted = false;
			
			edges.add(new UnweightedEdge(sourceID, targetID));
		}
	}

	private boolean isValidEdgeLine(String line) {
		Pattern edgePattern = Pattern.compile(EDGE_PATTERN_REGEX);
		boolean isValid = edgePattern.matcher(line).matches();
		
		return isValid;
	}
	
	private String stripAllQuotesFromString(String originalString) {
		return originalString.replaceAll("[\'\"]", "");
	}
}
