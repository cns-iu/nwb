package edu.iu.nwb.visualization.roundrussell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.visualization.roundrussell.utility.Constants;

/**
 * @author cdtank
 *
 */

public class RoundRussellAlgorithmFactory implements AlgorithmFactory, ParameterMutator  {
	
	public static final String NO_EDGE_WEIGHT_IDENTIFIER = "Unweighted";
	public static final String NO_STRENGTH_IDENTIFIER = "No Strength";
	public static final String NO_COLOR_IDENTIFIER = "No Node Color";
	public static final String NO_LEVEL_IDENTIFIER = "No Level";
	public static final Collection<String> NODE_KEYS_TO_IGNORE =
		Collections.unmodifiableList(Arrays.asList(NWBFileProperty.ATTRIBUTE_ID));
	public static final Collection<String> NODE_STRENGTH_KEYS_TO_ADD_TO_FRONT =
		Collections.unmodifiableList(Arrays.asList(NO_STRENGTH_IDENTIFIER));
	public static final Collection<String> NODE_C0LOR_KEYS_TO_ADD_TO_FRONT =
		Collections.unmodifiableList(Arrays.asList(NO_COLOR_IDENTIFIER));
	public static final Collection<String> NODE_LEVEL_KEYS_TO_ADD_TO_FRONT =
		Collections.unmodifiableList(Arrays.asList(NO_LEVEL_IDENTIFIER));
	public static final Collection<String> EGGE_KEYS_TO_IGNORE =
		Collections.unmodifiableList(Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.ATTRIBUTE_TARGET));
	public static final Collection<String> EDGE_KEYS_TO_ADD_TO_FRONT =
		Collections.unmodifiableList(Arrays.asList(NO_EDGE_WEIGHT_IDENTIFIER));
	
	private static URL postScriptHeaderPath;
	private static final String postScriptHeaderFilePath = "postScriptHeader.ps";

	protected void activate(ComponentContext componentContext) {
    	BundleContext bundleContext = componentContext.getBundleContext();
    	
    	RoundRussellAlgorithmFactory.postScriptHeaderPath =
    		bundleContext.getBundle().getResource(postScriptHeaderFilePath);
    	PostScriptOperations.setPostScriptHeaderFile(postScriptHeaderPath);
    	
    }
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		return new RoundRussellAlgorithm(data, parameters, context);
	}
	
	@SuppressWarnings("unchecked")	// Raw Map (metaDataHandler.getNodeSchema())
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata metaDataHandler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(metaDataHandler);
		} catch (IOException e1) {
			return parameters;
		} catch (ParsingException e) {
			return parameters;
		}

		Collection<String> nodeStrengthAttribute = MapUtilities.getValidKeysOfTypesInMap(
			metaDataHandler.getNodeSchema(),
			NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES,
			NODE_KEYS_TO_IGNORE);
		nodeStrengthAttribute = ArrayListUtilities.unionCollections(
			NODE_STRENGTH_KEYS_TO_ADD_TO_FRONT, nodeStrengthAttribute, null);
//		String[] nodeStrengthAttribute =
//			MapUtilities.getValidKeysOfTypesInMap(
//					metaDataHandler.getNodeSchema(),
//					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
//					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
//					new String[]{ NO_STRENGTH_IDENTIFIER });

		Collection<String> nodeColorAttribute = MapUtilities.getValidKeysOfTypesInMap(
			metaDataHandler.getNodeSchema(),
			NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES,
			NODE_KEYS_TO_IGNORE);
		nodeColorAttribute = ArrayListUtilities.unionCollections(
			NODE_C0LOR_KEYS_TO_ADD_TO_FRONT, nodeColorAttribute, null);
//		String[] nodeColorAttribute =
//			MapUtilities.getValidKeysOfTypesInMap(
//					metaDataHandler.getNodeSchema(),
//					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
//					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
//					new String[]{ NO_COLOR_IDENTIFIER });


		Collection<String> nodeLevelAttribute = MapUtilities.getValidKeysOfTypesInMap(
			metaDataHandler.getNodeSchema(),
			NWBFileProperty.ALL_ATTRIBUTE_TYPES,
			NODE_KEYS_TO_IGNORE);
		nodeLevelAttribute = ArrayListUtilities.unionCollections(
			NODE_LEVEL_KEYS_TO_ADD_TO_FRONT, nodeLevelAttribute, null);
//		String[] nodeLevelAttribute =
//			MapUtilities.getValidKeysOfTypesInMap(
//					metaDataHandler.getNodeSchema(),
//					(String[]) NWBFileProperty.ALL_ATTRIBUTE_TYPES.toArray(new String[0]),
//					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
//					new String[]{ NO_LEVEL_IDENTIFIER });
		
		
		LinkedHashMap edgeSchema;
		if (metaDataHandler.getDirectedEdgeSchema() != null) {
			edgeSchema  = metaDataHandler.getDirectedEdgeSchema();
		} else {
			edgeSchema = metaDataHandler.getUndirectedEdgeSchema();
		}

		Collection<String> edgeAttributes = MapUtilities.getValidKeysOfTypesInMap(
			edgeSchema,
			NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES,
			EGGE_KEYS_TO_IGNORE);
		edgeAttributes = ArrayListUtilities.unionCollections(
			EDGE_KEYS_TO_ADD_TO_FRONT, edgeAttributes, null);
//		String[] edgeAttributes =
//			MapUtilities.getValidKeysOfTypesInMap(
//					edgeSchema,
//					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
//					new String[]{ NWBFileProperty.ATTRIBUTE_SOURCE, 
//								  NWBFileProperty.ATTRIBUTE_TARGET },
//					new String[]{ NO_EDGE_WEIGHT_IDENTIFIER });
		
		DropdownMutator mutator = new DropdownMutator();		
		
		mutator.add(RoundRussellAlgorithm.STRENGTH_COLUMN_ID, 
					nodeStrengthAttribute,
					getMatchedKey(nodeStrengthAttribute, "strength"));
		
		mutator.add(RoundRussellAlgorithm.LEVEL0_COLUMN_ID, 
					nodeLevelAttribute,
					getDefaultLevelOption(nodeLevelAttribute, 0));

		mutator.add(RoundRussellAlgorithm.LEVEL1_COLUMN_ID, 
					nodeLevelAttribute,
					getDefaultLevelOption(nodeLevelAttribute, 1));
		
		mutator.add(RoundRussellAlgorithm.LEVEL2_COLUMN_ID, 
					nodeLevelAttribute,
					getDefaultLevelOption(nodeLevelAttribute, 2));
		
		mutator.add(RoundRussellAlgorithm.LEVEL3_COLUMN_ID, 
					nodeLevelAttribute,
					getDefaultLevelOption(nodeLevelAttribute, 3));
		
		mutator.add(RoundRussellAlgorithm.WEIGHT_COLUMN_ID, 
					edgeAttributes,
					getMatchedKey(edgeAttributes, "weight"));
		
		mutator.add(RoundRussellAlgorithm.NODE_COLOR_COLUMN_ID, 
					nodeColorAttribute,
					getMatchedKey(nodeColorAttribute, "color"));
		
		mutator.add(RoundRussellAlgorithm.NODE_COLOR_RANGE_ID, 
					new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
		
		return mutator.mutate(parameters);
	}
	
	/*
	 * To smartly select the appropriate level column name from the drop down box.
	 */
	private String getDefaultLevelOption(
			Collection<String> nodeLevelAttributes, int levelIndicator) {
		for (String nodeLevelAttribute : nodeLevelAttributes) {
			String lowerCaseNodeLevelAttributeName =  nodeLevelAttribute.toLowerCase();
			
			if (lowerCaseNodeLevelAttributeName.contains("level") &&
					lowerCaseNodeLevelAttributeName.contains(String.valueOf(levelIndicator))) {
				return nodeLevelAttribute;
			}
	    }
		
		return NO_LEVEL_IDENTIFIER;
	}
	
	/*
	 * To smartly select the appropriate strength or weight column name from
	 *  the drop down box.
	 */
	private String getMatchedKey(Collection<String> attributeNames, String suggestedPattern) {		
		String lowerCaseSuggestedPattern = suggestedPattern.toLowerCase();

		for (String attributeName : attributeNames) {
			String lowerCaseAttributeName = attributeName.toLowerCase();

			if (lowerCaseAttributeName.contains(lowerCaseSuggestedPattern)) {
				return attributeName;
			}
	    }
		
		return null;
	}
}