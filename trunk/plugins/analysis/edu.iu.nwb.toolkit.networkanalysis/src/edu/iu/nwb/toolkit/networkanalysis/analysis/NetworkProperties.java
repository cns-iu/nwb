package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.io.File;
import java.text.DecimalFormat;


public class NetworkProperties {

	private static final String LINE_SEP = System.getProperty("line.separator");

	public static StringBuffer calculateNetworkProperties(final prefuse.data.Graph graph){
		boolean isDirectedGraph = graph.isDirected();
		StringBuffer output = new StringBuffer();


		NodeStats nodeStats = new NodeStats(graph);
		EdgeStats edgeStats = EdgeStats.constructEdgeStats(graph);

		output = directedInfo(output, isDirectedGraph);
		output = outputNodeAndEdgeStats(output, nodeStats, edgeStats, isDirectedGraph);

		ComponentForest cf = new ComponentForest(graph);

		output = connectedInfo(output, cf, nodeStats,isDirectedGraph);

		if(!(edgeStats.getNumberOfParallelEdges() > 0 || edgeStats.getNumberOfSelfLoops() > 0)){

			output = averageDegreeInfo(output, graph.getNodeCount(),graph.getEdgeCount(),isDirectedGraph);

			output = densityInfo(output, edgeStats, graph.getNodeCount(), graph.getEdgeCount(),isDirectedGraph);
		}else{
			output = addWarningMessages(output, edgeStats, isDirectedGraph);
		}

		return output;
	}

	private static StringBuffer addWarningMessages(StringBuffer sb, EdgeStats es, boolean isDirected){
		if(es.getNumberOfSelfLoops() > 0 && es.getNumberOfParallelEdges() > 0)
			sb.append("Did not calculate density due to the presence of self-loops and parallel edges.");
		if(es.getNumberOfSelfLoops() > 0 && es.getNumberOfParallelEdges() == 0)
			sb.append("Did not calculate density due to the presence of self-loops.");
		if(es.getNumberOfSelfLoops() == 0 && es.getNumberOfParallelEdges() > 0)
			sb.append("Did not calculate density due to the presence of parallel edges.");
		sb.append(System.getProperty("line.separator"));

		if(es.getNumberOfSelfLoops() > 0 && !isDirected){
			sb.append("This graph claims to be undirected but has self-loops. Please re-examine your data.");
			sb.append(System.getProperty("line.separator"));
		}
		if(es.getNumberOfParallelEdges() > 0 && !isDirected){
			sb.append("This graph claims to be undirected but has parallel edges. Please re-examine your data.");
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("Many algorithms will not function correctly with this graph.");
		sb.append(System.getProperty("line.separator"));


		return sb;
	}


	private static StringBuffer outputNodeAndEdgeStats(StringBuffer sb, NodeStats ns, EdgeStats es, boolean isDirectedGraph){
		sb.append(System.getProperty("line.separator"));
		sb.append(ns.nodeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(es.appendEdgeInfo());
		sb.append(System.getProperty("line.separator"));

		return sb;
	}


	protected static StringBuffer averageDegreeInfo(StringBuffer sb, int numNodes, int numEdges, boolean isDirected){
		double averageDegree = calculateAverageDegree(numNodes,numEdges);
		if (isDirected) {
			sb.append("average total degree: " + averageDegree);
		} else { //is undirected
			sb.append("average degree: " + averageDegree);
		}
		sb.append(System.getProperty("line.separator"));
		return sb;
	}

	protected static StringBuffer directedInfo(StringBuffer sb, boolean isDirected){
		if(isDirected){
			sb.append("This graph claims to be directed.");
		}
		else{
			sb.append("This graph claims to be undirected.");
		}
		sb.append(System.getProperty("line.separator"));
		return sb;
	}

	protected static StringBuffer connectedInfo(StringBuffer sb, ComponentForest cf, NodeStats ns, boolean isDirected){
		if(cf.isWeaklyConnected()){
			sb.append("This graph is weakly connected.");
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("This graph is not weakly connected.");
			sb.append(System.getProperty("line.separator"));
		}

		sb.append("There are " + cf.getWeakComponentClusters() + " weakly connected components. (" + ns.getNumberOfIsolatedNodes() +
		" isolates)");
		sb.append(System.getProperty("line.separator"));
		sb.append("The largest connected component consists of " + cf.getMaximumWeakConnectedNodes()+ " nodes.");
		sb.append(System.getProperty("line.separator"));

		if(isDirected){

			if(cf.isStronglyConnected()){
				sb.append("This graph is strongly connected");
				sb.append(System.getProperty("line.separator"));
			}
			else{
				sb.append("This graph is not strongly connected.");
				sb.append(System.getProperty("line.separator"));
			}

			sb.append("There are " + cf.getStrongComponentClusters() + " strongly connected components.");
			sb.append(System.getProperty("line.separator"));
			sb.append("The largest strongly connected component consists of " + cf.getMaximumStrongConnectedNodes() + " nodes.");
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("Did not calculate strong connectedness because this graph was not directed.");
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(System.getProperty("line.separator"));
		return sb;
	}

	public static StringBuffer densityInfo(StringBuffer sb, EdgeStats es,int numNodes,int numEdges,boolean isDirected){
		sb.append(System.getProperty("line.separator"));
		double density = calculateDensity(numNodes, numEdges, isDirected);
		DecimalFormat densityFormatter = new DecimalFormat("#.#####");
		String densityString = densityFormatter.format(density);

		sb.append("density (disregarding weights): " + densityString);
		int numberOfAdditionalNumericAttributes = es.numericAttributes.size();
		if(numberOfAdditionalNumericAttributes > 0){
			sb.append(System.getProperty("line.separator"));
			sb.append("Additional Densities by Numeric Attribute");
			sb.append(System.getProperty("line.separator"));
			sb.append(printWeightedDensities(false,es,numNodes,isDirected));
			sb.append(printWeightedDensities(true,es,numNodes,isDirected));

		}

		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));



		return sb;
	}


	private static String printWeightedDensities(boolean useObservedMax,EdgeStats es, int numNodes,boolean isDirected){
		double weightedSum,maxObservedValue,weightedDensity, maxConnections;
		DecimalFormat densityFormatter = new DecimalFormat("#.#####");
		StringBuffer sb = new StringBuffer();
		maxConnections = (numNodes*(numNodes-1));
		if(useObservedMax){
			sb.append("densities (weighted against observed max)");
		}
		else{
			sb.append("densities (weighted against standard max)");
		}
		sb.append(System.getProperty("line.separator"));
		for(int i = 0; i < es.getAdditionalNumericAttributes().length; i++){
			sb.append(es.getAdditionalNumericAttributes()[i]+": ");


			weightedSum = es.getWeightedDensitySumArray()[i];
			maxObservedValue = es.getMaxValueArray()[i];
			if(isDirected){
				weightedDensity = weightedSum/maxConnections;
			}
			else{

				weightedDensity = weightedSum/(.5*maxConnections);
			}
			if(useObservedMax)
				weightedDensity = weightedDensity/maxObservedValue;

			sb.append(densityFormatter.format(weightedDensity));
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected static double calculateDensity(int numberOfNodes, int numberOfEdges, boolean isDirected){
		long maxEdges = numberOfNodes* (numberOfNodes-1);
		if(isDirected){
			return (double)numberOfEdges/(maxEdges);
		}
		else{
			return(double)numberOfEdges/(maxEdges/2);
		}
	}

	protected static double calculateAverageDegree(int numNodes, int numEdges) {
		//for each edge we have, two nodes have their degree increase by one.
		double averageDegree = 2 * (double) numEdges / (double) numNodes;
		return averageDegree;
	}
}
