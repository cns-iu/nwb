package edu.iu.scipolicy.preprocessing.mergenetworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.Constants;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.MergedNetworkSchemas;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.MergedNetworkToNWBFileWriter;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.MergedNetworkSchemas.NETWORK_HANDLE;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Edge;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.MergeNetworksHelper;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.NetworkMetadata;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Node;

/**
 * This algorithm is used to merge identical networks. 
 * It does so by first creating a unified node & edge schema (attribute name & data type). Along the
 * way it resolves any overlapping attribute names between the 2 networks by creating a new 
 * attribute name using the user provided network prefixes. 
 * After constructing unified schemas it 
 * merges nodes & edges form the 2 networks in to one single network. It ensures unique nodes & 
 * edges in the final network. If it encounters a duplicate node (or edge) it merges it's attributes
 * with the already existing one's.
 * These unique nodes & edges are then used to construct the output network in .NWB format made
 * available to the end user. 
 * @author cdtank (Chintan Tank)
 */
public class MergeNetworks implements Algorithm {
	
	private static final int MIN_EDGE_ATTRIBUTES = 2;
	private static final int MIN_NODE_ATTRIBUTES = 1;
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;


    public MergeNetworks(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
    }

    /* (non-Javadoc)
     * @see org.cishell.framework.algorithm.Algorithm#execute()
     */
    public Data[] execute() throws AlgorithmExecutionException {
		
		File firstInputNetwork = null, secondInputNetwork = null;
		
		MergeNetworksHelper mergeNetworksHelper = null;
		
		/*
		 * Get user input for different parameters. 
		 * */
		String firstInputAttributeCollisionResolvingName = (String) parameters
																.get("firstnetworkprefix");
		String secondInputAttributeCollisionResolvingName = (String) parameters
																.get("secondnetworkprefix");
		
		String nodeIdentifierColumnName = (String) parameters.get("identifier");
		
		try {
			
			/*
			 * Get network data & metadata from first network file.
			 * */
			firstInputNetwork = (File) data[0].getData();
			NetworkMetadata firstNetworkMetadata = getNetworkMetadata(firstInputNetwork);
				
			/*
			 * Get network data & metadata from second network file.
			 * */
			secondInputNetwork = (File) data[1].getData();
			NetworkMetadata secondNetworkMetadata = getNetworkMetadata(secondInputNetwork);
			
			/*
			 * Get mappings of attribute names to resolved attribute names & resolved schemas for
			 * both nodes & edges. (If we have name clashes between attribute names on the two 
			 * networks, resolve them by renaming attributes with prefixes.) 
			 * */
			mergeNetworksHelper = resolveNodeAndEdgeSchema(
										 nodeIdentifierColumnName, 
										 firstInputAttributeCollisionResolvingName, 
										 secondInputAttributeCollisionResolvingName,
										 firstNetworkMetadata,
										 secondNetworkMetadata);
		} catch (AlgorithmExecutionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		/*
		 * Process the provided files to get data & create a merged network. 
		 * */
		try {
			
			/*
			 * This will keep track of unique nodes & edges so that if duplicate nodes are 
			 * encountered in other networks they can be promptly merged.
			 * */
			Map<Object, Node> nodeIDToNodeObject = new HashMap<Object, Node>();
			Map<String, Edge> edgeIDToEdgeObject = new HashMap<String, Edge>();
			
			/*
			 * This method takes two input networks, and combine them (using the schema we just 
			 * generated). Essentially,
			 * 		1. Get all the node and edge data out of the first network.
			 * 		2. Get all the node and edge data out of the second network.
			 * 		3. Combine node and edge data from both networks into our final merged network.
			 * 
			 * This method side-effects nodeIDToNodeObject & edgeIDToEdgeObject maps. The reason is 
			 * because both these maps are being used by the merged network generator later in the 
			 * code & java does not support multiple parameters return. 
			 * */
			mergeNetworkNodesAndEdges(firstInputNetwork, 
									  secondInputNetwork,
									  nodeIdentifierColumnName,
									  mergeNetworksHelper,
									  nodeIDToNodeObject,
									  edgeIDToEdgeObject);
			
			/*
			 * This is used to generate the output NWB file containing the merged network. 
			*/
			File nwbFile = MergedNetworkToNWBFileWriter.writeMergedNetworkToNWBFile(
					mergeNetworksHelper,
					nodeIDToNodeObject, 
					edgeIDToEdgeObject);
			
			Data[] outData = wrapOutData(nwbFile);
		    
			return outData;
		
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (ParsingException e) {
 			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
		
	}

    private MergeNetworksHelper resolveNodeAndEdgeSchema(
    				String nodeIdentifierColumnName, 
    				String firstInputAttributeCollisionResolvingName, 
    				String secondInputAttributeCollisionResolvingName, 
    				NetworkMetadata firstNetworkMetadata, 
    				NetworkMetadata secondNetworkMetadata) throws AlgorithmExecutionException {
    	
    	MergeNetworksHelper mergeNetworksHelper = new MergeNetworksHelper();
    	
    	Set<String> nodeAttributeNamesToBeIgnored = new HashSet<String>();
		nodeAttributeNamesToBeIgnored.add(nodeIdentifierColumnName);
		nodeAttributeNamesToBeIgnored.addAll(Constants.NWB_FORMAT_MANDATED_NODE_ATTRIBUTES);
		
		Set<String> edgeAttributeNamesToBeIgnored = new HashSet<String>();
		edgeAttributeNamesToBeIgnored.addAll(Constants.NWB_FORMAT_MANDATED_EDGE_ATTRIBUTES);
		
		MergedNetworkSchemas mergedNetworkSchemas = 
			new MergedNetworkSchemas(nodeAttributeNamesToBeIgnored, 
									edgeAttributeNamesToBeIgnored,
									firstInputAttributeCollisionResolvingName,
									secondInputAttributeCollisionResolvingName);
		
		/*
		 * This is used to get a map of old attribute names to resolved schema definitions.
		 * */
		mergeNetworksHelper.setOldAttributeNameToNewNodeAttributeNameAndType(
			mergedNetworkSchemas.getOldAttributeToResolvedNodeSchemaDefinitions(
					firstNetworkMetadata.getNodeSchema(), 
					secondNetworkMetadata.getNodeSchema()
					)
			);
		
		mergeNetworksHelper.setOldAttributeNameToNewEdgeAttributeNameAndType(
			mergedNetworkSchemas.getOldAttributeToResolvedEdgeSchemaDefinitions(
					firstNetworkMetadata.getEdgeSchema(), 
					secondNetworkMetadata.getEdgeSchema()
					)
			);
		
		/*
		 * Proceed only if the resolved schemas are valid.
		 * */
		if (!areResolvedSchemasValid(mergeNetworksHelper)) {
			throw new AlgorithmExecutionException("Errors encountered while resolving attribute " 
					+ "names for the merged network.");
		}
		
		/*
		 * This will set the unified node & edge schema which will be used to generate the 
		 * merged network file.
		 * */
		mergeNetworksHelper.setFinalNodeSchema(
				mergedNetworkSchemas.getFinalNodeSchema(
						mergeNetworksHelper.getOldAttributeNameToNewNodeAttributeNameAndType())
			);
		
		mergeNetworksHelper.setFinalEdgeSchema(
				mergedNetworkSchemas.getFinalEdgeSchema(
							mergeNetworksHelper.getOldAttributeNameToNewEdgeAttributeNameAndType())
			);
		return mergeNetworksHelper;
    }
    
	/**
	 * @param firstInputNetwork
	 * @param secondInputNetwork
	 * @param nodeIdentifierColumnName
	 * @param mergeNetworksHelper 
	 * @param nodeIDToNodeObject
	 * @param edgeIDToEdgeObject
	 * @throws IOException
	 * @throws ParsingException
	 */
	private void mergeNetworkNodesAndEdges(
			File firstInputNetwork,
			File secondInputNetwork,
			String nodeIdentifierColumnName,
			MergeNetworksHelper mergeNetworksHelper, 
			Map<Object, Node> nodeIDToNodeObject,
			Map<String, Edge> edgeIDToEdgeObject) throws IOException,
			ParsingException {
		
		NWBFileParser parser;
		parser = new NWBFileParser(firstInputNetwork);
		
		/*
		 * Create networks assets (node & edge) for the first network.
		 * */
		MergeNetworkAssetsComputation mergeNetworkAssetsComputation = 
			new MergeNetworkAssetsComputation(nodeIdentifierColumnName, 
					mergeNetworksHelper.getOldAttributeNameToNewNodeAttributeNameAndType()
							.get(MergedNetworkSchemas.NETWORK_HANDLE.FIRST), 
					mergeNetworksHelper.getOldAttributeNameToNewEdgeAttributeNameAndType()
							.get(MergedNetworkSchemas.NETWORK_HANDLE.FIRST),
					nodeIDToNodeObject,
					edgeIDToEdgeObject,
					logger);
		parser.parse(mergeNetworkAssetsComputation);
		
		
		parser = new NWBFileParser(secondInputNetwork);

		/*
		 * Create networks assets (node & edge) for the second network.
		 * */			
		mergeNetworkAssetsComputation = 
			new MergeNetworkAssetsComputation(nodeIdentifierColumnName, 
					mergeNetworksHelper.getOldAttributeNameToNewNodeAttributeNameAndType()
					.get(MergedNetworkSchemas.NETWORK_HANDLE.SECOND), 
			mergeNetworksHelper.getOldAttributeNameToNewEdgeAttributeNameAndType()
					.get(MergedNetworkSchemas.NETWORK_HANDLE.SECOND),
					nodeIDToNodeObject,
					edgeIDToEdgeObject,
					logger);
		
		parser.parse(mergeNetworkAssetsComputation);
	}

	/**
	 * @param inputNetwork
	 * @return
	 * @throws AlgorithmExecutionException
	 */
	private NetworkMetadata getNetworkMetadata(File inputNetwork)
			throws AlgorithmExecutionException {
		NetworkMetadata firstNetworkMetadata = validateInputFileAndGetMetadata(inputNetwork);
		
		if (firstNetworkMetadata == null) {
			throw new AlgorithmExecutionException(inputNetwork.getName() + " is not valid.");
		}
		return firstNetworkMetadata;
	}

    /**
     * This method checks if all the schemas have minimum allowed attributes. 
     * 		For node schemas there should be at least 1 attribute which acts as a unique identifier.
     * 		For edge schemas there should be at least 2 attributes, for source & target node ids.
     * @param mergeNetworksHelper
     * @return
     */
    private boolean areResolvedSchemasValid(MergeNetworksHelper mergeNetworksHelper) {

    	for (Map.Entry<NETWORK_HANDLE, Map<String, NWBAttribute>> currentNodeSchema
    			: mergeNetworksHelper.getOldAttributeNameToNewNodeAttributeNameAndType()
    					.entrySet()) {
    		
    		if (currentNodeSchema.getValue().size() < MIN_NODE_ATTRIBUTES) {
    			return false;
    		} 
    		
    	}
    	
    	for (Map.Entry<NETWORK_HANDLE, Map<String, NWBAttribute>> currentEdgeSchema
    			: mergeNetworksHelper.getOldAttributeNameToNewEdgeAttributeNameAndType()
    					.entrySet()) {
    		
    		if (currentEdgeSchema.getValue().size() < MIN_EDGE_ATTRIBUTES) {
    			return false;
    		} 
    		
    	}
    	
    	return true;
	}

	private Data[] wrapOutData(File nwbFile) {
    	Data outData = new BasicData(nwbFile, NWBFileProperty.NWB_MIME_TYPE);
    	Dictionary metaData = outData.getMetadata();
    	metaData.put(DataProperty.LABEL, "Merged Network");
    	metaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	metaData.put(DataProperty.PARENT, data[1]);
    	
    	return new Data[] { outData };
    }
    
	private NetworkMetadata validateInputFileAndGetMetadata(File inData)
		throws AlgorithmExecutionException {
		
		ValidateNWBFile validateParser = new ValidateNWBFile();
		int numberOfNodes;
		int numberOfEdges;
		boolean isUndirectedNetwork;
		NetworkMetadata networkMetadata = null;
		
		try {
			validateParser.validateNWBFormat(inData);
			numberOfNodes = validateParser.getTotalNumOfNodes();
			numberOfEdges = validateParser.getTotalNumOfUndirectedEdges();
			isUndirectedNetwork = validateParser.isUndirectedGraph();
			
			/* TODO: This used to check if numberOfEdges >= 1, but I'm thinking that's wrong.
			 * Look into this more though.
			 * -Patrick
			 */
			if (isUndirectedNetwork && numberOfEdges >= 0 && numberOfNodes > 1) {
				networkMetadata = new NetworkMetadata(validateParser.getNodeAttrList(), 
													  validateParser.getUndirectedEdgeAttrList());
			} 
			
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
		return networkMetadata;
	}

}
