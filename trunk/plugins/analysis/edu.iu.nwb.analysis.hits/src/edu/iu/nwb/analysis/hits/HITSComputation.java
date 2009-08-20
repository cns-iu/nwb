package edu.iu.nwb.analysis.hits;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

public class HITSComputation  implements NWBFileParserHandler {
	
	private static final double DEFAULT_EDGE_WEIGHT = 1.0; 
	
	private boolean wantDense = false;
	
	private DoubleFactory2D matrixFactory;
	
	public DoubleMatrix2D adjacencyMatrix, adjacencyTransposeMatrix, authorityMatrix, hubMatrix;
	
	public HashMap nodeIDToMatrixIndexMap = new HashMap();
	
	private String edgeWeightColumnName; 

	private int numberOfNodes, numberOfIterations, nodeCountForMatrix;

	private Functions matrixMultiplicationFunctions = Functions.functions;
	
	
	public HITSComputation(int numberOfNodes, int numberOfIterations,
			String edgeWeightColumnName) {
		
		this.nodeCountForMatrix = 0;
		
		this.numberOfNodes = numberOfNodes;
		this.numberOfIterations = numberOfIterations;
		this.edgeWeightColumnName = edgeWeightColumnName; 
		
		initializeHITSComputationMatrices(numberOfNodes);
		
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
	
	/**
	 * Assign values to elements of Adjacency matrix.
	 * @param node1
	 * @param node2
	 * @param attributes
	 * @param isUnDirectedEdge In order to also set matrix element corresponding to [node2, node1] 
	 */
	private void setAdjacencyMatrixElements(int node1, int node2, Map attributes, boolean isUnDirectedEdge) {
		int sourceEdge = (Integer)((((List) nodeIDToMatrixIndexMap.get(node1)).toArray())[0]);
		int destinationEdge = (Integer) (((List) nodeIDToMatrixIndexMap.get(node2)).toArray())[0];
		
		double edgeWeight;
		
		/*
		 * To make sure that null edge weights represented by "*" are set to default edge 
		 * weight of 1.0 
		 * */
		try {
			edgeWeight = Double.parseDouble(attributes.get(edgeWeightColumnName).toString());
		}
		catch (Exception e) {
			//If the weight is null, use the default weight
			edgeWeight = DEFAULT_EDGE_WEIGHT;
		}
		
		adjacencyMatrix.set(sourceEdge, destinationEdge, edgeWeight);
		
		/*
		 * In case of Undirected graph there will a symmetric adjacency matrix.
		 * */
		
		if(isUnDirectedEdge) {
			adjacencyMatrix.set(destinationEdge, sourceEdge, edgeWeight);
		}
	}
	
	public void addComment(String comment) {}
	
	/*
	 * Save the id & label of a node corresponding to matrix index for future reference.
	 * */
	public void addNode(int id, String label, Map attributes) {
		List nodeAttributes = new ArrayList();
		nodeAttributes.add(nodeCountForMatrix);
		nodeAttributes.add(attributes);
		nodeIDToMatrixIndexMap.put(id, nodeAttributes);
		this.nodeCountForMatrix++;
	}

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		setAdjacencyMatrixElements(sourceNode, targetNode, attributes, false);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		setAdjacencyMatrixElements(node1, node2, attributes, true);
	}

	public void finishedParsing() {
		adjacencyTransposeMatrix = adjacencyMatrix.viewDice();
		updateAuthorityHubMatrices(numberOfIterations);
	}

	public boolean haltParsingNow() {
		return false;
	}

	public void setDirectedEdgeCount(int numberOfEdges) {}

	public void setDirectedEdgeSchema(LinkedHashMap schema) {}

	public void setNodeCount(int numberOfNodes) {}

	public void setNodeSchema(LinkedHashMap schema) {}

	public void setUndirectedEdgeCount(int numberOfEdges) {}

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {}

}
