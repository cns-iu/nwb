package edu.iu.nwb.util.nwbfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	private BufferedReader fileReader;
	private NWBFileParserHandler handler;

	/***** Start adapted guts from ValidateNWBFile. *****/
	private boolean hasHeader_Nodes = false;
	private boolean hasHeader_UndirectedEdges = false;
	private boolean hasHeader_DirectedEdges = false;
	private boolean isFileGood = true;
	private boolean inNodesSection = false;
	private boolean inUndirectededgesSection = false;
	private boolean inDirectededgesSection = false;
	private boolean hasTotalNumOfNodes = false;

	/* If true, it indicates the previous line is a section header.
	 * It is useful to parse the attribute line.
	 */
	private boolean passHeader = false;

	private int totalNumOfNodes;
	private int currentLine;
	private int countedNumDirected; // TODO Use this to validate edge count
	private int countedNumUndirected; // TODO Use this to validate edge count
	private int countedNodes;

	private StringBuffer errorMessages = new StringBuffer();

	private Collection<NWBAttribute> nodeAttributes;
	private Collection<NWBAttribute> directedEdgeAttributes;
	private Collection<NWBAttribute> undirectedEdgeAttributes;
	
	public NWBFileParser(String file) throws IOException {
		this(new File(file));
	}
	
	public NWBFileParser(File file) throws IOException {
		this(new FileInputStream(file));
	}
	
	public NWBFileParser(InputStream input) throws IOException {
		this(new InputStreamReader(input, "UTF-8"));
	}
	
	public NWBFileParser(Reader input) {
		this.fileReader = new BufferedReader(input);
	}
	
	public void parse(NWBFileParserHandler handler) throws ParsingException, IOException {
		this.handler = handler;
		
		try {
			processFile(fileReader);
		} catch (IOException e) {
			throw e;
		}catch (Exception e) {
			throw new ParsingException(e);
		} finally {
			handler.finishedParsing();
		}
		if (this.errorMessages.length() > 0) {
			throw new ParsingException(this.errorMessages.toString());
		}
	}

	private void processFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		
		while ((line != null) && this.isFileGood && !this.handler.haltParsingNow()) {
			this.currentLine++;
			line = line.trim();
			
			if (line.startsWith(NWBFileProperty.PREFIX_COMMENTS)) { 
				this.handler.addComment(line.substring(1));
			} else if (line.length() == 0) {
				// skip empty lines
			} else if (validateNodeHeader(line)) {
			} else if (validateDirectedEdgeHeader(line)) {
			} else if (validateUndirectedEdgeHeader(line)) {
			} else if (this.inNodesSection && this.isFileGood) {
				processNodeLine(line);
			} else if (this.inDirectededgesSection && this.isFileGood) {
				processDirectedEdgeLine(line);
			} else if (this.inUndirectededgesSection && this.isFileGood) {
				processUndirectedEdgeLine(line);
			}
			
			line = reader.readLine();
		}

		if (this.handler.haltParsingNow()) {
			// The handler has said to stop, so we assume no error messages should be thrown.
			this.errorMessages = new StringBuffer();
		} else {
			if (this.isFileGood) {
				checkFile();
			}
			
			/* TODO Validate edge count? */
			
			if (this.hasTotalNumOfNodes && ((this.countedNodes) != this.totalNumOfNodes)){
				// I'm not sure if we should set this to false or not.
				this.isFileGood = false;
				this.errorMessages.append(
					"There was an inconsistency between the specified number of nodes: " +
					this.totalNumOfNodes + " and the " +
					"number of nodes counted: " + this.countedNodes);  
			}
			
			this.totalNumOfNodes = this.countedNodes;
		}

		this.fileReader.close();
	}
	
	
	private void checkFile() {
		if (!this.hasHeader_Nodes) {
			this.isFileGood = false;
			this.errorMessages.append("*The file does not specify the node header.\n\n");
		} else if (!this.hasHeader_DirectedEdges && !this.hasHeader_UndirectedEdges) {
			this.isFileGood = false;
			this.errorMessages.append("This file has not specified a valid edge header.");
		} 		
	}
	
	/**
	 *  validateNodeHeader takes fileReader a string and checks to see if it starts with 
	 *  *nodes with an optional integer following the nodes declaration.
	 */
	private boolean validateNodeHeader(String header) {
		if (header.startsWith(NWBFileProperty.HEADER_NODE)) {
			this.hasHeader_Nodes = true;
			this.inNodesSection = true;
			this.inDirectededgesSection = false;
			this.inUndirectededgesSection = false;
			this.passHeader = true;
			this.nodeAttributes = new ArrayList<NWBAttribute>();

			// Get the total number of nodes.
			StringTokenizer stringTokenizer = new StringTokenizer(header);

			if (stringTokenizer.countTokens() > 1) {
				stringTokenizer.nextToken();
				// *****If it is not an integer...
				this.totalNumOfNodes = new Integer(stringTokenizer.nextToken()).intValue();
				this.hasTotalNumOfNodes = true;
				
				this.handler.setNodeCount(totalNumOfNodes);
			} else {
				this.hasTotalNumOfNodes = false;
			}

			return true;
		} else {
			if (header.equalsIgnoreCase(NWBFileProperty.HEADER_NODE)){
				this.isFileGood = false;
				this.errorMessages.append(
					"The header of the node section fileReader an nwb file should be " +
					NWBFileProperty.HEADER_NODE +
					" and it is case sensitive. The current header is "+header+".\n\n");

				return false;				
			}			
		}

		return false;

	}

	/*
	 * validateDirectedEdgeHeader takes a string corresponding to a line fileReader the current NWB file 
	 * being processed.  The function returns a boolean corresponding to whether the string
	 * is a header for a directed edge.
	 */
	private boolean validateDirectedEdgeHeader(String header) {
		if (header.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) {
			this.hasHeader_DirectedEdges = true;
			this.inDirectededgesSection = true;
			this.inNodesSection = false;
			this.inUndirectededgesSection = false;
			this.passHeader = true;
			this.directedEdgeAttributes = new ArrayList<NWBAttribute>();
			
			// Get the total number of edges, if available.
			StringTokenizer tokenizer = new StringTokenizer(header);
			if (tokenizer.countTokens() > 1) {
				tokenizer.nextToken();
				// *****If it is not an integer...
				int totalEdges = new Integer(tokenizer.nextToken()).intValue();
				this.handler.setDirectedEdgeCount(totalEdges);
			}

			return true;
		} else {
			if (header.equalsIgnoreCase(NWBFileProperty.HEADER_DIRECTED_EDGES)) {
				this.isFileGood = false;
				this.errorMessages.append(
					"The header of the directed edge section should be " +
					NWBFileProperty.HEADER_DIRECTED_EDGES +
					" and it is case sensitive. The current header is " + header + ".\n\n");

				return false;				
			}			
		}

		return false;
	}

	/*
	 * validateUndirectedEdgeHeader will check the input string and return true if that string
	 * refers to a valid undirected edge header.
	 */
	private boolean validateUndirectedEdgeHeader(String header) {
		if (header.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) {
			this.hasHeader_UndirectedEdges = true;
			this.inUndirectededgesSection = true;
			this.inNodesSection = false;
			this.inDirectededgesSection = false;
			this.passHeader = true;
			this.undirectedEdgeAttributes = new ArrayList<NWBAttribute>();
			
			// Get the total number of edges, if available.
			StringTokenizer tokenizer = new StringTokenizer(header);

			if (tokenizer.countTokens() > 1) {
				tokenizer.nextToken();
				// *****If it is not an integer...
				int totalEdges = new Integer(tokenizer.nextToken()).intValue();
				this.handler.setUndirectedEdgeCount(totalEdges);
			}

			return true;
		} else {
			if (header.equalsIgnoreCase(NWBFileProperty.HEADER_UNDIRECTED_EDGES)){
				this.isFileGood = false;
				this.errorMessages.append(
					"The header of the undirected edge section should be " +
					NWBFileProperty.HEADER_UNDIRECTED_EDGES +
					" and it is case sensitive. The current header is "+header+".\n\n");

				return false;				
			}			
		}

		return false;
	}

	/*
	 * 
	 */
	private void processNodeLine(String rawNodeLine) {
		if (this.passHeader) {// if previous line is a node header
			/*
			 * Get attribute line handle only one case: 
			 * id*int label*string attr3*dataType .... 
			 */
			if (rawNodeLine.startsWith(NWBFileProperty.ATTRIBUTE_ID)) {
				// process attribut line
				StringTokenizer st = new StringTokenizer(rawNodeLine);
				int totalTokens = st.countTokens();
				for (int i = 1; i <= totalTokens; i++) {
					// process token
					try {
						NWBAttribute attribute = processAttributeToken(st.nextToken());
						this.nodeAttributes.add(attribute);
					} catch (Exception e) {
						this.isFileGood = false;
						this.errorMessages.append(
							"*Wrong NWB format at line " +
							this.currentLine + ".\n" + e.toString() + "\n\n");

						break;
					}
				}
				
				if (this.isFileGood) {
					LinkedHashMap<String, String> nodeSchema = new LinkedHashMap<String, String>();

					for (NWBAttribute attribute : this.nodeAttributes) {
						nodeSchema.put(attribute.getAttrName(), attribute.getDataType());
					}

					this.handler.setNodeSchema(nodeSchema);
				}
			}
			else {
				this.isFileGood = false;
				this.errorMessages.append(
					"*Wrong NWB format at line " +
					this.currentLine + ". The attribute line is missing.\n\n");
				
			}

			this.passHeader = false;
			
		} else {// process node list
			// based on nodeAttributes to detect each node item fileReader the node list
			// basically, make sure if there's a value, the value belongs to the
			// declared data type. If it is a string, it must be surrounded by double
			// quotations.
			try {
				Map<String, Object> attributes = validateALine(rawNodeLine, this.nodeAttributes);
				int id = ((Integer) attributes.remove(NWBFileProperty.ATTRIBUTE_ID)).intValue();
				String label = (String) attributes.remove(NWBFileProperty.ATTRIBUTE_LABEL);
				this.handler.addNode(id, label, attributes);
				
				this.countedNodes++;
			} catch (Exception e) {
				this.isFileGood = false;
				this.errorMessages.append(
					"*Wrong NWB format at line " +
					this.currentLine + ".\n" + e.toString() + "\n\n");
			}
		}

	}

	private void processDirectedEdgeLine(String rawLineEdge) {
		if (this.passHeader) {// if previous line is an edge header
			/*
			 * get attribute line handle only one case:
			 * source*int target*int attr3*dataType .... 
			 */
			if (rawLineEdge.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)) {
				// process attribut line
				StringTokenizer tokenizer = new StringTokenizer(rawLineEdge);
				int tokens = tokenizer.countTokens();

				for (int ii = 1; ii <= tokens; ii++) {
					String token = tokenizer.nextToken();
					// Process token.
					try {
						NWBAttribute attribute = processAttributeToken(token);
						this.directedEdgeAttributes.add(attribute);
					} catch (Exception e) {
						this.isFileGood = false;
						this.errorMessages.append(
							"*Wrong NWB format at line " +
							this.currentLine + ".\n" + e.toString() + "\n\n");

						break;
					}
				}
				
				if (this.isFileGood) {
					LinkedHashMap<String, String> edgeSchema = new LinkedHashMap<String, String>();

					for (NWBAttribute attribute : this.directedEdgeAttributes) {
						edgeSchema.put(attribute.getAttrName(), attribute.getDataType());
					}

					this.handler.setDirectedEdgeSchema(edgeSchema);
				}
			} else {
					this.isFileGood = false;
					this.errorMessages.append(
						"*Wrong NWB format at line " +
						this.currentLine + ".\n" + "The attribute line is missing.\n\n");
			}

			this.passHeader = false;
		} else {
			try {
				Map<String, Object> attributes = validateALine(rawLineEdge, this.directedEdgeAttributes);
				int source =
					((Integer) attributes.remove(NWBFileProperty.ATTRIBUTE_SOURCE)).intValue();
				int target =
					((Integer) attributes.remove(NWBFileProperty.ATTRIBUTE_TARGET)).intValue();
					
				this.handler.addDirectedEdge(source, target, attributes);
				
				this.countedNumDirected++;
			/* TODO: Make this not catch everything as an error with the NWB format,
			 *  because it should be possible to throw Exceptions fileReader an implementation to
			 *  reflect other issues and handle them yourself.
			 */
			} catch (Exception e) {
				this.isFileGood = false;
				this.errorMessages.append(
					"*Wrong NWB format at line " +
					this.currentLine + ".\n" + e.toString() + "\n\n");
			}
		}
	}

	private void processUndirectedEdgeLine(String rawEdgeLine) {
		// If previous line is an edge header.
		if (this.passHeader) {
			/*
			 * Get attribute line handle one cases: 
			 * source*int target*int attr3*dataType .... 
			 */
			if (rawEdgeLine.startsWith(NWBFileProperty.ATTRIBUTE_SOURCE)) {
				// process attribut line
				StringTokenizer tokenizer = new StringTokenizer(rawEdgeLine);
				int tokens = tokenizer.countTokens();

				for (int ii = 1; ii <= tokens; ii++) {
					try {
						String token = tokenizer.nextToken();
						NWBAttribute attribute = processAttributeToken(token);
						this.undirectedEdgeAttributes.add(attribute);
					} catch (Exception e) {
						this.isFileGood = false;
						this.errorMessages.append(
							"*Wrong NWB format at line " +
							this.currentLine + ".\n" + e.toString() + "\n\n");

						break;
					}
				}
				
				if (this.isFileGood) {
					LinkedHashMap<String, String> edgeSchema = new LinkedHashMap<String, String>();

					for (NWBAttribute attribute : this.undirectedEdgeAttributes) {
						edgeSchema.put(attribute.getAttrName(), attribute.getDataType());
					}

					this.handler.setUndirectedEdgeSchema(edgeSchema);
				}
			} else {
				this.isFileGood = false;
				this.errorMessages.append(
					"*Wrong NWB format at line " + this.currentLine + ".\n" +
					"The attribute line is missing.\n\n");
			}

			this.passHeader = false;			
		} else {
			try {
				Map<String, Object> attributes = validateALine(rawEdgeLine, this.undirectedEdgeAttributes);
				int source =
					((Integer) attributes.remove(NWBFileProperty.ATTRIBUTE_SOURCE)).intValue();
				int target =
					((Integer) attributes.remove(NWBFileProperty.ATTRIBUTE_TARGET)).intValue();

				this.handler.addUndirectedEdge(source, target, attributes);
				this.countedNumUndirected++;

			} catch (Exception e) {
				this.isFileGood = false;
				this.errorMessages.append(
					"*Wrong NWB format at line " +
					this.currentLine + ".\n" + e.toString() + "\n\n");
			}
		}
	}

	private static Map<String, Object> validateALine(
			String line, Collection<NWBAttribute> attributes) throws Exception {
		StringTokenizer stringTokenizer = new StringTokenizer(line);
		int totalTokens = stringTokenizer.countTokens();

		if (totalTokens < attributes.size()) {
			// TODO: Don't throw generic exceptions.
			throw new Exception("Did not specify all values for defined attributes!");
		}

		Map<String, Object> entity = new HashMap<String, Object>();
		String[] columns = processTokens(stringTokenizer);

		int columnIndex = -1;
		for (NWBAttribute nwbAttribute : attributes) {
			columnIndex++;
			String dataType = nwbAttribute.getDataType();

			if (columns[columnIndex].equalsIgnoreCase("*")) {
				// this is a NULL value, skip it.
			} else if (dataType.equalsIgnoreCase(NWBFileProperty.TYPE_STRING)) {
				validateIsAString(columns[columnIndex]);
				columns[columnIndex] = columns[columnIndex].replace('\"', ' ').trim();
				entity.put(nwbAttribute.getAttrName(), columns[columnIndex]);
			} else if (dataType.equalsIgnoreCase(NWBFileProperty.TYPE_INT)) {
				validateIsAnIntegerOrID(columns[columnIndex], nwbAttribute.getAttrName());
				entity.put(nwbAttribute.getAttrName(), new Integer(columns[columnIndex]));
			} else if (dataType.equalsIgnoreCase(NWBFileProperty.TYPE_FLOAT)||
					dataType.equalsIgnoreCase(NWBFileProperty.TYPE_REAL)) {
				validateIsAFloat(columns[columnIndex]);
				entity.put(nwbAttribute.getAttrName(), new Float(columns[columnIndex]));
			}
		}

		return entity;
	}

	private static boolean validateIsAnIntegerOrID(String input, String attribute)
			throws NumberFormatException, Exception {
		Integer value = new Integer(input);
		if (attribute.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_ID) ||
				attribute.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_SOURCE) ||
				attribute.equalsIgnoreCase(NWBFileProperty.ATTRIBUTE_TARGET)) {
			if (value.intValue() < 1) {
				throw new Exception("The node id must be greater than 0.");
			}
		}

		return true;
	}

	private static boolean validateIsAString(String input) throws Exception {
		if (!input.startsWith("\"") || !input.endsWith("\"")) {
			throw new Exception("A string value must be surrounded by double quotation marks.");
		}

		return true;
	}

	private static boolean validateIsAFloat(String input)
			throws NumberFormatException, Exception {
		Float floatValue = new Float(input);
		floatValue.floatValue();

		return true;
	}

	private static NWBAttribute processAttributeToken(String token) throws Exception {
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
								+ " is not acceptable. Only float, int and string are valid data types fileReader the NWB format.\n" +
										"You supplied an attribute of " + attr_type);
			} else
				return new NWBAttribute(attr_name, attr_type);
		} else
			throw new Exception("Can not find * from attribut*datatype line.");
	}

	private static String[] processTokens(StringTokenizer st) {
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
