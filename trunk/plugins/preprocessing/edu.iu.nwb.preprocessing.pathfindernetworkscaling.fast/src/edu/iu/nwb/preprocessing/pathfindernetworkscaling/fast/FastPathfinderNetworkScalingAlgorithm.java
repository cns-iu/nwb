package edu.iu.nwb.preprocessing.pathfindernetworkscaling.fast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Network scaling algorithms such as the Pathfinder algorithm are used to prune 
 * many different kinds of networks, including citation networks, random networks, 
 * and social networks. However, this algorithm suffers from run time problems for 
 * large networks and online processing due to its O(n^4) time complexity. Hence 
 * another alternative,  Fast Pathfinder algorithm, is used which prunes the original 
 * network to get its PFNET(r,n-1) in just O(n^3) time.
 * 
 * The underlying idea of this approach is based on a classical algorithm in graph 
 * theory for shortest path computation - Floyd-Warshall's Shortest Path Algorithm. 
 *    
 * @author Chintan Tank
 */

public class FastPathfinderNetworkScalingAlgorithm implements Algorithm {
	
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;

	private int numberOfEdges;
	private int numberOfNodes;
	private boolean isUndirectedNetwork, isUnweightedNetwork;
	private double rParameter;
	
	public static final String WEIGHT_DISSIMILARITY = "DISSIMILARITY";
	public static final String WEIGHT_SIMILARITY = "SIMILARITY";
	
    /**
     * Construct with the appropriate parameters.
     * @param data This contains the input graph
     * @param parameters This contains number of iterations to be performed
     * @param context And this allows access to some additional capabilities
     * @param isUnweighted 
     * @throws AlgorithmExecutionException 
     */
	
    public FastPathfinderNetworkScalingAlgorithm(Data[] data, 
    											 Dictionary parameters, 
    											 CIShellContext context, boolean isUnweighted) {
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
		this.isUnweightedNetwork = isUnweighted;
    }

    private void printFastPathfinderUsageWarnings(String type) {
    	
    	if (type.equalsIgnoreCase("UNDIRECTED")) {
    		logger.log(LogService.LOG_WARNING,
        			"For undirected networks, use MST Pathfinder network scaling " 
        			+ "algorithm for faster results.\n");	
    	} if (type.equalsIgnoreCase("UNWEIGHTED")) {
    		logger.log(LogService.LOG_WARNING,
        			"For undirected networks having edges with no weight, " 
    				+ "use MST Pathfinder network scaling " 
        			+ "algorithm for faster & better results.\n");	
    	} if (type.equalsIgnoreCase("SCALING_RATIO")) {
    		logger.log(LogService.LOG_WARNING,
        			"The low scaling ratio (<= 1.5) is probably caused due to " 
    				+ "many of the edge weights being equal to the minimum edge weight. " 
    				+ "Better results can be obtained if MST Pathfinder "        			
    				+ "algorithm is used. But it only works for Undirected networks.\n");	
    	}
	}

	public Data[] execute() throws AlgorithmExecutionException {
		
		File inputData = (File) data[0].getData();
		
    	try {
			validateInputFile(inputData);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.", e);
		}
    	
		if (isUndirectedNetwork) {
			printFastPathfinderUsageWarnings("UNDIRECTED");
		}
		
		if (isUnweightedNetwork) {
			printFastPathfinderUsageWarnings("UNWEIGHTED");
		}
		
    	if (numberOfEdges > 1 && numberOfNodes > 1) {
			try {
				
				String edgeWeightColumnName = (String) parameters.get("weightcolumn");
				String edgeWeightType = (String) parameters.get("weighttypecolumn");
				
				/*
				 * Since r = infinity provides the optimal solution, preference is given to
				 * that in the user interface. If the r = infinity check-box is checked (the 
				 * default behavior) then set r = infinity value else parse it from r 
				 * parameter input box.
				 * */
				if ((Boolean) parameters.get("rinfinity")) {
					rParameter = Double.POSITIVE_INFINITY;
				} else {
					rParameter = Double.parseDouble(parameters.get("rparameter").toString());
				}
				
				/*
				 * Used to process the provided file for Pathfinder Network Scaling.
				 * */
				NWBFileParser parser = new NWBFileParser(inputData);
				FastPathfinderNetworkScalingComputation networkScalingComputation = 
					new FastPathfinderNetworkScalingComputation(edgeWeightColumnName, 
																edgeWeightType,
																rParameter,
																numberOfNodes,
																logger);
				parser.parse(networkScalingComputation);
				
				/*
				 * Used to generate the output file containing the scaled network. 
				*/
				File outputNWBFile = File.createTempFile("nwb-", ".nwb");
				NWBFileParser outputParser = new NWBFileParser(inputData);
				final FastPathfinderNetworkScalingOutputGenerator scalingOutputGenerator = 
					new FastPathfinderNetworkScalingOutputGenerator(networkScalingComputation, 
																	outputNWBFile);
				outputParser.parse(scalingOutputGenerator);
				
				Data outNWBData = new BasicData(outputNWBFile, "file:text/nwb");
				prepareOutputMetadata(edgeWeightType, scalingOutputGenerator, outNWBData);
				
				return new Data[]{outNWBData};
			
			} catch (FileNotFoundException e) {
				throw new AlgorithmExecutionException("NWB File'" + inputData.getAbsolutePath() 
						+ "' not found.", e);
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e);
			} catch (ParsingException e) {
				throw new AlgorithmExecutionException(e);
			}
    	} else {
    		throw new AlgorithmExecutionException("Input network should have " 
    				+ "more than 1 edge & 1 node.");
    	}
	}

	/**
	 * @param edgeWeightType
	 * @param scalingOutputGenerator
	 * @param outNWBData
	 */
	private void prepareOutputMetadata(String edgeWeightType,
			FastPathfinderNetworkScalingOutputGenerator scalingOutputGenerator, 
			Data outNWBData) {
		final double scalingRatio = calculateScalingRatio(
						(double) numberOfEdges, 
						(double) scalingOutputGenerator.getScaledNetworkEdgeCount());
		outNWBData.getMetadata().put(
				DataProperty.LABEL, "Fast Pathfinder Network Scaling used weight " 
				+ edgeWeightType + " & value of r = " + rParameter 
				+ " to reduce " + numberOfEdges + " edges to " 
				+ scalingOutputGenerator.getScaledNetworkEdgeCount() + ". Scaling ratio = " 
				+ scalingRatio);
		outNWBData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		outNWBData.getMetadata().put(DataProperty.PARENT, data[0]);
		
		if (scalingRatio <= 1.5) {
			printFastPathfinderUsageWarnings("SCALING_RATIO");
		}
	}
    
    private double calculateScalingRatio(double originalNumberOfEdges, 
    									 double scaledNetworkNumberOfEdges) {
       	DecimalFormat roundedRatioFormat = new DecimalFormat("#.###");
		return Double.valueOf(
				roundedRatioFormat.format(originalNumberOfEdges / scaledNetworkNumberOfEdges));
    }

	private void validateInputFile(File inData) 
		throws ParsingException, AlgorithmExecutionException {
		
		ValidateNWBFile validateParser = new ValidateNWBFile();

		try {
			validateParser.validateNWBFormat(inData);
			numberOfNodes = validateParser.getTotalNumOfNodes();
			isUndirectedNetwork = validateParser.isUndirectedGraph();
			if (isUndirectedNetwork) {
				numberOfEdges = validateParser.getTotalNumOfUndirectedEdges();
			} else {
				numberOfEdges = validateParser.getTotalNumOfDirectedEdges();
			}
		} catch (Exception e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.");
		}
	}
}
