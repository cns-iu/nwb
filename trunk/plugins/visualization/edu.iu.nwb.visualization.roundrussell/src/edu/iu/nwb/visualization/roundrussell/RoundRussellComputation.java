package edu.iu.nwb.visualization.roundrussell;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.osgi.service.log.LogService;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.visualization.roundrussell.interpolation.ColorInterpolator;
import edu.iu.nwb.visualization.roundrussell.interpolation.Interpolator;
import edu.iu.nwb.visualization.roundrussell.interpolation.InputRangeException;
import edu.iu.nwb.visualization.roundrussell.utility.Range;

public class RoundRussellComputation  implements NWBFileParserHandler {
	
	private static final Color DEFAULT_NODE_COLOR = new Color(203, 203, 203);
	private static final double RGB_NORMALIZING_VALUE = 255.0;
	private static final int IN_COMMUNITY_BOUND = 4;
	private static final double DEFAULT_EDGE_WEIGHT = 1.0; 
	private static final double DEFAULT_NODE_STRENGTH = 1.0;
	private static final double DEFAULT_NODE_COLOR_VALUE = 1.0;
	
	public static final double CANVAS_RADIUS = 800.0;
	public Interpolator<Color> nodeColorInterpolator = null;
	
	public StringBuilder psFileContent = new StringBuilder();
	
	private String nodeStrengthColumnName, nodeColorColumnName, edgeWeightColumnName;

	private List<String> levelColumnNames;
	private Map<Integer, List> nodesMap = new HashMap<Integer, List>();
	private Map<Integer, List<Double>> nodePositionsMap = new HashMap<Integer, List<Double>>(); 
	private List<Double> nodeColorValues = new ArrayList<Double>();

	private List<List<Number>> edgesMappingsList = new ArrayList<List<Number>>();
	private Map<Integer, Double> angles = new HashMap<Integer, Double>();
	private Map<List, List> levelsPositionMap = new HashMap<List, List>();
	private List edgeControlPositionValues = new ArrayList();
	private double edgeBundlingDegree;

	
	/**
	 * List of levels of the hierarchy, where each level is a map from node ID to the ID for 
	 * the community it belongs to on that level.
	 */
	private List<Map<Integer, String>> hierarchy = new ArrayList<Map<Integer, String>>();
	private Range<Color> nodeColorRange;
	private LogService logger;
	private boolean useStrength = true;
	private boolean useNodeColor = true;
	
	
	/*
	 * The algorithm aims to create a circular visualization when provided with hierarchy
	 * information for each node. Different communities information can also be provided 
	 * using the levels. The algorithm is as follows,
	 * 1. Collect information about the nodes including their ids, labels, strengths & 
	 * 	  color attribute values.
	 * 2. Then strengths of the nodes are normalized.
	 * 3. Edge information is collected next.
	 * 4. Hierarchy information is collected from the appropriate columns in the nwb file. 
	 *    Appropriate mapping of Node ID to Level ID are collected.
	 * 5. After this angles for each node are calculated using the nodes & level information.
	 * 6. The calculated angles are then converted into Cartesian system.
	 * 7. These node positions are then reconfigured/normalized based on the radius of the proposed
	 * circular visualization.
	 * 8. Next level angles are computed followed by its position.
	 * 9. Next computations are performed on edges. After normalizing the edge weights, the edge 
	 *    control points are computed. To do this, the ancestor hierarchy is computed & then 
	 *    normalized. 
	 * 10. Afterwards the edges are sorted so that in-community edges are drawn first. 
	 * 11. Next edges are sorted from thickest to thinnest & then drawn in that order.
	 * 12. Next dividers - separating different communities are calculated for their angles, 
	 * position.
	 * 13. Color values in the form of RGB are computed for each node. If no color information
	 * 	   is provided then the default color of Grey (203, 203, 203) is assumed.
	 * All this data is then output into a ps file in appropriate format.
	 * */
	public RoundRussellComputation(int numberOfNodes, 
								   boolean useStrength,
								   String nodeStrengthColumnName, 
								   List<String> levelColumnNames,
								   String edgeWeightColumnName, 
								   boolean useNodeColor, 
								   String nodeColorColumnName,
								   Range<Color> nodeColorRange,
								   double betaCurvedValue, 
								   LogService logger) {
		this.useStrength = useStrength;
		this.nodeStrengthColumnName = nodeStrengthColumnName;
		this.levelColumnNames = levelColumnNames;
		this.edgeBundlingDegree = betaCurvedValue;
		this.edgeWeightColumnName = edgeWeightColumnName;
		this.useNodeColor = useNodeColor;
		this.nodeColorColumnName = nodeColorColumnName;
		this.nodeColorRange = nodeColorRange;
		this.logger = logger;
		
		/*
		 * Initializing hierarchy. Initialize for only those levels whose level 
		 * names are not the default no level column name "no_level_column".
		 * */
		for (int ii = 0; ii < levelColumnNames.size(); ii++) {
			if (!levelColumnNames.get(ii).equalsIgnoreCase(
					RoundRussellAlgorithm.NO_LEVEL_COLUMN_NAME)) {
				hierarchy.add(new HashMap<Integer, String>());
			}
		}
	}

	/**
	 * Assign values to elements of Edges list.
	 * @param node1
	 * @param node2
	 * @param attributes
	 * @param isUndirectedEdge
	 */
	private void createEdge(int node1, int node2, Map attributes, boolean isUndirectedEdge) {
		
		List<Number> currentEdgeAttributes = new ArrayList<Number>(); 
		double edgeWeight;
		
		currentEdgeAttributes.add(node1);
		currentEdgeAttributes.add(node2);
		
		/*
		 * To make sure that null edge weights represented by "*" are set to default edge 
		 * weight of 1.0 
		 * */
		try {
			edgeWeight = Double.parseDouble(attributes.get(edgeWeightColumnName).toString());
		} catch (Exception e) {
			//If the weight is null, use the default weight
			edgeWeight = DEFAULT_EDGE_WEIGHT;
		}
		
		currentEdgeAttributes.add(edgeWeight);
		
		edgesMappingsList.add(currentEdgeAttributes);
		
		/*
		 * In case of Undirected graph save required business logic here.
		 * 
		 * 	if(isUndirectedEdge) {
		 *  }
		 * */
	}
	
	public void addComment(String comment) { }
	
	/* Used to process each node and collect the id, label, strength & level 
	 * information. 
	 * @see edu.iu.nwb.util.nwbfile.NWBFileParserHandler#addNode(int, 
	 * 															 java.lang.String, 
	 * 															 java.util.Map)
	 */
	public void addNode(int id, String label, Map attributes) {

		List nodeAttributes = new ArrayList();
		nodeAttributes.add(label);
		
		/*
		 * In case of no node strength column being specified set the node strength to the 
		 * default value for each node.
		 * */
		if (useStrength) {
			try {
				double currentNodeStrength = Double.parseDouble(
						attributes.get(nodeStrengthColumnName).toString()); 
				nodeAttributes.add(currentNodeStrength);
			} catch (NumberFormatException e) {
				nodeAttributes.add(DEFAULT_NODE_STRENGTH);
			}
		} else {
			nodeAttributes.add(DEFAULT_NODE_STRENGTH);
		}
		
		/*
		 * In case of no node color column being specified set the node color value to the 
		 * default value for each node.
		 * */
		if (useNodeColor) {
			try {
				double currentNodeColorValue = Double.parseDouble(
						attributes.get(nodeColorColumnName).toString()); 
				nodeAttributes.add(currentNodeColorValue);
				nodeColorValues.add(currentNodeColorValue);
			} catch (NumberFormatException e) {
				nodeAttributes.add(DEFAULT_NODE_COLOR_VALUE);
				nodeColorValues.add(DEFAULT_NODE_COLOR_VALUE);
			}
		} else {
			nodeAttributes.add(DEFAULT_NODE_COLOR_VALUE);
			nodeColorValues.add(DEFAULT_NODE_COLOR_VALUE);
		}
		
		
		/*
		 * Generating hierarchy information from the levels provided by the user.
		 * */
		for (int ii = 0; ii < hierarchy.size(); ii++) {
			String levelValue = attributes.get(levelColumnNames.get(ii)).toString();
			hierarchy.get(ii).put(id, levelValue);
		}
		nodesMap.put(id, nodeAttributes);		
	}

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		createEdge(sourceNode, targetNode, attributes, false);
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		createEdge(node1, node2, attributes, true);
	}

	/*
	 * After all the data structures containing information for hierarchy, nodes, strengths,
	 * weights & edge mappings are created, this is processed to calculate output information
	 * in terms of co-ordinates, weights, strengths for each node & edge. In the end it sets 
	 * up the variable content which must be inserted into the output ps file.
	 * */
	public void finishedParsing() {
		
		normalizeNodesStrength();
		
		/*
		 * Used to calculate the angle (in degrees) for each node based on the hierarchy. 
		 * */
		calculateAngles();
		
		/* Used to calculate the node positions in Cartesian system based on the angles.
		 * */
		calculateNodePositions();
		
		/*
		 * Used to calculate positions for each level in Cartesian system. For this it first 
		 * calculates the angles for each level which is provided by the "calculateLevelAngles"
		 * function.
		 * */
		calculateLevelPositions(calculateLevelAngles(), hierarchy.size());
		
		/*
		 * Used to calculate the edge control points. It does so by first finding the
		 * ancestor hierarchy & then it finds the positions of the points in the 
		 * hierarchy.
		 * */
		calculateEdgeControlPoints();
		
		/*
		 * Used to sort the control points in descending manner.
		 * */
		Collections.sort(edgeControlPositionValues, new Comparator() {
			public int compare(Object o1, Object o2) {
				double edgeWeight1 = 1 - Double.parseDouble(((List) o1).get(0).toString());
				double edgeWeight2 = 1 - Double.parseDouble(((List) o2).get(0).toString());
				return Double.compare(edgeWeight1, edgeWeight2);
			}
		});


		/*
		 * Used to normalize edge weights.
		 * */
		edgeControlPositionValues = normalizeEdgeControlPositionWeights();
		/*
		 * This is done to sort the edges so that in community edges are show first and then 
		 * rest of the edges are sorted from thick to thin edge.
		 * */
		Collections.sort(edgeControlPositionValues, new Comparator() {

			public int compare(Object o1, Object o2) {
				
				int numberOfControlPoints1 = ((List) ((List) o1).get(1)).size();
				int numberOfControlPoints2 = ((List) ((List) o2).get(1)).size();
				
				double edgeWeight1 = 1 - Double.parseDouble(((List) o1).get(0).toString());
				double edgeWeight2 = 1 - Double.parseDouble(((List) o2).get(0).toString());
				
//				all in-community edges first, but then sort by reversed weight
				if (numberOfControlPoints1 >= IN_COMMUNITY_BOUND 
						&& numberOfControlPoints2 < IN_COMMUNITY_BOUND) {
					return 1;
				} else if (numberOfControlPoints1 < IN_COMMUNITY_BOUND 
						&& numberOfControlPoints2 >= IN_COMMUNITY_BOUND) {
					return -1;
				} else if (numberOfControlPoints1 < IN_COMMUNITY_BOUND 
						&& numberOfControlPoints2 < IN_COMMUNITY_BOUND) {
					return Double.compare(edgeWeight1, edgeWeight2);
				} else if (numberOfControlPoints1 >= IN_COMMUNITY_BOUND 
						&& numberOfControlPoints2 >= IN_COMMUNITY_BOUND) {
					return Double.compare(edgeWeight1, edgeWeight2);
				}
				return 0;
			}
		});
		
		generateOutputFileContent();
		
	}

	/**
	 * Generate output PS file content from the information processed 
	 * regarding nodes, edges, angles, hierarchies, levels, dividers.
	 */
	private void generateOutputFileContent() {
		
		Color nodeColor = DEFAULT_NODE_COLOR;
		psFileContent.append("/radius " + CANVAS_RADIUS + " def\n");
		
		boolean isNodeColoringPossible = false;
		
		if (!nodeColorColumnName.equalsIgnoreCase(
				RoundRussellAlgorithmFactory.NO_COLOR_IDENTIFIER)) {
			try {
				nodeColorInterpolator = 
					new ColorInterpolator(nodeColorValues, nodeColorRange);
				isNodeColoringPossible = true;
			} catch (InputRangeException e) {
				logger.log(LogService.LOG_ERROR, "Node color attribute values " 
						+ "have to be sufficiently distinct. In the current sample" 
						+ " max & min for node color attribute is the same. Default " 
						+ "node color assumed." + e.getMessage(), e);
			}
		}
		
		/*
		 * For inserting node related information in the ps file. syntax for it is so,
		 * (<nodeLabel>) <nodeStrength> <xPosition> <yPosition> node 
		 * */
		for (Iterator positionMapIterator = nodePositionsMap.entrySet().iterator(); 
				positionMapIterator.hasNext();) {
		
			Map.Entry positionMapEntry = (Map.Entry) positionMapIterator.next();
			
			String nodeLabel = nodesMap.get(positionMapEntry.getKey()).get(0).toString();
			String nodeStrength = nodesMap.get(positionMapEntry.getKey()).get(1).toString();
			double nodecolorValue =  Double.parseDouble(
					nodesMap.get(positionMapEntry.getKey()).get(2).toString());

			String xPosition = ((List) positionMapEntry.getValue()).get(0).toString();
			String yPosition = ((List) positionMapEntry.getValue()).get(1).toString();
			
			if (!RoundRussellAlgorithmFactory.NO_COLOR_IDENTIFIER.
					equalsIgnoreCase(nodeColorColumnName) 
					&& isNodeColoringPossible) {
					nodeColor = nodeColorInterpolator.interpolate(nodecolorValue);
			}
			
			/*
			 * Assign node color value in the postscript content followed by label, 
			 * strength, x & y position..
			 * */
			psFileContent.append(
				(nodeColor.getRed() / RGB_NORMALIZING_VALUE)
				+ " "
				+ (nodeColor.getGreen() / RGB_NORMALIZING_VALUE)
				+ " "
				+ (nodeColor.getBlue() / RGB_NORMALIZING_VALUE)
				+ " "
				+ "(" + nodeLabel + ")"
				+ " " 
				+ nodeStrength
				+ " " 
				+ xPosition
				+ " " 
				+ yPosition 
				+ " node\n");
		
		}
			
		/*
		 * For inserting edge related information in the ps file. syntax for it is so,
		 * [<space delimited point position information>] <edgeWeight> edge
		 * */
		for (Iterator normalizedEdgesControlPositionsIterator = 
				edgeControlPositionValues.iterator(); 
			normalizedEdgesControlPositionsIterator.hasNext();) {
			StringBuilder arrayPoints = new StringBuilder();
			List currentEdge = ((List) normalizedEdgesControlPositionsIterator.next());
			
			for (Iterator controlPointsIterator = ((List) currentEdge.get(1)).iterator(); 
				 controlPointsIterator.hasNext();) {
				List currentEdgePositions = ((List) controlPointsIterator.next()); 
				arrayPoints.append(currentEdgePositions.get(0) 
					+ " " 
					+ currentEdgePositions.get(1) 
					+ " ");
			}
			psFileContent.append("[ " 
					+ arrayPoints + " ] " 
					+ currentEdge.get(0).toString() 
					+ " edge\n");
			
		}
		
		/*
		 * For inserting dividers for each level information in the ps file. syntax for it is so,
		 * [<space delimited divider position information>] <level number> level
		 * */
		for (int level = 0; level < hierarchy.size(); level++) {
			List<Double> dividers = calculateDividers((Map<Integer, String>) hierarchy.get(level));
			psFileContent.append("[ ");
			for (Iterator dividersIterator = dividers.iterator(); dividersIterator.hasNext();) {
				psFileContent.append(dividersIterator.next().toString() + " ");
			}
			psFileContent.append("] " + level + " level\n");
		}
		
		psFileContent.append("grestore\n");
	}

	/*
	 * The dividers are placed between two separating levels, communities. This is used to
	 * calculate the size & the angle  for each divider. 
	 * */
	private List<Double> calculateDividers(Map<Integer, String> levelMap) {
		
		List<Double> calculatedDividers = new ArrayList<Double>();
		double newAngle; 
		
		int previousNode, currentNode;
		SortedSet<Integer> sortedNodesByAngles = new TreeSet<Integer>(new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return angles.get(o1).compareTo(angles.get(o2));
			}
		});
		
		sortedNodesByAngles.addAll(angles.keySet());
		
		List<Integer> sortedNodesByAnglesList = new ArrayList<Integer>();
		sortedNodesByAnglesList.addAll(sortedNodesByAngles);
		sortedNodesByAnglesList.add(sortedNodesByAngles.first());
		
		previousNode = sortedNodesByAnglesList.get(0).intValue();
			
			for (Iterator iterator = sortedNodesByAnglesList.iterator(); iterator.hasNext();) {
				
				/*
				 * we've already set the first 'previousAngle'
				 * */
				currentNode = Integer.parseInt(iterator.next().toString());
				if (!levelMap.get(currentNode).equals(levelMap.get(previousNode))) {
					newAngle = calculateBetweenAngle(angles.get(previousNode), 
													 angles.get(currentNode));
					calculatedDividers.add(newAngle);
				}
				previousNode = currentNode;
			}
		return calculatedDividers;
	}


	/*
	 * Generates Normalized Edge Control Position Weights list.
	 * */
	private List<List> normalizeEdgeControlPositionWeights() {
		
		double maxWeight, minWeight, normalizedMaxWeight, normalizedMinWeight, 
			currentEdgeWeight, currentEdgeNormalizedWeight; 
		
		List edgeWeightValues = new ArrayList();
		
		List<List> normalizedEdgeControlPositionWeights = new ArrayList<List>();
		
		
		for (Iterator edgeWeightValuesIterator = edgeControlPositionValues.iterator(); 
			 edgeWeightValuesIterator.hasNext();) {
			edgeWeightValues.add(((List) edgeWeightValuesIterator.next()).get(0));
		}
		
		maxWeight = Double.parseDouble(Collections.max(edgeWeightValues).toString());
		minWeight = Double.parseDouble(Collections.min(edgeWeightValues).toString());
		
		normalizedMaxWeight = Math.log(maxWeight + 1.0);
		normalizedMinWeight = Math.log(minWeight + 1.0);
		
		for (Iterator updateNormalizedEdgeWeightValuesIterator = 
				edgeControlPositionValues.iterator(); 
			updateNormalizedEdgeWeightValuesIterator.hasNext();) {
		
			List currentEdgeValues = (List) updateNormalizedEdgeWeightValuesIterator.next();
			
			currentEdgeNormalizedWeight = getCurrentEdgeNormalizedWeight(
						normalizedMaxWeight, normalizedMinWeight, currentEdgeValues);
			
			List currentNormalizedEdgeControlPositionValues = new ArrayList();
			
			currentNormalizedEdgeControlPositionValues.add(currentEdgeNormalizedWeight);
			currentNormalizedEdgeControlPositionValues.add(currentEdgeValues.get(1));
			
			normalizedEdgeControlPositionWeights.add(currentNormalizedEdgeControlPositionValues);
		}
		return normalizedEdgeControlPositionWeights;
	}

	/**
	 * Gets the normalized weight for each edge based on the normalized max & min edge weight.
	 * @param normalizedMaxWeight
	 * @param normalizedMinWeight
	 * @param edgeValues
	 * @return
	 */
	private double getCurrentEdgeNormalizedWeight(double normalizedMaxWeight, 
			double normalizedMinWeight, List edgeValues) {
		
		double currentEdgeWeight;
		double currentNormalizedEdgeWeight;
		
		/*
		 * In case of inappropriate weight set the weight to default value.
		 * */
		try {
			currentEdgeWeight = Double.parseDouble(edgeValues.get(0).toString());	
		} catch (Exception e) {
			currentEdgeWeight = DEFAULT_EDGE_WEIGHT;
		}

		if (normalizedMaxWeight - normalizedMinWeight == 0) {
			currentNormalizedEdgeWeight = 0.0;
		} else {
			currentNormalizedEdgeWeight = (Math.log(currentEdgeWeight + 1.0) 
										  - normalizedMinWeight) 
										  / (normalizedMaxWeight - normalizedMinWeight);
		}
		return currentNormalizedEdgeWeight;
	}

	/*
	 * Used to iterate over all the edges and calculate control points for each edge.  
	 * */
	private void calculateEdgeControlPoints() {

		for (Iterator edgeMappingsIterator = edgesMappingsList.iterator(); 
			edgeMappingsIterator.hasNext();) {
			List currentEdgeControlPositionValues = new ArrayList();
			List<Number> currentEdge = (List) edgeMappingsIterator.next(); 
			int sourceNode = Integer.parseInt(currentEdge.get(0).toString());
			int targetNode = Integer.parseInt(currentEdge.get(1).toString());
			double edgeWeight = Double.parseDouble(currentEdge.get(2).toString());
			currentEdgeControlPositionValues.add(edgeWeight);

			List<List<Double>> controlPoints = calculateControlPoints(sourceNode, targetNode); 
			currentEdgeControlPositionValues.add(controlPoints);
			
			edgeControlPositionValues.add(currentEdgeControlPositionValues);
		}
	}


	private List<List<Double>> calculateControlPoints(int sourceNode, int targetNode) {
		List<List> ancestors = calculateAncestorHierarchy(sourceNode, targetNode);
		List<List<Double>> ancestorPositions = new ArrayList<List<Double>>();
		List<List<Double>> controlPositions = new ArrayList<List<Double>>();
		
		for (Iterator ancestorsIterator = ancestors.iterator(); ancestorsIterator.hasNext();) {
			List<Double> currentAncestorsPositionMappings = 
					levelsPositionMap.get(ancestorsIterator.next());
			ancestorPositions.add(currentAncestorsPositionMappings);
		}

		controlPositions.add(nodePositionsMap.get(sourceNode));
		controlPositions.addAll(ancestorPositions);
		controlPositions.add(nodePositionsMap.get(targetNode));
		
		return adjustControlPositions(controlPositions, edgeBundlingDegree);
	}


	/*
	 * Used to adjust the computed positions by averaging them with shortest line path 
	 * between source & target. 
	 * */
	private List<List<Double>> adjustControlPositions(List<List<Double>> controlPositions, 
			double curveBetaValue) {
		List<List<Double>> adjustedControlPositions = new ArrayList<List<Double>>();
		
		int numberofControlPositions = controlPositions.size(); 
		double x0 = controlPositions.get(0).get(0);
		double y0 = controlPositions.get(0).get(1);
		
		double xLast = controlPositions.get(numberofControlPositions - 1).get(0);
		double yLast = controlPositions.get(numberofControlPositions - 1).get(1);
		
		double xDifference = xLast - x0;
		double yDifference = yLast - y0;
		
		int index = 0;
		for (Iterator controlPositionIterator = controlPositions.iterator(); 
			controlPositionIterator.hasNext(); index++) {
			double xPrime, yPrime;
			List currentControlPositionPair = (List) controlPositionIterator.next();
			List<Double> currentAdjustedControlPositionPair = new ArrayList<Double>();
			
				double xCurrent = Double.parseDouble(currentControlPositionPair.get(0).toString());
				double yCurrent = Double.parseDouble(currentControlPositionPair.get(1).toString());
				
				xPrime = (curveBetaValue * xCurrent) + (1 - curveBetaValue) 
						* (x0 + index * xDifference / (numberofControlPositions - 1));
				
				yPrime = (curveBetaValue * yCurrent) + (1 - curveBetaValue) 
						* (y0 + index * yDifference / (numberofControlPositions - 1));
				
				currentAdjustedControlPositionPair.add(xPrime);
				currentAdjustedControlPositionPair.add(yPrime);

				adjustedControlPositions.add(currentAdjustedControlPositionPair);
		}
		return adjustedControlPositions;
	}


	/*
	 * Used to find the least common ancestor path i.e. shortest path going through
	 * the tree between 2 nodes. It leaves out the least common ancestor in long paths.
	 * */
	private List<List> calculateAncestorHierarchy(int sourceNode, int targetNode) {
		List<List> sourceAncestry = new ArrayList<List>();
		List<List> targetAncestry = new ArrayList<List>();
		for (int level = 0; level < hierarchy.size(); level++) {
			String sourceCommunity = hierarchy.get(level).get(sourceNode);
			String targetCommunity = hierarchy.get(level).get(targetNode);
			
			if (sourceCommunity.equalsIgnoreCase(targetCommunity)) {
				List<List> ancestorHierarchy = new ArrayList<List>();
				List ancestor = new ArrayList();
				if (sourceAncestry.isEmpty()) {
					ancestor.add(level);
					ancestor.add(sourceCommunity);
					ancestorHierarchy.add(ancestor);
					return ancestorHierarchy;
				} else {
					ancestorHierarchy.addAll(sourceAncestry);
					ancestorHierarchy.addAll(reverseList(targetAncestry)); //wait for reverse
					return ancestorHierarchy;
				}
			}
			
			List temporaryAncestorElement = new ArrayList();
			temporaryAncestorElement.add(level);
			temporaryAncestorElement.add(sourceCommunity);
			sourceAncestry.add(temporaryAncestorElement);
			
			List temporaryAncestorElement2 = new ArrayList();
			temporaryAncestorElement2.add(level);
			temporaryAncestorElement2.add(targetCommunity);
			targetAncestry.add(temporaryAncestorElement2);
			
		}
		
		List<List> returnedAncestorHierarchy = new ArrayList<List>();
		returnedAncestorHierarchy.addAll(sourceAncestry);
		returnedAncestorHierarchy.addAll(reverseList(targetAncestry));
		return returnedAncestorHierarchy;
	}

	/**
	 * Reverse the List of List.
	 * @param targetAncestry
	 * @return 
	 */
	private List<List> reverseList(List<List> sequence) {
		Collections.reverse(sequence);
		return sequence;
	}


	/*
	 * Calculate level positions.
	 * */
	private void calculateLevelPositions(Map<List, Double> levelAngles, int totalLevels) {
		Iterator levelAnglesIterator = levelAngles.entrySet().iterator();
		double radius;
		
		while (levelAnglesIterator.hasNext()) {
			List<Double> levelPositionMapValue = new ArrayList<Double>(); 
			Map.Entry levelAngleEntry = (Map.Entry) levelAnglesIterator.next();
			
			
			radius = ((totalLevels 
						- Double.parseDouble(((List) levelAngleEntry.getKey()).get(0).toString()) 
						- 1) 
					* CANVAS_RADIUS) / totalLevels;
			
			levelPositionMapValue.addAll(
					convertToCartesianSystem(radius, 
							Double.parseDouble(levelAngleEntry.getValue().toString())));
			
			levelsPositionMap.put((List) levelAngleEntry.getKey(), levelPositionMapValue);
		}
	}

	/*
	 * It walks the hierarchy finding the angles for each community in each level based on 
	 * the angles/nodes in that community.
	 * */
	private Map<List, Double> calculateLevelAngles() {
		
		double previousAngle, newAngle; 
		Map<List, Double> levelAngles = new HashMap<List, Double>();
		
		int previousNode, currentNode;
		SortedSet<Integer> sortedNodesByAngles = new TreeSet<Integer>(new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return angles.get(o1).compareTo(angles.get(o2));
			}
		});
		
		sortedNodesByAngles.addAll(angles.keySet());
		List<Integer> sortedNodesByAnglesList = new ArrayList<Integer>();
		sortedNodesByAnglesList.addAll(sortedNodesByAngles);
		sortedNodesByAnglesList.add(sortedNodesByAngles.first());
		
		for (int level = 0; level < hierarchy.size(); level++) {
			previousAngle = 0.0;
			previousNode = Integer.parseInt(sortedNodesByAnglesList.get(0).toString()); 
			
			for (Iterator iterator = sortedNodesByAnglesList.iterator(); iterator.hasNext();) {
				
				/*
				 * we've already set the first 'previousAngle'
				 * */
				currentNode = Integer.parseInt(iterator.next().toString());
				if (!hierarchy.get(level).get(currentNode).equals(
						hierarchy.get(level).get(previousNode))) {
					List levelAngleKeyComposite = new ArrayList();
					newAngle = calculateBetweenAngle(angles.get(previousNode), 
													angles.get(currentNode));
					
		
					levelAngleKeyComposite.add(level);
					levelAngleKeyComposite.add(hierarchy.get(level).get(previousNode));
					
					levelAngles.put(levelAngleKeyComposite, 
									calculateBetweenAngle(previousAngle, newAngle));
					
					previousAngle = newAngle;
				}
				previousNode = currentNode;
			} 
		}
		return levelAngles;
	}

	/*
	 * Used to find a mid-angle from where the new level position can start. Also takes care of 
	 * cases where start level angle is greater then the end level angle. 
	 * 
	 * */
	private double calculateBetweenAngle(Double start, Double end) {
		if (end < start) {
			end += 360.0;
		}
		return ((start + end) / 2.0) % 360.0;
	}

	/*
	 * Calculate the Cartesian system co-ordinated for each node.
	 * */
	private void calculateNodePositions() {
		
		Iterator anglesIterator = angles.entrySet().iterator();
		double currentAnglesInDegrees;
		
		while (anglesIterator.hasNext()) {
			Map.Entry angleEntry = (Map.Entry) anglesIterator.next();
			List<Double> positionValues = new ArrayList<Double>();
			
			currentAnglesInDegrees = Double.parseDouble(angleEntry.getValue().toString());
			positionValues = convertToCartesianSystem(CANVAS_RADIUS, currentAnglesInDegrees);
			
			nodePositionsMap.put((Integer) angleEntry.getKey(), positionValues);
			
		}
	}

	/**
	 * Convert the node position to Cartesian system.
	 * @param radius
	 * @param currentAnglesInDegrees
	 */
	private List<Double> convertToCartesianSystem(double radius, double currentAnglesInDegrees) {
		double currentAnglesInRadians;
		List<Double> positionValues = new ArrayList<Double>();
		currentAnglesInRadians = Math.toRadians(currentAnglesInDegrees);
		
		positionValues.add(radius * Math.cos(currentAnglesInRadians));
		positionValues.add(radius * Math.sin(currentAnglesInRadians));
		
		return positionValues;
	}


	/*
	 * Calculate angles for each node.
	 * */
	private void calculateAngles() {
		
		double numberOfNodes; 
		double angle;
		int indexCounter = 0;
		
		List<Integer> sortedNodesForAnglesList = new ArrayList<Integer>();
		
		sortedNodesForAnglesList.addAll(nodesMap.keySet());
		
		Collections.sort(sortedNodesForAnglesList, new Comparator() {

			public int compare(Object n1, Object n2) {
				for (int i = hierarchy.size() - 1; i > -1; i--) {
					if (!hierarchy.get(i).get(n1).equalsIgnoreCase(
							hierarchy.get(i).get(n2))) {
						
						/*
						 * Try to order based on the fact that 2 nodes are in the same community
						 * or level 
						 * */
						return hierarchy.get(i).get(n1).compareToIgnoreCase(
								hierarchy.get(i).get(n2));
					}
				}

				/*
				 * If comparison cannot be made on the basis of the levels or
				 * communities compare using the labels of the nodes.
				 */
				return nodesMap.get(n1).get(0).toString().compareToIgnoreCase(
						nodesMap.get(n2).get(0).toString());
			}

		});
		
		
		numberOfNodes = (double) sortedNodesForAnglesList.size();
		
		Iterator sortedNodesForAnglesIterator = sortedNodesForAnglesList.iterator();
		
		while (sortedNodesForAnglesIterator.hasNext()) {
			angle = ((double) indexCounter + 0.5) * 360.0 / numberOfNodes;
			angles.put((Integer) sortedNodesForAnglesIterator.next(), angle);
			indexCounter++;
		}
	}

	/*
	 * Normalize node strengths.
	 * */
	private void normalizeNodesStrength() {
		Double minStrength, maxStrength, normalizedMaxStrength, normalizedMinStrength;
		
		List nodeStrengthValues = new ArrayList();
		List nodeValues;
		Iterator nodeValuesIterator = nodesMap.values().iterator();
		Double nodeCurrentStrength, normalizedNodeCurrentStrength;
		Map<Integer, List> nodesMapOutput = new HashMap<Integer, List>();

		while (nodeValuesIterator.hasNext()) {
			nodeValues = (List) (nodeValuesIterator.next());
			nodeStrengthValues.add(nodeValues.get(1)); 
		}

		maxStrength = Double.parseDouble(Collections.max(nodeStrengthValues).toString());
		minStrength = Double.parseDouble(Collections.min(nodeStrengthValues).toString());
		
		normalizedMaxStrength = Math.log(maxStrength + 1.0);
		normalizedMinStrength = Math.log(minStrength + 1.0);
		
		Iterator updateNodeStrengthIterator = nodesMap.entrySet().iterator();
		
		while (updateNodeStrengthIterator.hasNext()) {
		
			Entry thisEntry = (Entry) updateNodeStrengthIterator.next();
			
			Integer nodeID = (Integer) thisEntry.getKey();
			nodeValues = (List) thisEntry.getValue();
			
			normalizedNodeCurrentStrength = getCurrentNodeNormalizedStrength(
						normalizedMaxStrength, normalizedMinStrength, nodeValues);
			
			nodeValues.set(1, normalizedNodeCurrentStrength);
			
			nodesMapOutput.put(nodeID, nodeValues);
		}
	}

	/**
	 * Gets the normalized strength for each value based on the normalized max & min node strength.
	 * @param normalizedMaxStrength
	 * @param normalizedMinStrength
	 * @param nodeValues
	 * @return
	 */
	private Double getCurrentNodeNormalizedStrength(
			Double normalizedMaxStrength, Double normalizedMinStrength,
			List nodeValues) {
		Double nodeCurrentStrength;
		Double normalizedNodeCurrentStrength;
		
		/*
		 * In case of inappropriate strength set the strength to default value.
		 * */
		try {
			nodeCurrentStrength = Double.parseDouble(nodeValues.get(1).toString());	
		} catch (Exception e) {
			nodeCurrentStrength = DEFAULT_NODE_STRENGTH;
		}

		if (normalizedMaxStrength - normalizedMinStrength == 0) {
			normalizedNodeCurrentStrength = 0.0;
		} else {
			normalizedNodeCurrentStrength = (Math.log(nodeCurrentStrength + 1.0) 
											- normalizedMinStrength) 
											/ (normalizedMaxStrength - normalizedMinStrength);
		}
		return normalizedNodeCurrentStrength;
	}

	/**
	 * @return the range of nodeColorValues. i.e. the min & max value for color.
	 */
	public Range<Double> getNodeColorValueRange() {
		double minimumColorValue = Collections.min(nodeColorValues);
		double maximumColorValue = Collections.max(nodeColorValues);
		return new Range<Double>(minimumColorValue, maximumColorValue);
	}
	
	public boolean haltParsingNow() {
		return false;
	}

	public void setDirectedEdgeCount(int numberOfEdges) { }

	public void setDirectedEdgeSchema(LinkedHashMap schema) { }

	public void setNodeCount(int numberOfNodes) { }

	public void setNodeSchema(LinkedHashMap schema) { }

	public void setUndirectedEdgeCount(int numberOfEdges) { }

	public void setUndirectedEdgeSchema(LinkedHashMap schema) { }

}
