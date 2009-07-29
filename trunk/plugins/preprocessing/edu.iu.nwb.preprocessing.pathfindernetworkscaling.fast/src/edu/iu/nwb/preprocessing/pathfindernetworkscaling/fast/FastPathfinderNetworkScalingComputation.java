package edu.iu.nwb.preprocessing.pathfindernetworkscaling.fast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.service.log.LogService;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;


/**
 * After getting the input file following algorithm is implemented. 
 * 1. Initialize matrices for edge weights & minimum distance costs with 
 * dimensions being number of nodes x number of nodes. Initialize their 
 * default values as POSITIVE_INFINITY.
 * 2. When reading through the nodes, map node ids to matrix indexes. 
 * 3. Next, when reading through the edges set weight & distance matrices 
 * with edge weights corresponding to a pair of nodes. Depending upon 
 * users choice of how edge weight should be represented, normalize the 
 * edge weight i.e. If they chose similarity, normalize edge weights to be 
 * dissimilarity based by inverting the edge weight.
 * 3. Now run 3 nested "for" loops with indexes i, j, k such that it returns 
 * the shortest possible path from node i to node j using only vertices 1 
 * through k as intermediate points along the way. Now, given this function, 
 * our goal is to find the shortest path from each node i to each node j using 
 * only nodes 1 through k. 
 * 		a. For this, either the true shortest path only uses nodes in the set 
 * 		(1...k); or there exists some path that goes from node i to node k, 
 * 		then from k to j that is better.
 * 		b. Use Minkowski's distance, r parameter, to calculate the distance 
 * 		between the intermediate nodes.
 * 4. Once the distances for all pairs of nodes are calculated, during generating 
 * the output file, output only those edges whose distance & weight matrix values
 * are equal.
 * 
 * @author cdtank
 */
public class FastPathfinderNetworkScalingComputation  implements NWBFileParserHandler {
	
	private static final double DEFAULT_EDGE_WEIGHT = 1.0; 
	
	private Map nodeIDToMatrixIndex = new HashMap();
	
	private int nodeCount, numberOfNodes;
	private String edgeWeightColumnName; 
	private String edgeWeightType;
	private double rParameter;
	
	private LogService logger;

	private DoubleMatrix2D weightMatrix, distanceMatrix;

	public FastPathfinderNetworkScalingComputation(String edgeWeightColumnName, 
			String edgeWeightType, double rParameter, int numberOfNodes, LogService logger) {
		
		this.nodeCount = 0;
		
		this.edgeWeightType = edgeWeightType;
		this.edgeWeightColumnName = edgeWeightColumnName;
		this.rParameter = rParameter;
		this.numberOfNodes = numberOfNodes;
		this.logger = logger;
		
		/*
		 * Initializing matrices required in this algorithm viz., weight & distance.
		 * */
		initializeFastPathfinderMatrices();
	}


	private void initializeFastPathfinderMatrices() {

		/*
		 * Factory method for setting the type of matrix. Dense or Sparse. 
		 * Weight matrix are generally sparse in nature while Distance matrix
		 * is generally dense in nature & hence the appropriate initialization.
		 * */
		DoubleFactory2D matrixFactory = DoubleFactory2D.sparse; 
		DoubleFactory2D distanceMatrixFactory = DoubleFactory2D.dense;
		
		weightMatrix = matrixFactory.make(numberOfNodes, numberOfNodes);
		distanceMatrix = distanceMatrixFactory.make(numberOfNodes, numberOfNodes);

		/*
		 * Requirement of algorithm so that only those edges that are actually present 
		 * in the network are preprocessed.
		 * */
		weightMatrix.assign(Double.POSITIVE_INFINITY);
		distanceMatrix.assign(Double.POSITIVE_INFINITY);
	}


	/**
	 * Set weight values for the edges. Check for irregularities in input & 
	 * assume default values for edge weights.
	 * @param isUnDirectedEdge In order to also set matrix element corresponding to [node2, node1] 
	 */
	private void setMatrixElements(int node1, 
								   int node2, 
								   Map edgeAttributes, 
								   boolean isUnDirectedEdge) {
		
		double edgeWeight = getEdgeWeight(node1, node2, edgeAttributes);
		
		int matrixRowIndex = ((Integer) nodeIDToMatrixIndex.get(node1)).intValue();
		int matrixColumnIndex = ((Integer) nodeIDToMatrixIndex.get(node2)).intValue();
		
		weightMatrix.set(matrixRowIndex, matrixColumnIndex, edgeWeight);
		distanceMatrix.set(matrixRowIndex, matrixColumnIndex, edgeWeight);
		
		/*
		 * To symmetrize the matrix for undirected networks.
		 * */
		if (isUnDirectedEdge) {
			weightMatrix.set(matrixColumnIndex, matrixRowIndex, edgeWeight);
			distanceMatrix.set(matrixColumnIndex, matrixRowIndex, edgeWeight);
		}
	}


	/**
	 * @param node1
	 * @param node2
	 * @param edgeAttributes
	 * @return
	 */
	private double getEdgeWeight(int node1, int node2, Map edgeAttributes) {
		double edgeWeight;
		/*
		 * To make sure that null edge weights represented by "*" are set to default edge 
		 * weight of 1.0 
		 * */
		try {
			edgeWeight = Double.parseDouble(edgeAttributes.get(edgeWeightColumnName).toString());
		} catch (Exception e) {
			
			/*
			 * If the weight is null, use the default weight
			 * */
			edgeWeight = DEFAULT_EDGE_WEIGHT;
		}
		
		/*
		 * Edge weight cannot be less than or equal to 0. Else assume default edge weight value & 
		 * print a warning.
		 * */
		if (edgeWeight <= 0) {
			edgeWeight = DEFAULT_EDGE_WEIGHT;
			logger.log(LogService.LOG_WARNING, "Edge weight for " + node1 + " - " + node2 
					+ " should be more than 0. Default " 
					+ "value of \"Edge Weight\" (" + DEFAULT_EDGE_WEIGHT + ") used.");
		}
		
		if (edgeWeightType.equalsIgnoreCase(
				FastPathfinderNetworkScalingAlgorithm.WEIGHT_SIMILARITY)) {
			edgeWeight = 1 / edgeWeight;
		}
		return edgeWeight;
	}
	
	public void addNode(int id, String label, Map attributes) {
		nodeIDToMatrixIndex.put(id, nodeCount++);
	} 

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) { 
		setMatrixElements(sourceNode, targetNode, attributes, false);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		setMatrixElements(node1, node2, attributes, true);
	}

	public void finishedParsing() {

		/*
		 * Side affects the distance matrix.
		 * */
		computeMinimumWeightPaths();
	}

	/*
	 * Responsible for finding minimum costs between couple of nodes.     
	 * */
	private void computeMinimumWeightPaths() {
		for (int k = 0; k < numberOfNodes; k++) {
			for (int i = 0; i < numberOfNodes; i++) {
				for (int j = 0; j < numberOfNodes; j++) {
					distanceMatrix.set(i, j, getMinimumCostPathWeight(i, j, k));
				}
			}
		}
	}

	/**
	 * Performs the actual comparisons between the current cost & costs by using
	 * intermediate edges.   
	 * @param i source node 
	 * @param j target node
	 * @param k intermediate node (the node we visit when going from i to j
	 * @return
	 */
	private double getMinimumCostPathWeight(int i, int j, int k) {
		
		/*
		 * To override the default behavior in case of r being infinity. 
		 * */
		if (rParameter < Double.POSITIVE_INFINITY) {
			/*
			 * Mimicking the formula,
			 * d = distance
			 * d_of_i_j = MIN{d_of_i_j), ((d_of_i_k)^r + (d_of_k_j)^r)^(1/r)}
			 * */
		double intermediatePathWeight = Math.pow(
											(
											Math.pow(distanceMatrix.get(i, k), rParameter) 
											+ Math.pow(distanceMatrix.get(k, j), rParameter)
											)
											, 1 / rParameter);
		return Math.min(distanceMatrix.get(i, j), intermediatePathWeight);
		} else {
			/*
			 * Here when anything is raised to 1/infinity it becomes a problem in the Lp space. 
			 * This causes it to reduce to the value maximum of different participating values.
			 * Refer to (@link http://en.wikipedia.org/wiki/Lp_space).
			 * */
			return Math.min(distanceMatrix.get(i, j), 
							Math.max(distanceMatrix.get(i, k), distanceMatrix.get(k, j)));
		}
	}

	public boolean haltParsingNow() {
		return false;
	}

	/**
	 * @return the weight associated with row, column of the weightMatrix
	 */
	public double getWeightMatrixElement(int row, int column) {
		int matrixRowIndex = ((Integer) nodeIDToMatrixIndex.get(row)).intValue();
		int matrixColumnIndex = ((Integer) nodeIDToMatrixIndex.get(column)).intValue();
		return weightMatrix.get(matrixRowIndex, matrixColumnIndex);
	}

	/**
	 * @return the weight associated with row, column of the distanceMatrix
	 */
	public double getDistanceMatrixElement(int row, int column) {
		int matrixRowIndex = ((Integer) nodeIDToMatrixIndex.get(row)).intValue();
		int matrixColumnIndex = ((Integer) nodeIDToMatrixIndex.get(column)).intValue();
		return distanceMatrix.get(matrixRowIndex, matrixColumnIndex);
	}
	
	public void addComment(String comment) { }
	
	public void setDirectedEdgeCount(int numberOfEdges) { }

	public void setDirectedEdgeSchema(LinkedHashMap schema) { }

	public void setNodeCount(int numberOfNodes) { }

	public void setNodeSchema(LinkedHashMap schema) { }

	public void setUndirectedEdgeCount(int numberOfEdges) { }

	public void setUndirectedEdgeSchema(LinkedHashMap schema) { }

}
