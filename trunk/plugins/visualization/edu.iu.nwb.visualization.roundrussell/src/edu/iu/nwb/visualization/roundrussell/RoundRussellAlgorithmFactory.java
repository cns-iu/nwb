package edu.iu.nwb.visualization.roundrussell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.mutateParameter.DropdownMutator;
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
	
	private static URL postScriptHeaderPath;
	private static final String postScriptHeaderFilePath = "postScriptHeader.ps";

	protected void activate(ComponentContext ctxt) {
    	BundleContext bContext = ctxt.getBundleContext();
    	
    	this.postScriptHeaderPath = bContext.getBundle().getResource(postScriptHeaderFilePath);
    	PostScriptOperations.setPostScriptHeaderFile(postScriptHeaderPath);
    	
    }
	
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new RoundRussellAlgorithm(data, parameters, context);
	}
	
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
		
		String[] nodeStrengthAttribute =
			MapUtilities.getValidKeysOfTypesInMap(
					metaDataHandler.getNodeSchema(),
					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
					new String[]{ NO_STRENGTH_IDENTIFIER });
		
		String[] nodeColorAttribute =
			MapUtilities.getValidKeysOfTypesInMap(
					metaDataHandler.getNodeSchema(),
					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
					new String[]{ NO_COLOR_IDENTIFIER });

		String[] nodeLevelAttribute =
			MapUtilities.getValidKeysOfTypesInMap(
					metaDataHandler.getNodeSchema(),
					(String[]) NWBFileProperty.ALL_ATTRIBUTE_TYPES.toArray(new String[0]),
					new String[]{ NWBFileProperty.ATTRIBUTE_ID },
					new String[]{ NO_LEVEL_IDENTIFIER });
		
		
		LinkedHashMap edgeSchema;
		if (metaDataHandler.getDirectedEdgeSchema() != null) {
			edgeSchema  = metaDataHandler.getDirectedEdgeSchema();
		} else {
			edgeSchema = metaDataHandler.getUndirectedEdgeSchema();
		}
		
		String[] edgeAttributes =
			MapUtilities.getValidKeysOfTypesInMap(
					edgeSchema,
					(String[]) NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]),
					new String[]{ NWBFileProperty.ATTRIBUTE_SOURCE, 
								  NWBFileProperty.ATTRIBUTE_TARGET },
					new String[]{ NO_EDGE_WEIGHT_IDENTIFIER });
		
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
	 * */
	private String getDefaultLevelOption(String[] nodeLevelAttribute, int levelIndicator) {
		for (int attributesIndex = 0; 
				attributesIndex < nodeLevelAttribute.length; 
				attributesIndex++) {			
			String lowerCaseNodeLevelAttributeName = 
				nodeLevelAttribute[attributesIndex].toLowerCase();
			
			if (lowerCaseNodeLevelAttributeName.contains("level") 
					&& lowerCaseNodeLevelAttributeName.contains(String.valueOf(levelIndicator))) {
				return nodeLevelAttribute[attributesIndex];
			}
	    }
		
		return NO_LEVEL_IDENTIFIER;
	}
	
	/*
	 * To smartly select the appropriate strength or weight column name from
	 *  the drop down box.
	 */
	private String getMatchedKey(String[] attributeNames,
									  String suggestedPattern) {		
		String lowerCaseSuggestedPattern = suggestedPattern.toLowerCase();
		
		for (int attributesIndex = 0;
				attributesIndex < attributeNames.length;
				attributesIndex++) {
			String lowerCaseAttributeName =
				attributeNames[attributesIndex].toLowerCase();

			if (lowerCaseAttributeName.contains(lowerCaseSuggestedPattern)) {
				return attributeNames[attributesIndex];
			}
	    }
		
		return null;
	}
}