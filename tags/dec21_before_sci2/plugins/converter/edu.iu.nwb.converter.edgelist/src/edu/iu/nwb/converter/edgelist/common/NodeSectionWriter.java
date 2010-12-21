package edu.iu.nwb.converter.edgelist.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.UnicodeReader;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

/* Determine and write the nodes implied by the lines in edgeListFile.
 * Since the node names in edgeListFile may generally not be valid NWB node IDs
 * (that is, positive integers) we must assign new IDs to each implied node.
 * The association between the original EdgeList node name and the generated
 * NWB node ID is recorded in nodeNameToID.
 */
public class NodeSectionWriter {
	public static final int FIRST_NODE_ID = 1;
	
	private NWBFileParserHandler writer;
	private final Map nodeNameToID;
	private int nextAvailableNodeID;

	
	public NodeSectionWriter(NWBFileParserHandler writer) {
		this.writer = writer;
		
		this.nodeNameToID = new HashMap();
		this.nextAvailableNodeID = FIRST_NODE_ID;
	}
	
	
	public void write(final File edgeListFile, boolean validateOnly)
			throws InvalidEdgeListFormatException,
				   IOException,
				   AlgorithmExecutionException {
		writer.setNodeSchema(NWBFileProperty.NECESSARY_NODE_ATTRIBUTES);
		
		final BufferedReader edgeReader =
			new BufferedReader(new UnicodeReader(new FileInputStream(edgeListFile)));
		String line = edgeReader.readLine();
		
		// Skip the directedness line, if present
		if (line.matches(EdgeListParser.UNDIRECTED_REGEX)
				|| line.matches(EdgeListParser.DIRECTED_REGEX)) {
			line = edgeReader.readLine();
		}
		
		/* For each edge line:
		 * (1) Determine the implied nodes
		 * (2) Record their EdgeList-name to NWB-ID associations in nodeNameToID
		 * (3) Tell writer to write the node
		 */
		while (line != null) {
			if (line.matches(EdgeListParser.LINE_ONLY_WHITESPACE_REGEX)) { 
				// Skip whitespace lines
			}
			else {
				Matcher edgeLineMatcher =
					EdgeListParser.EDGE_PATTERN.matcher(line);
				
				if (edgeLineMatcher.matches()) {
					if (!validateOnly) {
						writeNodesOnLine(edgeLineMatcher);
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
	}

	public Map getNodeNameToIDMap() {
		return nodeNameToID;
	}

	/* If this is a valid EdgeList edge line, determine the implied nodes
	 * and write a node declaration only if a node with this EdgeList name
	 * has not been previously processed.
	 */
	private void writeNodesOnLine(Matcher edgeLineMatcher)
			throws AlgorithmExecutionException {
		if (edgeLineMatcher.matches()) {
			// Note matches() prepares the matcher to read tokens using group()
			String sourceToken =
				edgeLineMatcher.group(EdgeListParser.SOURCE_NODE_INDEX);
			writeNodeIfNew(sourceToken);
			
			String targetToken =
				edgeLineMatcher.group(EdgeListParser.TARGET_NODE_INDEX);
			writeNodeIfNew(targetToken);
		}
		else {
			throw new AlgorithmExecutionException("Cannot read edge data from "
				+ "an invalid EdgeList edge line.");
		}
	}

	/* Clean the given node name of any quote characters and, if we have not
	 * already declared a node with this name, declare it and associate this
	 * name with the next available (that is, unused) node ID. 
	 */
	private void writeNodeIfNew(String possiblyQuotedNodeName) {
		String nodeName =
			EdgeListParser.stripAllQuoteCharacters(possiblyQuotedNodeName);

		if ( !nodeNameToID.containsKey(nodeName)) {
			int nodeID = nextAvailableNodeID++;
			nodeNameToID.put(nodeName, new Integer(nodeID));
			writer.addNode(nodeID, nodeName, new HashMap());
		}
	}
}
