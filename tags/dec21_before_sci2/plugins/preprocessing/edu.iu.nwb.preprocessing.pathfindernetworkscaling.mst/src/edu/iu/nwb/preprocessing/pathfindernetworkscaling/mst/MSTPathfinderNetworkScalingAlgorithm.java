package edu.iu.nwb.preprocessing.pathfindernetworkscaling.mst;

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
 * another alternative, MST-Pathfinder algorithm, is used which prunes the original 
 * network to get its PFNET(infinity,n-1) in just O(n^2 * log n) time.
 * 
 * The underlying idea comes from the fact that the union (superposition) of all 
 * the Minimum Spanning Trees extracted from a given network is equivalent to the 
 * PFNET resulting from the Pathfinder algorithm parameterized by a specific set 
 * of values (r = infinity and q = n-1), those usually considered in many different
 * applications.
 * 
 * @author Chintan Tank
 */

public class MSTPathfinderNetworkScalingAlgorithm implements Algorithm{
	
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService logger;

	private int numberOfEdges;
	private int numberOfNodes;
	private boolean isUndirectedNetwork;
	
	public static final String WEIGHT_DISSIMILARITY = "DISSIMILARITY";
	public static final String WEIGHT_SIMILARITY = "SIMILARITY";
	
    /**
     * Construct with the appropriate parameters
     * @param data This contains the input graph
     * @param parameters This contains number of iterations to be performed.
     * @param context And this allows access to some additional capabilities
     * @throws AlgorithmExecutionException 
     */
	
    public MSTPathfinderNetworkScalingAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException{
		
		File inputData = (File)data[0].getData();
		
    	try {
			validateInputFile(inputData);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.", e);
		}
    	
		
    	if(isUndirectedNetwork && numberOfEdges > 1 && numberOfNodes > 1) {
			try {
				
				String edgeWeightColumnName = (String) parameters.get("weightcolumn");
				String edgeWeightType = (String) parameters.get("weighttypecolumn");
				
				/*
				 * Used to process the provided file for Pathfinder Network Scaling.
				 * */
				NWBFileParser parser = new NWBFileParser(inputData);
				MSTPathfinderNetworkScalingComputation networkScalingComputation = 
					new MSTPathfinderNetworkScalingComputation(edgeWeightColumnName, edgeWeightType, logger);
				parser.parse(networkScalingComputation);
				
				/*
				 * Used to generate the output file containing the scaled network. 
				*/
				File outputNWBFile = File.createTempFile("nwb-", ".nwb");
				NWBFileParser outputParser = new NWBFileParser(inputData);
				outputParser.parse(new MSTPathfinderNetworkScalingOutputGenerator(networkScalingComputation, outputNWBFile));
				
				Data outNWBData = new BasicData(outputNWBFile,"file:text/nwb");
				prepareOutputMetadata(edgeWeightType, networkScalingComputation, outNWBData);
				
				return new Data[]{outNWBData};
			
			} catch (FileNotFoundException e) {
				throw new AlgorithmExecutionException("NWB File'" + inputData.getAbsolutePath() + "' not found.");
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e);
			} catch (ParsingException e) {
				throw new AlgorithmExecutionException(e);
			}
    	} 
    	else {
    		throw new AlgorithmExecutionException("Input network should be Undirected " +
    				"with more than 1 edge & 1 node.");
    	}


	}

	/**
	 * @param edgeWeightType
	 * @param networkScalingComputation
	 * @param outNWBData
	 */
	private void prepareOutputMetadata(String edgeWeightType,
			MSTPathfinderNetworkScalingComputation networkScalingComputation,
			Data outNWBData) {
		outNWBData.getMetadata().put(DataProperty.LABEL, "MST Pathfinder Network Scaling used weight " 
				+ edgeWeightType + " to reduce " + numberOfEdges + " edges to " 
				+ networkScalingComputation.getScaledNetworkEdgeCount() + ". Scaling ratio = " 
				+ calculateScalingRatio((double)numberOfEdges, (double)networkScalingComputation.getScaledNetworkEdgeCount()));
		outNWBData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		outNWBData.getMetadata().put(DataProperty.PARENT, data[0]);
	}
    
    private double calculateScalingRatio(double eoriginalNumberOfEdges, double scaledNetworkNumberOfEdges) {
       	DecimalFormat roundedRatioFormat = new DecimalFormat("#.###");
		return Double.valueOf(roundedRatioFormat.format(eoriginalNumberOfEdges/scaledNetworkNumberOfEdges));
    }

	private void validateInputFile(File inData) throws ParsingException, AlgorithmExecutionException {
		ValidateNWBFile validateParser = new ValidateNWBFile();

		try {
			validateParser.validateNWBFormat(inData);
			numberOfNodes = validateParser.getTotalNumOfNodes();
			numberOfEdges = validateParser.getTotalNumOfUndirectedEdges();
			isUndirectedNetwork = validateParser.isUndirectedGraph();
		} catch (Exception e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.");
		}
		
	}

}
