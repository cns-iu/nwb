package edu.iu.nwb.util.nwbfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Callback-based NWB file parser. Internal code adapted from 
 * edu.iu.nwb.convert.nwb.common.ValidateNWBFile.java 
 * 
 * @author Bruce Herr (bh2@bh2.net)
 * @author ValidateNWBFile.java authors
 */
public class NWBFileParser {
	private BufferedReader in;
	private NWBFileParserHandler handler;
	
	public NWBFileParser(String file) throws IOException {
		this(new File(file));
	}
	
	public NWBFileParser(File file) throws IOException {
		this(new FileInputStream(file));
	}
	
	public NWBFileParser(InputStream input) throws IOException {
		in = new BufferedReader(new InputStreamReader(input,"UTF-8"));
	}

	public void parse(NWBFileParserHandler handler) throws ParsingException, IOException {
		this.handler = handler;
		
		try {
			processFile(in);
		} catch (IOException e) {
			throw e;
		}catch (Exception e) {
			throw new ParsingException(e);
		} finally {
			handler.finishedParsing();
		}
		if (errorMessages.length() > 0) {
			throw new ParsingException(errorMessages.toString());
		}
	}
	
	/***** Start adapted guts from ValidateNWBFile. *****/
	private boolean hasHeader_Nodes = false;
	private boolean hasHeader_UndirectedEdges = false;
	private boolean hasHeader_DirectedEdges = false;
	private boolean isFileGood = true;
	private boolean inNodesSection = false;
	private boolean inUndirectededgesSection = false;
	private boolean inDirectededgesSection = false;
	private boolean hasTotalNumOfNodes = false;

	//if true, it indicates the previous line is a section header
	//It is useful to parse the attribute line
	private boolean passHeader = false;

	private int totalNumOfNodes, currentLine, countedNumDirected, countedNumUnDirected, countedNodes;

	private StringBuffer errorMessages = new StringBuffer();

	private List nodeAttrList, directedEdgeAttrList, undirectedEdgeAttrList;

	private void processFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		
		while (line != null && isFileGood && !handler.haltParsingNow()) {
			currentLine++;
			line = line.trim();
			
			if (line.startsWith(NWBFileProperty.PREFIX_COMMENTS)) { 
				handler.addComment(line.substring(1));
			} else if (line.length() == 0) {
			} else if (validateNodeHeader(line)) {
			} else if (validateDirectedEdgeHeader(line)) {
			} else if(validateUndirectedEdgeHeader(line)) {
			} else if (inNodesSection && isFileGood) {
				processNodes(line);
			} else if (inDirectededgesSection && isFileGood) {
				processDirectedEdges(line);
			} else if (inUndirectededgesSection && isFileGood) {
				processUndirectedEdges(line);
			}
			
			line = reader.readLine();
		}

		if (handler.haltParsingNow()) {
			//the Handler has said to stop, so we assume
			//no error messages should be thrown.
			errorMessages = new StringBuffer();
		} else {
			if (isFileGood) {
				checkFile();
			}
			
			if(this.hasTotalNumOfNodes && ((this.countedNodes) != this.totalNumOfNodes)){
				isFileGood = false; //I'm not sure if we should set this to false or not.
				errorMessages.append("There was an inconsistency between the specified number of nodes: " 
						+ this.totalNumOfNodes + " and the " +
						"number of nodes counted: " + this.countedNodes);  
			}
			
			this.totalNumOfNodes = this.countedNodes;
		}
		in.close();
	}
	
	
	private void checkFile() {
		if (!hasHeader_Nodes) {
			isFileGood = false;
			errorMessages
					.append("*The file does not specify the node header.\n\n");
		} else if (!hasHeader_DirectedEdges && !hasHeader_UndirectedEdges) {
			isFileGood = false;
			errorMessages.append("This file has not specified a valid edge header.");
		} 		
	}
	
	/**
	 *  validateNodeHeader takes in a string and checks to see if it starts with 
	 *  *nodes with an optional integer following the nodes declaration.
	 */

	private boolean validateNodeHeader(String s) throws IOException {
		
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
				
				handler.setNodeCount(totalNumOfNodes);
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
				errorMessages
				.append("The header of the node section in an nwb file should be "+
						NWBFileProperty.HEADER_NODE+
						" and it is case sensitive. The current header is "+s+".\n\n");
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
	private boolean validateDirectedEdgeHeader(String s) throws IOException {
		if (s.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) {
			hasHeader_DirectedEdges = true;
			inDirectededgesSection = true;
			inNodesSection = false;
			inUndirectededgesSection = false;
			passHeader = true;
			directedEdgeAttrList = new ArrayList();
			
			//get the total number of edges, if available
			StringTokenizer st = new StringTokenizer(s);
			if (st.countTokens() > 1) {
				st.nextToken();
				//*****If it is not an integer...
				int totalEdges = new Integer(st.nextToken()).intValue();
				handler.setDirectedEdgeCount(totalEdges);
			}
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
	private boolean validateUndirectedEdgeHeader(String s) throws IOException {
		if (s.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) {
			hasHeader_UndirectedEdges = true;
			inUndirectededgesSection = true;
			inNodesSection = false;
			inDirectededgesSection = false;
			passHeader = true;
			undirectedEdgeAttrList = new ArrayList();
			
			//get the total number of edges, if available
			StringTokenizer st = new StringTokenizer(s);
			if (st.countTokens() > 1) {
				st.nextToken();
				//*****If it is not an integer...
				int totalEdges = new Integer(st.nextToken()).intValue();
				handler.setUndirectedEdgeCount(totalEdges);
			}
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
				
				if (isFileGood) {
					LinkedHashMap nodeSchema = new LinkedHashMap();
					for (Iterator attrs = nodeAttrList.iterator(); attrs.hasNext(); ) {
						NWBAttribute attr = (NWBAttribute) attrs.next();
						
						nodeSchema.put(attr.getAttrName(), attr.getDataType());
					}
					handler.setNodeSchema(nodeSchema);
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
				Map attr = validateALine(s, nodeAttrList);
				int id = ((Integer)attr.remove(NWBFileProperty.ATTRIBUTE_ID)).intValue();
				String label = (String) attr.remove(NWBFileProperty.ATTRIBUTE_LABEL);
					
				handler.addNode(id, label, attr);
				
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
				
				if (isFileGood) {
					LinkedHashMap edgeSchema = new LinkedHashMap();
					for (Iterator attrs = directedEdgeAttrList.iterator(); attrs.hasNext(); ) {
						NWBAttribute attr = (NWBAttribute) attrs.next();
						
						edgeSchema.put(attr.getAttrName(), attr.getDataType());
					}
					handler.setDirectedEdgeSchema(edgeSchema);
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
				Map attr = validateALine(s, directedEdgeAttrList);
				int src = ((Integer)attr.remove(NWBFileProperty.ATTRIBUTE_SOURCE)).intValue();
				int target = ((Integer)attr.remove(NWBFileProperty.ATTRIBUTE_TARGET)).intValue();
					
				handler.addDirectedEdge(src, target, attr);
				
				this.countedNumDirected++;
			//TODO: make this not catch everything as an error with the NWB format, because it should be possible to throw Exceptions in an implementation to reflect other issues and handle them yourself.
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
				
				if (isFileGood) {
					LinkedHashMap edgeSchema = new LinkedHashMap();
					for (Iterator attrs = undirectedEdgeAttrList.iterator(); attrs.hasNext(); ) {
						NWBAttribute attr = (NWBAttribute) attrs.next();
						
						edgeSchema.put(attr.getAttrName(), attr.getDataType());
					}
					handler.setUndirectedEdgeSchema(edgeSchema);
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
				Map attr = validateALine(s, undirectedEdgeAttrList);
				int src = ((Integer)attr.remove(NWBFileProperty.ATTRIBUTE_SOURCE)).intValue();
				int target = ((Integer)attr.remove(NWBFileProperty.ATTRIBUTE_TARGET)).intValue();
					
				handler.addUndirectedEdge(src, target, attr);
				
				this.countedNumUnDirected++;

			} catch (Exception e) {
				isFileGood = false;
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}
	}

	private Map validateALine(String line, List attrList) throws Exception {
		StringTokenizer st = new StringTokenizer(line);
		int totalTokens = st.countTokens();
		if (totalTokens < attrList.size())
			//TODO: don't throw generic exceptions
			throw new Exception(
					"Did not specify all values for defined attributes!");
		Map entity = new HashMap();
		String[] columns = processTokens(st);
		for (int i = 0; i < attrList.size(); i++) {
			NWBAttribute nwbAttr = (NWBAttribute) attrList.get(i);
			String dt = nwbAttr.getDataType();
			if(columns[i].equalsIgnoreCase("*")){
			}
			else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_STRING)) {
				isAString(columns[i], nwbAttr.getAttrName());
				
				columns[i] = columns[i].replace('\"', ' ').trim();
				entity.put(nwbAttr.getAttrName(), columns[i]);
			} else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_INT)) {
				isAnInteger(columns[i], nwbAttr.getAttrName());
				entity.put(nwbAttr.getAttrName(), new Integer(columns[i]));
			} else if (dt.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)||
					dt.equalsIgnoreCase(NWBFileProperty.TYPE_REAL)) {
				isAFloat(columns[i], nwbAttr.getAttrName());
				entity.put(nwbAttr.getAttrName(), new Float(columns[i]));
			}
		}
		return entity;
	}

	private boolean isAnInteger(String input, String attr)
			throws NumberFormatException, Exception {
		Integer value = new Integer(input);
		if (attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID)
				|| attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE)
				|| attr.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)) {
			if (value.intValue() < 1)
				throw new Exception("The node id must be greater than 0.");
		}
		return true;
	}

	private boolean isAString(String input, String attr) throws Exception {
		if (!input.startsWith("\"") || !input.endsWith("\"")) {
			throw new Exception(
					"A string value must be surrounded by double quatation marks.");
		}
		return true;
	}

	private boolean isAFloat(String input, String attr)
			throws NumberFormatException, Exception {
		Float f = new Float(input);
		f.floatValue();
		return true;
	}

	private NWBAttribute processAttrToken(String token) throws Exception {
		if (token.indexOf("*") != -1) {
			String attr_name = token.substring(0, token.indexOf("*"));
			if (attr_name.startsWith("//"))
				attr_name = attr_name.substring(2);
			String attr_type = token.substring(token.indexOf("*") + 1);
			if (!(attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)||
				  attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_REAL)||
				  attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_INT) || 
				  attr_type.equalsIgnoreCase(NWBFileProperty.TYPE_STRING))) {
				throw new Exception(
						"The data type of the attribute "
								+ attr_name
								+ " is not acceptable. Only float, int and string are valid data types in the NWB format.\n" +
										"You supplied an attribute of " + attr_type);
			} else
				return new NWBAttribute(attr_name, attr_type);
		} else
			throw new Exception("Can not find * from attribut*datatype line.");
	}

	private String[] processTokens(StringTokenizer st) {
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

				} else if (element.startsWith("\"") && element.endsWith("\"") && !element.equals("\"")) {
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
	
	private static class NWBAttribute {
		private String attributeName;
		private String dataType;
		
		public NWBAttribute (String name, String type){
			this.attributeName = name;
			this.dataType = type;
		}
		
		public String getAttrName(){
			return attributeName;
		}
		
		public String getDataType() {
			return dataType;
		}

	}
}
