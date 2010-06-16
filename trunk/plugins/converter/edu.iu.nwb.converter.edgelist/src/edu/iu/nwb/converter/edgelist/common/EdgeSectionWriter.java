package edu.iu.nwb.converter.edgelist.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.UnicodeReader;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class EdgeSectionWriter {
	private NWBFileParserHandler writer;
	private final Map nodeNameToID;
	public static final String WEIGHT_NWB_ATTRIBUTE_KEY = "weight";
	public static final boolean IS_DIRECTED_DEFAULT = false;
	

	public EdgeSectionWriter(NWBFileParserHandler writer, Map nodeNameToID) {
		this.writer = writer;
		this.nodeNameToID = nodeNameToID;
	}
	
	
	public void write(final File edgeListFile, boolean validateOnly)
			throws InvalidEdgeListFormatException,
				   IOException,
				   AlgorithmExecutionException {
		// Determine global settings and write the section header
		boolean isWeighted = findWeightedness(edgeListFile);
		boolean isDirected = isDirected(edgeListFile);
		writeEdgeSectionHeader(isWeighted, isDirected);
	
		final BufferedReader edgeReader =
			new BufferedReader(new UnicodeReader(new FileInputStream(edgeListFile)));
		String line = edgeReader.readLine();
	
		// Skip the directedness line, if present
		if (line.matches(EdgeListParser.UNDIRECTED_REGEX)
				|| line.matches(EdgeListParser.DIRECTED_REGEX)) {
			line = edgeReader.readLine();
		}
	
		/* For each edge line in edgeListFile, declare the corresponding
		 * NWB edge.
		 */		
		while (line != null) {
			if (line.matches(EdgeListParser.LINE_ONLY_WHITESPACE_REGEX)) {
				// Skip whitespace lines
			} else if (EdgeListParser.isValidEdgeLine(line)) {
				if (!validateOnly) {
					writeEdgeOnLine(line, isDirected);
				}
			} else {
				String errorMessage = "The line \n\t" + line
						+ "\n was invalid.";
				throw new InvalidEdgeListFormatException(errorMessage);
			}
			line = edgeReader.readLine();
		}
		
		edgeReader.close();
	}

	/* Write an NWB edge schema which is weighted (that is, has a weight*float
	 * edge attribute) and/or directed, as instructed.
	 * Note edges must and will have source*int and target*int attributes.
	 */
	private void writeEdgeSectionHeader(boolean isWeighted,
										boolean isDirected) {
		LinkedHashMap edgeSchema = new LinkedHashMap();
		edgeSchema.put(NWBFileProperty.ATTRIBUTE_SOURCE,
				NWBFileProperty.TYPE_INT);
		edgeSchema.put(NWBFileProperty.ATTRIBUTE_TARGET,
				NWBFileProperty.TYPE_INT);
	
		if (isWeighted) {
			edgeSchema.put(EdgeSectionWriter.WEIGHT_NWB_ATTRIBUTE_KEY,
						   NWBFileProperty.TYPE_FLOAT);
		}
	
		if (isDirected) {
			writer.setDirectedEdgeSchema(edgeSchema);
		} else {
			writer.setUndirectedEdgeSchema(edgeSchema);
		}
	}

	/* Read the source and target node names on this line.
	 * Look up the NWB node IDs that nodeNameToID assigns for them.
	 * If a weight is read, add that NWB edge attribute.
	 */
	private void writeEdgeOnLine(String line, boolean isDirected)
			throws AlgorithmExecutionException {
		Matcher tokens = EdgeListParser.EDGE_PATTERN.matcher(line);
		tokens.matches();
	
		String sourceName = tokens.group(EdgeListParser.SOURCE_NODE_INDEX);
		int sourceID = getNodeIDFromName(sourceName);
		
		String targetName = tokens.group(EdgeListParser.TARGET_NODE_INDEX);
		int targetID = getNodeIDFromName(targetName);
	
		String weight = tokens.group(EdgeListParser.EDGE_WEIGHT_INDEX);
		Map attributes = new HashMap();
		if (weight != null) {
			attributes.put(EdgeSectionWriter.WEIGHT_NWB_ATTRIBUTE_KEY,
						   Double.valueOf(weight));
		}
	
		if (isDirected) {
			writer.addDirectedEdge(sourceID, targetID, attributes);
		} else {
			writer.addUndirectedEdge(sourceID, targetID, attributes);
		}
	}

	/* Check whether the first line of edgeListFile is, with some whitespace
	 * tolerance, "directed" or "undirected".  If neither is found, assume the
	 * default directedness.
	 */
	private boolean isDirected(File edgeListFile)
			throws IOException, InvalidEdgeListFormatException {
		final BufferedReader edgeReader =
			new BufferedReader(new UnicodeReader(new FileInputStream(edgeListFile)));
		String firstLine = edgeReader.readLine();
		edgeReader.close();
		
		if (firstLine.matches(EdgeListParser.DIRECTED_REGEX)) {
			return true;
		} else if (firstLine.matches(EdgeListParser.UNDIRECTED_REGEX)) {
			return false;
		} else {
			return EdgeSectionWriter.IS_DIRECTED_DEFAULT;
		}
	}

	/* Assume the EdgeList is intended to be weighted
	 * if and only if
	 * the first valid edge line in edgeListFile declares a weight.
	 * 
	 * This is a contrived heuristic.  We may rather, for example, want to
	 * assume weightedness if and only if *any* valid edge line
	 * declares a weight.
	 */
	private boolean findWeightedness(File edgeListFile)
			throws InvalidEdgeListFormatException, IOException {
		final BufferedReader edgeReader =
			new BufferedReader(new UnicodeReader(new FileInputStream(edgeListFile)));
		String line = edgeReader.readLine();
		
		// Skip the directedness line, if present
		if (line.matches(EdgeListParser.UNDIRECTED_REGEX)
				|| line.matches(EdgeListParser.DIRECTED_REGEX)) {
			line = edgeReader.readLine();
		}
		
		while (line != null) {
			if (line.matches(EdgeListParser.LINE_ONLY_WHITESPACE_REGEX)) { 
				// Skip whitespace lines
			}
			else {
				Matcher edgeLineMatcher =
					EdgeListParser.EDGE_PATTERN.matcher(line);
				
				if (edgeLineMatcher.matches()) {
					edgeReader.close();
					
					String weight =
						edgeLineMatcher.group(EdgeListParser.EDGE_WEIGHT_INDEX);
					
					if (weight == null) {
						return false;
					} else {
						return true;
					}
				}
				else {
					String message = "The line \n\t" + line + "\n was invalid.";
					throw new InvalidEdgeListFormatException(message);
				}
			}
			line = edgeReader.readLine();
		}
		
		edgeReader.close();
		return false;
	}

	// Clean the EdgeList node name and return its assigned NWB node ID.
	private int getNodeIDFromName(String possiblyQuotedName)
			throws AlgorithmExecutionException {
		String name = EdgeListParser.stripAllQuoteCharacters(possiblyQuotedName);
		if (nodeNameToID.containsKey(name)) {
			int nodeID = ((Integer) nodeNameToID.get(name)).intValue();
			return nodeID;
		}
		else {
			String message = "Node name didn't have a corresponding ID.";
			throw new AlgorithmExecutionException(message);
		}
	}
}
