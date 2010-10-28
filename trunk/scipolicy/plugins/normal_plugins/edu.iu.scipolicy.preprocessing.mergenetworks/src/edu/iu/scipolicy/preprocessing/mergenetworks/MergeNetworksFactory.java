package edu.iu.scipolicy.preprocessing.mergenetworks;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.Constants;
import edu.iu.scipolicy.preprocessing.mergenetworks.utils.NetworkPrefixAttributeDefinition;

/**
 * @author cdtank
 */

public class MergeNetworksFactory implements AlgorithmFactory, ParameterMutator  {
	
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new MergeNetworks(data, parameters, context);
	}
	
	/* This is used to,
	 * 		1. Dynamically add user interaction controls like textboxes that will contain prefix
	 * string which will be used in case there are any overlapping attribute names for the selected 
	 * networks.
	 * 		2. Validate input network files.
	 * 		3. Validate input prefixes. See if they follow all the rules for "prefix" like,
	 * 			1. Prefix cannot be empty.
	 * 			2. Prefix cannot start with a number.
	 * 			3. Prefix for different networks cannot be the same.
	 * 			4. Prefix cannot be more than 10 in length. 
	 * @see org.cishell.framework.algorithm.ParameterMutator#mutateParameters
	 * (org.cishell.framework.data.Data[], org.osgi.service.metatype.ObjectClassDefinition)
	 */
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		File firstNetworkFile = (File) data[0].getData();
		GetNWBFileMetadata firstNetworkMetaDataHandler = new GetNWBFileMetadata();
		
		File secondNetworkFile = (File) data[1].getData();
		GetNWBFileMetadata secondNetworkMetaDataHandler = new GetNWBFileMetadata();
		try {
			NWBFileParser parser = new NWBFileParser(firstNetworkFile);
			parser.parse(firstNetworkMetaDataHandler);
			
			NWBFileParser parser2 = new NWBFileParser(secondNetworkFile);
			parser2.parse(secondNetworkMetaDataHandler);
			
		} catch (IOException e) {
			throw new AlgorithmCreationFailedException(e.getMessage(), e);	
		} catch (ParsingException e) {
			throw new AlgorithmCreationFailedException(
				e.getMessage() + "One (or both) networks do not conform to NWB File Format.",
				e);	
		}
		
		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(parameters.getID(), 
														parameters.getName(), 
														parameters.getDescription(), 
														parameters.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(parameters.getID(), 
														parameters.getName(), 
														parameters.getDescription(), 
														null);
		}
		
		String[] uniqueIdentifierColumnCandidates;

		/*
		 * Currently we are only accepting undirected networks. This will return an error message if
		 * that is not the case. 
		 * */
		if (firstNetworkMetaDataHandler.getDirectedEdgeSchema() == null 
				&& secondNetworkMetaDataHandler.getDirectedEdgeSchema() == null) {
			uniqueIdentifierColumnCandidates = getCommonNodeAttributes(
													firstNetworkMetaDataHandler.getNodeSchema(), 
													secondNetworkMetaDataHandler.getNodeSchema());
		} else {
			throw new AlgorithmCreationFailedException("Directed networks are not supported " 
					+ "currently. Please make sure that both networks are undirected.");	
		}

		AttributeDefinition[] definitions = parameters.getAttributeDefinitions(
													ObjectClassDefinition.ALL);

		for (int ii = 0; ii < definitions.length; ii++) {
			String attributeID = definitions[ii].getID();
			
			if (attributeID.equals("identifier")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(definitions[ii].getID(), 
								definitions[ii].getName(), 
								definitions[ii].getDescription(), 
								definitions[ii].getType(), 
								uniqueIdentifierColumnCandidates, 
								uniqueIdentifierColumnCandidates));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		if (areThereCommonNodeOrEdgeAttributes(firstNetworkMetaDataHandler, 
											   secondNetworkMetaDataHandler)) {
			
			String prefixRules = "\n1. Prefix cannot be empty." 
									+ "\n2. Prefix cannot start with a number."
									+ "\n3. Prefix for different networks cannot be the same." 
									+ "\n4. Prefix cannot be more than 10 in length.";
			
			BasicAttributeDefinition firstPrefixTextbox = new NetworkPrefixAttributeDefinition(
					  "firstnetworkprefix",
					  getPrefixDisplayName(firstNetworkFile.getName(), "1st"),
					  "This prefix will be used for the first network to resolve attribute name " 
					  + "collisions." + prefixRules,
					  1,
					  "NETWORK_1",
					  logger
			  );

			
			BasicAttributeDefinition secondPrefixTextbox = new NetworkPrefixAttributeDefinition(
					  "secondnetworkprefix",
					  getPrefixDisplayName(secondNetworkFile.getName(), "2nd"),
					  "This prefix will be used for the second network to resolve attribute name " 
					  + "collisions." + prefixRules,
					  1,
					  "NETWORK_2",
					  logger
			  );

			/*
			 * This is used to set make sure that the current prefix textbox is aware of other
			 * textboxes when it comes to validation.
			 * */
			((NetworkPrefixAttributeDefinition) firstPrefixTextbox)
					.addOtherPrefixTextboxes(secondPrefixTextbox);
			
			((NetworkPrefixAttributeDefinition) secondPrefixTextbox)
					.addOtherPrefixTextboxes(firstPrefixTextbox);

			/*
			 * This will add the textbox controls to the user's GUI.
			 * */
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, 
					  firstPrefixTextbox);
			
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, 
					  secondPrefixTextbox);
			
		}
		return definition;
	}
	
	/**
	 * Currently the approach for slugifying filenames is naive. In future if there is need, 
	 * we can write more sophisticated method.
	 * @param textToBeSlugified
	 * @return
	 */
	public static String slugify(String textToBeSlugified) {
		String textBlockSeparator = "-";
		return StringUtils.removeEnd(StringUtils.substring(textToBeSlugified.toLowerCase().trim()
											.replaceAll("[^a-zA-Z0-9-]+", textBlockSeparator), 
											0, 
											Constants.MAX_PREFIX_DISPLAY_TEXT_LENGTH),
									 textBlockSeparator);
	}
	
	private String getPrefixDisplayName(String name, String networkNumber) {
		return slugify(name) + " network (" + networkNumber + ") prefix";
	}

	/**
	 * 
	 * This method will compare the node & edge schemas of the input network files & test if there 
	 * are any overlapping attribute names.  
	 * @param firstNetworkMetaDataHandler
	 * @param secondNetworkMetaDataHandler
	 * @return
	 */
	private boolean areThereCommonNodeOrEdgeAttributes(
			GetNWBFileMetadata firstNetworkMetaDataHandler,
			GetNWBFileMetadata secondNetworkMetaDataHandler) {

		/*
		 * Since there is no direct method to find intersection of 2 sets in Collections data 
		 * structure as of now, I made this work around. 
		 * */
		Set<String> commonEdgeAttributes = 
			new HashSet<String>(firstNetworkMetaDataHandler.getUndirectedEdgeSchema().keySet());
		
		commonEdgeAttributes.retainAll(
				secondNetworkMetaDataHandler.getUndirectedEdgeSchema().keySet());
		
		/*
		 * Certain attributes like id, label (for node schema) are mandated by nwb file format so
		 * we know that they will be always present. So we will not consider these NWB mandated
		 * attributes when testing for overlapping attribute names.
		 * */
		
		commonEdgeAttributes.removeAll(Constants.NWB_FORMAT_MANDATED_EDGE_ATTRIBUTES);
		
		
		/*
		 * Since overlapping whether it be for node or edge attributes will result in rendering the 
		 * prefix textbox, even if collision happens only for edge (or just nodes) we will just stop
		 * testing when either of conditions becomes true.
		 * */
		if (commonEdgeAttributes.size() > 0) {
			return true;
		}
		
		Set<String> commonNodeAttributes = 
			new HashSet<String>(firstNetworkMetaDataHandler.getNodeSchema().keySet());
		commonNodeAttributes.retainAll(secondNetworkMetaDataHandler.getNodeSchema().keySet());
		
		commonNodeAttributes.removeAll(Constants.NWB_FORMAT_MANDATED_NODE_ATTRIBUTES);
		
		if (commonNodeAttributes.size() > 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * This is used to generate drop down options for identifying the column which will be used to 
	 * disambiguate nodes.
	 * @param firstNetworkNodeSchema
	 * @param secondNetworkNodeSchema
	 * @return
	 */
	private String[] getCommonNodeAttributes(Map<String, String> firstNetworkNodeSchema, 
									Map<String, String> secondNetworkNodeSchema) {
		
		Set<String> commonNodeAttributes = new HashSet<String>(firstNetworkNodeSchema.keySet());
		commonNodeAttributes.retainAll(secondNetworkNodeSchema.keySet());
		
		/*
		 * If there are no common attributes we can not know what column contains unique node 
		 * identifiers common to both networks.
		 * */
		if (commonNodeAttributes.size() < 1) {
			throw new AlgorithmCreationFailedException("No common attribute names found." 
					+ " The two networks provided must have some node attribute names in common" 
					+ ", so we know which nodes in the first network correspond to which nodes" 
					+ " in the second network.");
		}
		return (String[]) commonNodeAttributes.toArray(new String[]{});
	}
	
}