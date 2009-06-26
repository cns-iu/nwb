package edu.iu.nwb.analysis.hits;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class HITSComputation {
	
	private static final double DEFAULT_EDGE_WEIGHT = 1.0; 
	
	private static final int DEFAULT_EDGE_SOURCE_INDEX = 0;

	private static final int DEFAULT_EDGE_TARGET_INDEX = 1;
	
	private int EDGE_WEIGHT_COLUMN_INDEX = -1;
	
	private boolean hasHeader_Nodes = false;

	private boolean hasHeader_UndirectedEdges = false;

	private boolean hasHeader_DirectedEdges = false;

	private boolean inNodesSection = false;

	private boolean inUndirectededgesSection = false;

	private boolean inDirectededgesSection = false;

	private boolean hasTotalNumOfNodes = false;
	
	private boolean wantDense = false;
	
	private DoubleFactory2D matrixFactory;
	
	private DoubleMatrix2D adjacencyMatrix, adjacencyTransposeMatrix, authorityMatrix, hubMatrix;
	
	private HashMap nodeIDToMatrixIndexMap = new HashMap();
	
	private String edgeWeightColumnName; 

	//if true, it indicates the previous line is a section header
	//It is useful to parse the attribute line
	private boolean passHeader = false;

	private int totalNumOfNodes, currentLine, countedNumDirected, countedNumUnDirected, countedNodes;

	private StringBuffer errorMessages = new StringBuffer();

	private List nodeAttrList, directedEdgeAttrList, undirectedEdgeAttrList;
	
	private Functions matrixMultiplicationFunctions = Functions.functions;
	
	
	/*
	 * performHITSComputation invokes processFile with a BufferedReader corresponding
	 * to the file that we will process for HITS computation.
	 */
	public Data performHITSComputation(File fileHandler, int totalNumberOfNodes, int numberOfIterations, String edgeWeightColumnName)
			throws FileNotFoundException, IOException, ParsingException {
		currentLine = 0;
		countedNumDirected = 0;
		countedNumUnDirected = 0;
		countedNodes = 0;
		BufferedReader reader = new BufferedReader(
								 new InputStreamReader(
								  new FileInputStream(fileHandler),"UTF-8"));
		
		this.edgeWeightColumnName = edgeWeightColumnName; 
		
		initializeHITSComputationMatrices(totalNumberOfNodes);
		
		processFile(reader);
		
		adjacencyTransposeMatrix = adjacencyMatrix.viewDice();
		
		updateAuthorityHubMatrices(numberOfIterations);
		
		return generateOutputFile(fileHandler);
		
	}

	/**
	 * Used to generate the output file containing the modified graph information. 2 new 
	 * attributes - authority & hub score are added for each node.
	 * @param fileHandler
	 * @return Metadata to be displayed.
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Data generateOutputFile(File fileHandler) throws IOException,
			ParsingException {
		File outputNWBFile = File.createTempFile("nwb-", ".nwb");
		
		NWBFileParser parser = new NWBFileParser(fileHandler);
		parser.parse(new HITSAlgorithmOutputGenerator(nodeIDToMatrixIndexMap, authorityMatrix, hubMatrix, outputNWBFile));
		
		Data outNWBData = new BasicData(outputNWBFile,"file:text/nwb");

		return outNWBData;
	}

	/**
	 * Computes the Hub and Authority scores for all the nodes in the Web Graph.
	 * 
	 * Given a Web graph, an iterative calculation is performed on the value of 
	 * authority and value of hub. For each page p, the authority value of page p is 
	 * the sum of hub scores of all the pages that points to p, the hub value of 
	 * page p is the sum of authority scores of all the pages that p points to.
	 * This is done via Matrix manipulation between adjacencyMatrix, adjacencyTranposeMatrix,
	 * authorityMatrix & hubMatrix.
	 *
	 * @param numberOfIterations Number of Times the Authority & Hub Values should be updated 
	 * so that they converge.
	 * 
	 */
	private void updateAuthorityHubMatrices(int numberOfIterations) {
		
		Double authoritySum, hubSum = 0.0;
		
		while(numberOfIterations-- > 0) {

			/*
			 * Update Authority Matrix i.e. authorityMatrix = adjacencyTransposeMatrix * hubMatrix
			 * */
			adjacencyTransposeMatrix.zMult(hubMatrix, authorityMatrix);
			
			/*
			 * Update Hub Matrix i.e. hubMatrix = adjacencyTransposeMatrix * authorityMatrix
			 * */
			adjacencyMatrix.zMult(authorityMatrix, hubMatrix);
			
			/*
			 * Normalize Authority Matrix by dividing each element by the sum 
			 * of all authority scores of all the nodes.
			 * */
			authoritySum = authorityMatrix.zSum();
			authorityMatrix.assign(matrixMultiplicationFunctions.div(authoritySum));
			
			/*
			 * Normalize Hub Matrix by dividing each element by the sum 
			 * of all hub scores of all the nodes.
			 * */
			hubSum = hubMatrix.zSum();
			hubMatrix.assign(matrixMultiplicationFunctions.div(hubSum));
			
		}
		
	}

	/**
	 * This method initializes the matrices required for the computations like
	 * Adjacency, Authority & Hub Matrices.   
	 * @param adjacencyMatrixDimension provides the dimensions for the matrices.
	 */
	private void initializeHITSComputationMatrices(int adjacencyMatrixDimension) {
		
		/*
		 * Factory method for setting the type of matrix. Dense or Sparse. By default
		 * it is sparse but can be modified.
		 * */
		if (wantDense) { 
			matrixFactory = DoubleFactory2D.dense; 
		}
		else {
			matrixFactory = DoubleFactory2D.sparse;
		}
		
		adjacencyMatrix = matrixFactory.make(adjacencyMatrixDimension, adjacencyMatrixDimension);
		authorityMatrix = matrixFactory.make(adjacencyMatrixDimension, 1);
		hubMatrix = matrixFactory.make(adjacencyMatrixDimension, 1);
		
		/*
		 * Setting the default value for authority & hub matrix to 1.0.
		 * */
		authorityMatrix.assign(1.0);
		hubMatrix.assign(1.0);
		
	}

	private void processFile(BufferedReader reader) throws IOException {

		String line = reader.readLine();
		
		while (line != null ) {
			currentLine++;
			line = line.trim();
			if (line.startsWith(NWBFileProperty.PREFIX_COMMENTS) ||line.length()<=0){
				line = reader.readLine();
				continue;				
			}			
			// process section header that looks like
			// *Nodes or *Nodes 1000
			if (validateNodeHeader(line) ){
				line = reader.readLine();
				continue;
			}
			if (validateDirectedEdgeHeader(line)){
				line = reader.readLine();
				continue;
			}
			if(validateUndirectedEdgeHeader(line)) {
				line = reader.readLine();
				continue;
			}

			if (inNodesSection ) {
				processNodes(line);
				//nodes++;
				line = reader.readLine();
				continue;
			}

			if (inDirectededgesSection ) {
				processDirectedEdges(line);
				//dedges++;
				line = reader.readLine();
				continue;
			}

			if (inUndirectededgesSection ) {
				processUndirectedEdges(line);
				line = reader.readLine();
				continue;
			}
			line = reader.readLine();
		}

		this.totalNumOfNodes = this.countedNodes;
		
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
			} else {
				hasTotalNumOfNodes = false;
			}
			return true;

		}

		return false;

	}

	/**
	 * validateDirectedEdgeHeader takes a string corresponding to a line in the current NWB file 
	 * being processed.  The function returns a boolean corresponding to whether the string
	 * is a header for a directed edge.
	 * @param s
	 * @return
	 * @throws IOException
	 */
	private boolean validateDirectedEdgeHeader(String s) throws IOException {
		if (s.startsWith(NWBFileProperty.HEADER_DIRECTED_EDGES)) {
			hasHeader_DirectedEdges = true;
			inDirectededgesSection = true;
			inNodesSection = false;
			inUndirectededgesSection = false;
			passHeader = true;
			directedEdgeAttrList = new ArrayList();
			return true;
		}

		return false;
	}

	/**
	 * validateUndirectedEdgeHeader will check the input string and return true if that string
	 * refers to a valid undirected edge header.
	 * @param s
	 * @return
	 * @throws IOException
	 */
	private boolean validateUndirectedEdgeHeader(String s) throws IOException {
		if (s.startsWith(NWBFileProperty.HEADER_UNDIRECTED_EDGES)) {
			hasHeader_UndirectedEdges = true;
			inUndirectededgesSection = true;
			inNodesSection = false;
			inDirectededgesSection = false;
			passHeader = true;
			undirectedEdgeAttrList = new ArrayList();
			return true;
		}

		return false;
	}

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
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString()+"\n\n");
						break;
					}

				}
			}
			else {
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
				processLine(s, nodeAttrList);
				this.countedNodes++;
			} catch (Exception e) {
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
						initializeEdgeWeightColumnIndex(i, attr);
						directedEdgeAttrList.add(attr);
					} catch (Exception e) {
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString() + "\n\n");
						break;
					}
				}			
			}
			else {
					errorMessages
							.append("*Wrong NWB format at line "
									+ currentLine
									+ ".\n"
									+ "The attribute line is missing.\n\n");
			}
			passHeader = false;
			
		} else {
			try {
				processLine(s, directedEdgeAttrList);
				this.countedNumDirected++;
			} catch (Exception e) {
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}
	}

	/**
	 * It initializes Edge Weight Column Index so that appropriate values are set in the adjacency matrix.
	 * @param i
	 * @param attr
	 */
	private void initializeEdgeWeightColumnIndex(int i, NWBAttribute attr) {
		if(EDGE_WEIGHT_COLUMN_INDEX == -1 && attr.getAttrName().equalsIgnoreCase(edgeWeightColumnName)) {
			EDGE_WEIGHT_COLUMN_INDEX = i - 1; 
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
						initializeEdgeWeightColumnIndex(i, attr);
						undirectedEdgeAttrList.add(attr);
					} catch (Exception e) {
						errorMessages.append("*Wrong NWB format at line "
								+ currentLine + ".\n" + e.toString() + "\n\n");
						break;
					}

				}				
			}
			else {
					errorMessages
							.append("*Wrong NWB format at line "
									+ currentLine
									+ ".\n"
									+ "The attribute line is missing.\n\n");
			}
			passHeader = false;			
		} else {
			try {
				processLine(s, undirectedEdgeAttrList);
				this.countedNumUnDirected++;

			} catch (Exception e) {
				errorMessages.append("*Wrong NWB format at line " + currentLine
						+ ".\n" + e.toString() + "\n\n");
			}
		}
	}

	/**
	 * It processes each line and extracts tokens, then performs appropriate action depending 
	 * upon the type of token.
	 * @param line
	 * @param attrList
	 * @return
	 * @throws Exception
	 */
	private boolean processLine(String line, List attrList) throws Exception {
		if (line.length() <= 0 || line.startsWith(NWBFileProperty.PREFIX_COMMENTS))
			return true;
		StringTokenizer st = new StringTokenizer(line);
		int totalTokens = st.countTokens();
		if (totalTokens <= 0)
			return true;
		if (totalTokens < attrList.size())
			throw new Exception(
					"Did not specify all values for defined attributes!");
		String[] columns = processTokens(st);
		
		/*
		 * Save the id & label of a node corresponding to matrix index for future reference.
		 * */
		if(inNodesSection) {
			List nodeAttributes = new ArrayList();
			nodeAttributes.add(countedNodes);
			nodeAttributes.add(columns[1]);
			nodeIDToMatrixIndexMap.put(columns[0], nodeAttributes);
		}
		
		if(inDirectededgesSection || inUndirectededgesSection) {
			
			int sourceEdge = (Integer)((((List) nodeIDToMatrixIndexMap.get(columns[DEFAULT_EDGE_SOURCE_INDEX])).toArray())[0]);
			int destinationEdge = (Integer) (((List) nodeIDToMatrixIndexMap.get(columns[DEFAULT_EDGE_TARGET_INDEX])).toArray())[0];
			
			double edgeWeight;

			/*
			 * To make sure that null edge weights represented by "*" are set to default edge 
			 * weight of 1.0 
			 * */
			try {
				edgeWeight = Double.parseDouble(columns[EDGE_WEIGHT_COLUMN_INDEX]);
			}
			catch (Exception e) {
				//If the weight is null, use the default weight
				edgeWeight = DEFAULT_EDGE_WEIGHT;
			}
			
			adjacencyMatrix.set(sourceEdge, destinationEdge, edgeWeight);
			
			/*
			 * In case of Undirected graph there will a symmetric adjacency matrix.
			 * */
			if(inUndirectededgesSection) {
				adjacencyMatrix.set(destinationEdge, sourceEdge, edgeWeight);	
			}
			
		}
		
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

	public boolean isDirectedGraph() {
		if (hasHeader_DirectedEdges && directedEdgeAttrList.size() > 0)
			return true;
		else
			return false;
	}

	public boolean isUndirectedGraph() {
		if (hasHeader_UndirectedEdges && undirectedEdgeAttrList.size() > 0)
			return true;
		else
			return false;
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
	
	public int getTotalNumOfUndirectedEdges(){
		return this.countedNumUnDirected;
	}
	
	public int getTotalNumOfDirectedEdges(){
		return this.countedNumDirected;
	}

}
