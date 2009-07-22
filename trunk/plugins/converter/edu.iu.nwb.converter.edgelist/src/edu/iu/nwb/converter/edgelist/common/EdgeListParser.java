package edu.iu.nwb.converter.edgelist.common;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

/* Common code for validation of EdgeList and edu.iu.nwb.converter.edgelist.conversion of EdgeList to NWB.
 * A validator will pass a null object for the writer and set validateOnly=true.
 * A converter will pass an NWBFileWriter for the writer
 * and set validateOnly=false.
 */
public class EdgeListParser {
	public static final String DIRECTED_REGEX = "^\\s*directed\\s*$";
	public static final String UNDIRECTED_REGEX = "^\\s*undirected\\s*$";
	
	public static final String LINE_ONLY_WHITESPACE_REGEX = "^\\s*$";
	
	public static final String SINGLE_OR_DOUBLE_QUOTE_REGEX = "[\'\"]";
	
	private static final String NODE_WITH_NO_QUOTES_REGEX = "[\\w_]+";
	private static final String NODE_WITH_QUOTES_REGEX =
		"(\"[^\"]+\")|(\'[^\']+\')";	
	private static final String ANY_NODE_REGEX =
		"(" + NODE_WITH_NO_QUOTES_REGEX + "|" + NODE_WITH_QUOTES_REGEX + ")";
	private static final String OPTIONAL_WEIGHT_REGEX =
		"(\\s+[+-]?(([0-9]+)?\\.)?[0-9]+)?";
	private static final String EDGE_PATTERN_REGEX =
		ANY_NODE_REGEX + "\\s+" + ANY_NODE_REGEX + OPTIONAL_WEIGHT_REGEX;
	public static final Pattern EDGE_PATTERN =
		Pattern.compile(EDGE_PATTERN_REGEX);	
	// Group indexes for EDGE_PATTERN
	public static final int SOURCE_NODE_INDEX = 1;
	public static final int TARGET_NODE_INDEX = 4;
	public static final int EDGE_WEIGHT_INDEX = 7;
	
	private File edgeListFile;
	private boolean validateOnly;

	
	public EdgeListParser(File edgeListFile) {
		this(edgeListFile, false);		
	}
	
	/* validateOnly means perform only operations necessary for validation.
	 * For conversion, this will be false.
	 */
	public EdgeListParser(File edgeListFile, boolean validateOnly) {
		this.edgeListFile = edgeListFile;
		this.validateOnly = validateOnly;
	}

	
	public void parseInto(NWBFileParserHandler writer)
			throws InvalidEdgeListFormatException,
				   IOException,
				   AlgorithmExecutionException {
		if (FileUtilities.isFileEmpty(edgeListFile)) {			
			throw new InvalidEdgeListFormatException("The input file was empty.");
		}		
		
		// Write the NWB nodes section
		NodeSectionWriter nodeSectionWriter = new NodeSectionWriter(writer);
		nodeSectionWriter.write(edgeListFile, validateOnly);
		
		/* Retrieve the association between node names in the EdgeList file
		 * and node IDs in the NWB file.
		 */
		final Map nodeNameToID = nodeSectionWriter.getNodeNameToIDMap();
		
		// Write the NWB edges section.
		EdgeSectionWriter edgeSectionWriter =
			new EdgeSectionWriter(writer, nodeNameToID);
		edgeSectionWriter.write(edgeListFile, validateOnly);
		
		writer.finishedParsing();
	}

	protected static boolean isValidEdgeLine(String line) {
		return EDGE_PATTERN.matcher(line).matches();
	}

	protected static String stripAllQuoteCharacters(String originalString) {
		return originalString.replaceAll(SINGLE_OR_DOUBLE_QUOTE_REGEX, "");
	}
}
