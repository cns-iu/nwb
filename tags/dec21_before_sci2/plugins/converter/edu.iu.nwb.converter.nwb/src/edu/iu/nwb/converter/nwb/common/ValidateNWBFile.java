package edu.iu.nwb.converter.nwb.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.UnicodeReader;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class ValidateNWBFile {
	private boolean hasHeader_Nodes = false;
	private boolean hasHeader_UndirectedEdges = false;
	private boolean hasHeader_DirectedEdges = false;

	private boolean isFileGood = true;

	private boolean inNodesSection = false;
	private boolean inUndirectededgesSection = false;
	private boolean inDirectededgesSection = false;

	private boolean hasTotalNumOfNodes = false;

	/* If true, it indicates the previous line is a section header
	 * It is useful to parse the attribute line
	 */
	private boolean passHeader = false;

	private int totalNumOfNodes;
	private int currentLine;
	private int countedNumDirected;
	private int countedNumUnDirected;
	private int countedNodes;

	private StringBuffer errorMessages = new StringBuffer();

	private List nodeAttrList, directedEdgeAttrList, undirectedEdgeAttrList;

	
	/* Invokes processFile with a BufferedReader corresponding
	 * to the file that we will validate.
	 */
	public void validateNWBFormat(File fileHandler)
			throws FileNotFoundException, IOException {
		currentLine = 0;
		countedNumDirected = 0;
		countedNumUnDirected = 0;
		countedNodes = 0;
		BufferedReader reader =
			new BufferedReader(new UnicodeReader(new FileInputStream(fileHandler)));
		processFile(reader);
	}

	private void processFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();		
		
		while (line != null && isFileGood) {
			currentLine++;
			line = line.trim();
			if (line.startsWith(NWBFileProperty.PREFIX_COMMENTS)
					|| line.length()<=0) {
				line = reader.readLine();
				continue;				
			}			
			// process section header that looks like
			// *Nodes or *Nodes 1000
			if (validateNodeHeader(line) ) {
				line = reader.readLine();
				continue;
			}
			if (validateDirectedEdgeHeader(line)) {
				line = reader.readLine();
				continue;
			}
			if(validateUndirectedEdgeHeader(line)) {
				line = reader.readLine();
				continue;
			}

			if (inNodesSection && isFileGood) {				
				processNodes(line);
				//nodes++;
				line = reader.readLine();
				continue;
			}

			if (inDirectededgesSection && isFileGood) {
				processDirectedEdges(line);
				//dedges++;
				line = reader.readLine();
				continue;
			}

			if (inUndirectededgesSection && isFileGood) {
				processUndirectedEdges(line);
				
				line = reader.readLine();
				continue;
			}
			line = reader.readLine();
		}

		if (isFileGood) {
			checkFile();
		}

		// TODO: Should this really matter?
		if (this.hasTotalNumOfNodes && (this.countedNodes != this.totalNumOfNodes)) {
			// I'm not sure if we should set this to false or not.
			// TODO: It should not?
			isFileGood = false;
			errorMessages.append(
				"There was an inconsistency between the specified number of nodes: " 
				+ this.totalNumOfNodes + " and the " +
				"number of nodes counted: " + this.countedNodes);
		}
		
		this.totalNumOfNodes = this.countedNodes;
	}
		
	private void checkFile() {
		if (!hasHeader_Nodes) {
			isFileGood = false;
			errorMessages.append("*The file does not specify the node header.\n\n");
		} else if (!hasHeader_DirectedEdges && !hasHeader_UndirectedEdges) {
			isFileGood = false;
			errorMessages.append("This file has not specified a valid edge header.");
		} 		
	}
	
	/**
	 *  validateNodeHeader takes in a string and checks to see if it starts with 
	 *  *nodes with an optional integer following the nodes declaration.
	 */
	private boolean validateNodeHeader(String s) {		
		if (s.startsWith(NWBFileProperty.HEADER_NODE)) {
			hasHeader_Nodes = true;
			inNodesSection = true;
			inDirectededgesSection = false;
			inUndirectededgesSection = false;
			passHeader = true;
			nodeAttrList = new ArrayList();

			//get the total number of nodes
			StringTokenizer st = new StringTokenizer(s);
			if (st.countTokens() > 1) {
				st.nextToken();
				//*****If it is not an integer...
				totalNumOfNodes = new Integer(st.nextToken()).intValue();
				hasTotalNumOfNodes = true;
			} else {
				hasTotalNumOfNodes = false;
			}
			return true;

		}
		else{
//			String lower = s.toLowerCase();
//			if(lower.equals(NWBFileProperty.HEADER_NODE.toLowerCase())){
			if(s.equalsIgnoreCase(NWBFileProperty.HEADER_NODE)){
				isFileGood = false;
				errorMessages.append(
						"The header of the node section in an nwb file should be "
						+ NWBFileProperty.HEADER_NODE
						+ " and it is case sensitive. The current header is "
						+ s + ".\n\n");
				return false;				
			}			
		}
		return false;

	}

	/*
	 * validateDirectedEdgeHeader takes a string corresponding to a line in the current NWB file 
	 * being processed.  The function returns a boolean corresponding to whether the string
	 * is a header for a directed edge.
	 */
	private boolean validateDirectedEdgeHeader(String s) {
		if (s.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) {
			hasHeader_DirectedEdges = true;
			inDirectededgesSection = true;
			inNodesSection = false;
			inUndirectededgesSection = false;
			passHeader = true;
			directedEdgeAttrList = new ArrayList();
			return true;
		}
		else{
//			String lower = s.toLowerCase();
//			if(lower.equals(NWBFileProperty.HEADER_DIRECTED_EDGES.toLowerCase())){
			if(s.equalsIgnoreCase(NWBFileProperty.HEADER_DIRECTED_EDGES)){
				isFileGood = false;
				errorMessages
				.append("The header of the directed edge section should be "+
						NWBFileProperty.HEADER_DIRECTED_EDGES+
						" and it is case sensitive. The current header is "+s+".\n\n");
				return false;				
			}			
		}

		return false;
	}

	/*
	 * validateUndirectedEdgeHeader will check the input string and return true if that string
	 * refers to a valid undirected edge header.
	 */
	private boolean validateUndirectedEdgeHeader(String s) {
		if (s.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) {
			hasHeader_UndirectedEdges = true;
			inUndirectededgesSection = true;
			inNodesSection = false;
			inDirectededgesSection = false;
			passHeader = true;
			undirectedEdgeAttrList = new ArrayList();
			return true;
		}
		else{
			//String lower = s.toLowerCase();
			//if(lower.equals(NWBFileProperty.HEADER_UNDIRECTED_EDGES.toLowerCase())){
			if(s.equalsIgnoreCase(NWBFileProperty.HEADER_UNDIRECTED_EDGES)){
				isFileGood = false;
				errorMessages
				.append("The header of the undirected edge section should be "+
						NWBFileProperty.HEADER_UNDIRECTED_EDGES+
						" and it is case sensitive. The current header is "+s+".\n\n");
				return false;				
			}			
		}

		return false;
	}

	/*
	 * 
	 */
	private void processNodes(String s) {
		//s = s.toLowerCase();
		if (passHeader) {// if previous line is a node header
			/*
			 * get attribute line handle only one case: 
			 * id*int label*string attr3*dataType .... 
			 */
			if (s.startsWith(NWBFileProperty.ATTRIBUTE_ID)) {
				// process attribut line
				StringTokenizer st = new StringTokenizer(s);
				int totalTokens = st.countTokens();
				for (int i = 1; i <= totalTokens; i++) {
					// process token
					try {
						NWBAttribute attr = processAttrToken(st.nextToken());
						nodeAttrList.add(attr);
					} catch (Exception e) {
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString()+"\n\n");
						break;
					}

				}
			}
			else {
				isFileGood = false;
				errorMessages.append("*Wrong NWB format at line "
						+ currentLine + ". The attribute line is missing.\n\n");
				
			}
			passHeader = false;
			
		} else {// process node list
			// based on nodeAttrList to detect each node item in the node list
			// basically, make sure if there's a value, the value belongs to the
			// declared data type. If it is a string, it must be surrounded by double
			// quotations.
			try {
				validateALine(s, nodeAttrList);
				this.countedNodes++;
			} catch (Exception e) {
				isFileGood = false;
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}

	}

	private void processDirectedEdges(String s) {
		//s.toLowerCase();
		if (passHeader) {// if previous line is an edge header
			/*
			 * get attribute line handle only one case:
			 * source*int target*int attr3*dataType .... 
			 */
			if (s.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)) {
				// process attribut line
				StringTokenizer st = new StringTokenizer(s);
				int tokens = st.countTokens();
				for (int i = 1; i <= tokens; i++) {
					String token = st.nextToken();
					// process token
					try {
						NWBAttribute attr = processAttrToken(token);
						directedEdgeAttrList.add(attr);
					} catch (Exception e) {
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString() + "\n\n");
						break;
					}
				}			
			}
			else {
					isFileGood = false;
					errorMessages
							.append("*Wrong NWB format at line "
									+ currentLine
									+ ".\n"
									+ "The attribute line is missing.\n\n");
			}
			passHeader = false;
			
		} else {
			try {
				validateALine(s, directedEdgeAttrList);
				this.countedNumDirected++;
			} catch (Exception e) {
				isFileGood = false;
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}
	}

	private void processUndirectedEdges(String s) {
		//s.toLowerCase();
		if (passHeader) {// if previous line is an edge header
			/*
			 * get attribute line handle one cases: 
			 * source*int target*int attr3*dataType .... 
			 */
			if (s.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)) {
				// process attribut line
				StringTokenizer st = new StringTokenizer(s);
				int tokens = st.countTokens();
				for (int i = 1; i <= tokens; i++) {
					String token = st.nextToken();
					// process token
					try {
						NWBAttribute attr = processAttrToken(token);
						undirectedEdgeAttrList.add(attr);
					} catch (Exception e) {
						isFileGood = false;
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString() + "\n\n");
						break;
					}

				}				
			}
			else {
					isFileGood = false;
					errorMessages
							.append("*Wrong NWB format at line "
									+ currentLine
									+ ".\n"
									+ "The attribute line is missing.\n\n");
			}
			passHeader = false;			
		} else {
			try {
				validateALine(s, undirectedEdgeAttrList);
				this.countedNumUnDirected++;

			} catch (Exception e) {
				isFileGood = false;
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}
	}

	private boolean validateALine(String line, List attrList) throws AlgorithmExecutionException {
		if (line.length() <= 0
				|| line.startsWith(NWBFileProperty.PREFIX_COMMENTS)) {
			return true;
		}
		StringTokenizer st = new StringTokenizer(line);
		int totalTokens = st.countTokens();
		if (totalTokens <= 0) {
			return true;
		}
		if (totalTokens < attrList.size()) {
			throw new AlgorithmExecutionException(
					"Did not specify all values for defined attributes!");
		}
		
		String[] columns = processTokens(st);
		for (int ii = 0; ii < attrList.size(); ii++) {
			NWBAttribute nwbAttr = (NWBAttribute) attrList.get(ii);
			String dt = nwbAttr.getDataType();
			if(columns[ii].equalsIgnoreCase("*")){
				// Do nothing?
			} else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_STRING)) {
				isAString(columns[ii], nwbAttr.getAttrName());
			} else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_INT)) {
				isAnInteger(columns[ii], nwbAttr.getAttrName());
			} else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)
						|| dt.equalsIgnoreCase(NWBFileProperty.TYPE_REAL)) {
				isAFloat(columns[ii], nwbAttr.getAttrName());
			}
		}
		
		return true;
	}

	private boolean isAnInteger(String input, String attr)
			throws NumberFormatException, AlgorithmExecutionException {
		Integer value = new Integer(input);
		if (attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)
				|| attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE)
				|| attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)) {
			if (value.intValue() < 1)
				throw new AlgorithmExecutionException(
						"The node id must be greater than 0.");
		}
		
		return true;
	}

	private boolean isAString(String input, String attr)
			throws AlgorithmExecutionException {
		if (!input.startsWith("\"") || !input.endsWith("\"")) {
			throw new AlgorithmExecutionException(
					"A string value must be surrounded by double quatation marks.");
		}
		
		return true;
	}

	private boolean isAFloat(String input, String attr)
			throws NumberFormatException {
		Float f = new Float(input);
		f.floatValue();
		return true;
	}

	private NWBAttribute processAttrToken(String token)
			throws AlgorithmExecutionException {
		if (token.indexOf("*") != -1) {
			String attr_name = token.substring(0, token.indexOf("*"));
			if (attr_name.startsWith("//"))
				attr_name = attr_name.substring(2);
			String attr_type = token.substring(token.indexOf("*") + 1);
			if (!(attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)
					|| attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_REAL)
					|| attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_INT)
					|| attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_STRING))) {
				throw new AlgorithmExecutionException(
						"The data type of the attribute " + attr_name
						+ " is not acceptable. Only float, int and string are "
						+ "valid data types in the NWB format.\n"
						+ "You supplied an attribute of " + attr_type);
			} else
				return new NWBAttribute(attr_name, attr_type);
		} else
			throw new AlgorithmExecutionException(
					"Cannot find * from attribute*datatype line.");
	}

	public String[] processTokens(StringTokenizer st) {
		int total = st.countTokens();
		int tokenIndex = 0;
		String[] tokens = new String[total];
		StringBuffer bf = new StringBuffer();
		boolean append = false;
		for (int index = 0; index < total; index++) {
			String element = st.nextToken();
			if (!append) {
				if (!element.startsWith("\"")) {
					tokens[tokenIndex] = element;
					tokenIndex++;

				} else if (element.startsWith("\"")
							&& element.endsWith("\"")
							&& !element.equals("\"")) {
					tokens[tokenIndex] = element;
					tokenIndex++;

				} else {
					append = true;
					bf.append(element);
				}
			} else {
				if (element.endsWith("\\\"") || !element.endsWith("\"")) {
					bf.append(" " + element);
				} else if (element.endsWith("\"")) {
					bf.append(" " + element);
					tokens[tokenIndex] = bf.toString();
					tokenIndex++;
					bf = new StringBuffer();
					append = false;
				}
			}
		}

		return tokens;

	}

	public boolean isDirectedGraph() {
		if (hasHeader_DirectedEdges && directedEdgeAttrList.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isUndirectedGraph() {
		if (hasHeader_UndirectedEdges && undirectedEdgeAttrList.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean getValidationResult() {
		return isFileGood;
	}

	public String getErrorMessages() {
		return errorMessages.toString();
	}

	public List getNodeAttrList() {
		return nodeAttrList;
	}

	public List getUndirectedEdgeAttrList() {
		return undirectedEdgeAttrList;
	}

	public List getDirectedEdgeAttrList() {
		return directedEdgeAttrList;
	}

	public int getTotalNumOfNodes() {
		return totalNumOfNodes;
	}

	public boolean getHasTotalNumOfNodes() {
		return hasTotalNumOfNodes;
	}
	
	public int getTotalNumOfUndirectedEdges() {
		return this.countedNumUnDirected;
	}
	
	public int getTotalNumOfDirectedEdges() {
		return this.countedNumDirected;
	}
}
