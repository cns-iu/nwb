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
		
		String nodeNoQuotes = "[\\w_]+";
		String nodeWithQuotes = "(\"[^\"]+\")";
		String anyNode = "(" + nodeNoQuotes + "|" + nodeWithQuotes + ")";
		String optionalWeight = "(\\s+[+-]?(([0-9]+)?\\.)?[0-9]+)?";
		Pattern edgePattern = Pattern.compile(anyNode + "\\s+" + anyNode + optionalWeight);
		Matcher tokens = edgePattern.matcher(line);	
		tokens.matches(); 
		
		int sourceIndex = 1;
		int targetIndex = 3;
		int weightIndex = 5;
		
		String source = tokens.group(sourceIndex);
		String target = tokens.group(targetIndex);
		String weight = tokens.group(weightIndex);
		
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
		//regex parts
		String nodeNoQuotes = "[\\w_]+";
		String nodeWithQuotes = "(\"[^\"]+\")";
		String anyNode = "(" + nodeNoQuotes + "|" + nodeWithQuotes + ")";
		String optionalWeight = "(\\s+[+-]?(([0-9]+)?\\.)?[0-9]+)?";
		Pattern edgePattern = Pattern.compile(anyNode + "\\s+" + anyNode + optionalWeight);
		boolean isValid = edgePattern.matcher(line).matches();
		return isValid;
	}
}
