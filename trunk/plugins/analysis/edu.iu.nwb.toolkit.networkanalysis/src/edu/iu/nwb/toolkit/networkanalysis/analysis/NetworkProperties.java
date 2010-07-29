package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;

import org.cishell.framework.algorithm.AlgorithmExecutionException;


public class NetworkProperties {

	private static final String LINE_SEP = System.getProperty("line.separator");
	public static final DecimalFormat roundedStatisticsFormatter = new DecimalFormat("#.####");

	public static StringBuffer calculateNetworkProperties(final prefuse.data.Graph graph) throws AlgorithmExecutionException {
		boolean isDirectedGraph = graph.isDirected();
		StringBuffer output = new StringBuffer();


		NodeStats nodeStats = NodeStats.constructNodeStats(graph);
		EdgeStats edgeStats = EdgeStats.constructEdgeStats(graph);
		WeakComponentClusteringThread weakComponents = new WeakComponentClusteringThread(graph);
		StrongComponentClusteringThread strongComponents = new StrongComponentClusteringThread(graph);
		//ConnectedComponents cf = ConnectedComponents.constructConnectedComponents(graph);
		
		nodeStats.run();
		edgeStats.run();
		weakComponents.run();
		if(isDirectedGraph){
			strongComponents.run();
		}
		
		try{
		
		nodeStats.join();
		edgeStats.join();
		weakComponents.join();
		strongComponents.join();

		
		
		output = directedInfo(output, isDirectedGraph);
		output = outputNodeAndEdgeStats(output, nodeStats, edgeStats);	

		output = averageDegreeInfo(output, nodeStats,isDirectedGraph);
		
		output = weakConnectedInfo(output, weakComponents, nodeStats);
		if(isDirectedGraph){
			output = strongConnectedInfo(output,strongComponents);
		}else{	
				output.append("Did not calculate strong connectedness because this graph was not directed.");
				output.append(LINE_SEP);	
		}

		if(!(edgeStats.getNumberOfParallelEdges() > 0 || edgeStats.getNumberOfSelfLoops() > 0)){
			output = densityInfo(output, edgeStats, graph.getNodeCount(), graph.getEdgeCount(),isDirectedGraph);
		}else{
			output = addWarningMessages(output, edgeStats, isDirectedGraph);
		}

		return output;
		}catch(InterruptedException ie){
			throw new AlgorithmExecutionException("There were errors completing the evaluation. One of the threads has died.", ie);
		}
		
	}

	private static StringBuffer addWarningMessages(StringBuffer sb, EdgeStats es, boolean isDirected){
		if(es.getNumberOfSelfLoops() > 0 && es.getNumberOfParallelEdges() > 0)
			sb.append("Did not calculate density due to the presence of self-loops and parallel edges.");
		if(es.getNumberOfSelfLoops() > 0 && es.getNumberOfParallelEdges() == 0)
			sb.append("Did not calculate density due to the presence of self-loops.");
		if(es.getNumberOfSelfLoops() == 0 && es.getNumberOfParallelEdges() > 0)
			sb.append("Did not calculate density due to the presence of parallel edges.");
		sb.append(LINE_SEP);

		if(es.getNumberOfSelfLoops() > 0 && !isDirected){
			sb.append("This graph claims to be undirected but has self-loops. Please re-examine your data.");
			sb.append(LINE_SEP);
		}
		if(es.getNumberOfParallelEdges() > 0 && !isDirected){
			sb.append("This graph claims to be undirected but has parallel edges. Please re-examine your data.");
			sb.append(LINE_SEP);
		}
		sb.append("Many algorithms will not function correctly with this graph.");
		sb.append(LINE_SEP);


		return sb;
	}


	private static StringBuffer outputNodeAndEdgeStats(
			StringBuffer sb, NodeStats ns, EdgeStats es) {
		sb.append(LINE_SEP);
		sb.append(ns.nodeInfo());
		sb.append(LINE_SEP);
		sb.append(es.appendEdgeInfo());
		sb.append(LINE_SEP);

		return sb;
	}


	protected static StringBuffer averageDegreeInfo(StringBuffer sb, NodeStats ns, boolean isDirected){
		
		if (isDirected) {
			sb.append("Average total degree: " + ns.getRoundedAverageDegree());
			sb.append(LINE_SEP);
			sb.append("Average in degree: " + ns.getRoundedAverageInDegree());
			sb.append(LINE_SEP);
			sb.append("Average out degree: " + ns.getRoundedAverageOutDegree());
		} else { //is undirected
			sb.append("Average degree: " + ns.getRoundedAverageDegree());
		}
		sb.append(LINE_SEP);
		return sb;
	}


	protected static StringBuffer directedInfo(StringBuffer sb, boolean isDirected){
		if(isDirected){
			sb.append("This graph claims to be directed.");
		}
		else{
			sb.append("This graph claims to be undirected.");
		}
		sb.append(LINE_SEP);
		return sb;
	}
	
	protected static StringBuffer weakConnectedInfo(StringBuffer sb, WeakComponentClusteringThread wcct, NodeStats ns){
		if(wcct.getClusters()==1){
			sb.append("This graph is weakly connected.");
			sb.append(LINE_SEP);
		}
		else{
			sb.append("This graph is not weakly connected.");
			sb.append(LINE_SEP);
		}

		sb.append("There are " + wcct.getClusters() + " weakly connected components. (" + ns.getNumberOfIsolatedNodes() +
		" isolates)");
		sb.append(LINE_SEP);
		sb.append("The largest connected component consists of " + wcct.getMaxSize() + " nodes.");
		sb.append(LINE_SEP);
		
		return sb;
	}
	
	protected static StringBuffer strongConnectedInfo(StringBuffer sb, StrongComponentClusteringThread scct){
		if(scct.getClusters()==1){
			sb.append("This graph is strongly connected");
			sb.append(LINE_SEP);
		}
		else{
			sb.append("This graph is not strongly connected.");
			sb.append(LINE_SEP);
		}

		sb.append("There are " + scct.getClusters() + " strongly connected components.");
		sb.append(LINE_SEP);
		sb.append("The largest strongly connected component consists of " + scct.getMaxSize() + " nodes.");
		sb.append(LINE_SEP);
	
	
	sb.append(LINE_SEP);
	return sb;
	}

	
	public static StringBuffer densityInfo(StringBuffer sb, EdgeStats es,int numNodes,int numEdges,boolean isDirected){
		sb.append(LINE_SEP);
		double density = calculateDensity(numNodes, numEdges, isDirected);
		
		String densityString = roundedStatisticsFormatter.format(density);

		sb.append("Density (disregarding weights): " + densityString);
		int numberOfAdditionalNumericAttributes = es.numericAttributes.size();
		if(numberOfAdditionalNumericAttributes > 0){
			sb.append(LINE_SEP);
			sb.append("Additional Densities by Numeric Attribute");
			/*
			 * Removing the metrics below for the following reasons.
			 * 
			 * These metrics have some major problems:
			 *  1) they count date attributes as edge weights, which gives results that make no sense.
				2) standard is strange, since it can result in densities above 1, which shouldn't be 
					possible (1, or 100%, is supposed to be 'as dense as possible').
				3) observed tries to cover up for the problem with the second. It assumes the maximum 
					possible weight is the highest weight in the current network. But for most of our
					networks, there is no maximum possible weight (what's the maximum number of times
					two people can collaborate with each other?). Thus a network where everyone 
					collaborates with each other once (having a max weight of 1) will be more dense 
					than that same network where one person collaborates 20 additional times 
					(having a max weight of 21). More collaboration shouldn't result in a less dense
					 network.
			 
			 	Also, they take up a lot of vertical console space, usually causing the user to have
			 	 to scroll up to see the node and edge counts, which are needed much more often.
			 */
			
			/*
			 * TODO: Remove the comment above, and remove the code associated with the commented
			 *  		out calculation.
			 */
			
//			sb.append(LINE_SEP);
//			sb.append(printWeightedDensities(false,es,numNodes,isDirected));
//			sb.append(printWeightedDensities(true,es,numNodes,isDirected));

		}

		sb.append(LINE_SEP);
		sb.append(LINE_SEP);



		return sb;
	}


	private static String printWeightedDensities(boolean useObservedMax,EdgeStats es, int numNodes,boolean isDirected){
		double weightedSum,maxObservedValue,weightedDensity;
		long maxConnections;
		
		StringBuffer sb = new StringBuffer();
		
		maxConnections = ((long)numNodes*((long)numNodes-1));
		
		if(useObservedMax){
			sb.append("densities (weighted against observed max)");
		}
		else{
			sb.append("densities (weighted against standard max)");
		}
		sb.append(LINE_SEP);
		for(int i = 0; i < es.getAdditionalNumericAttributes().length; i++){
			sb.append(es.getAdditionalNumericAttributes()[i]+": ");


			weightedSum = es.getWeightedDensitySumArray()[i];
			maxObservedValue = es.getMaxValueArray()[i];
			if(isDirected){
				weightedDensity = (double)weightedSum/maxConnections;
			}
			else{

				weightedDensity = (double)weightedSum/(.5*maxConnections);
			}
			if(useObservedMax)
				weightedDensity = (double)weightedDensity/maxObservedValue;

			sb.append(roundedStatisticsFormatter.format(weightedDensity));
			sb.append(LINE_SEP);
		}
		return sb.toString();
	}

	protected static double calculateDensity(int numberOfNodes, int numberOfEdges, boolean isDirected){
		long maxEdges = (long)numberOfNodes* (long)(numberOfNodes-1);
		if(isDirected){
			return (double)((long)numberOfEdges)/(maxEdges);
		}
		else{
			return(double)((long)numberOfEdges)/(maxEdges/2);
		}
	}
	
	protected static double calculateAverageDegree(int numNodes, int numEdges) {
		//for each edge we have, two nodes have their degree increase by one.
		double averageDegree = 2 * (double) numEdges / (double) numNodes;
		return averageDegree;
	}

}
