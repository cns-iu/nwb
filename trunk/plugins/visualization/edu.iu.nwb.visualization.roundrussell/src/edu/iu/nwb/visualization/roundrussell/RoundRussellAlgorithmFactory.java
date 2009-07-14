package edu.iu.nwb.visualization.roundrussell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * @author cdtank
 *
 */

public class RoundRussellAlgorithmFactory implements AlgorithmFactory, ParameterMutator  {
	
	public final static String NO_EDGE_WEIGHT_IDENTIFIER = "Unweighted";
	public final static String NO_STRENGTH_IDENTIFIER = "No Strength";
	public final static String NO_LEVEL_IDENTIFIER = "No Level";
	
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
		
		
		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), parameters.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), null);
		}
		
		String[] edgeAttributes;
		
		String[] nodeStrengthAttribute, nodeLevelAttribute;
		nodeStrengthAttribute = createNodeStrengthAttributeKeys(metaDataHandler.getNodeSchema());
		nodeLevelAttribute = createNodeLevelAttributeKeys(metaDataHandler.getNodeSchema());
		
		if(metaDataHandler.getDirectedEdgeSchema() != null){
			edgeAttributes = createEdgeAttributeKeyArray(metaDataHandler.getDirectedEdgeSchema());	
		}
		else {
			edgeAttributes = createEdgeAttributeKeyArray(metaDataHandler.getUndirectedEdgeSchema());
		}

		AttributeDefinition[] attributeDefinitions = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < attributeDefinitions.length; ii++) {
			String id = attributeDefinitions[ii].getID();
			
			if(id.equals("weightcolumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(),
								0, getDefaultOption(edgeAttributes, "weight"),
								null,
								edgeAttributes, 
								edgeAttributes));
			}  
			else if(id.equals("strengthcolumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(),
								0, getDefaultOption(nodeStrengthAttribute, "strength"),
								null,
								nodeStrengthAttribute, 
								nodeStrengthAttribute));
			}
			else if(id.equals("level0_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(),
								0, getDefaultLevelOption(nodeLevelAttribute, 0),
								null,
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level1_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								0, getDefaultLevelOption(nodeLevelAttribute, 1),
								null,
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level2_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								0, getDefaultLevelOption(nodeLevelAttribute, 2),
								null,
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level3_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(),
								0, getDefaultLevelOption(nodeLevelAttribute, 3),
								null,
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("betacolumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new EdgeBundlingDegreeAttributeValueValidator(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(),
								attributeDefinitions[ii].getDefaultValue().toString()));
			}
			else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attributeDefinitions[ii]);
			}
		}

		return definition;
	}
	
	/*
	 * To smartly select the appropriate level column name from the drop down box.
	 * */
	private String[] getDefaultLevelOption(String[] nodeLevelAttribute, int levelIndicator) {
		
		for(int attributesIndex = 0; 
				attributesIndex < nodeLevelAttribute.length; 
				attributesIndex++ ) {
			
			String lowerCaseNodeLevelAttributeName = nodeLevelAttribute[attributesIndex].toLowerCase();
			if(lowerCaseNodeLevelAttributeName.contains("level") 
				&& lowerCaseNodeLevelAttributeName.contains(String.valueOf(levelIndicator))) {
				return new String[] { nodeLevelAttribute[attributesIndex] };
			}
	    }
		
		for(int attributesIndex = 0; attributesIndex < nodeLevelAttribute.length; attributesIndex++ ) {
			String lowerCaseNodeLevelAttributeName = nodeLevelAttribute[attributesIndex].toLowerCase();
			if(lowerCaseNodeLevelAttributeName.contains("level")) {
				return new String[] { nodeLevelAttribute[attributesIndex] }; 
			}
	    }
		return null;
	}
	
	/*
	 * To smartly select the appropriate strength or weight column name from
	 *  the drop down box.
	 */
	private String[] getDefaultOption(String[] attributeNames,
									  String suggestedPattern) {
		
		String lowerCaseSuggestedPattern = suggestedPattern.toLowerCase();
		
		for(int attributesIndex = 0;
				attributesIndex < attributeNames.length;
				attributesIndex++ ) {
			String lowerCaseAttributeName =
				attributeNames[attributesIndex].toLowerCase();
			if(lowerCaseAttributeName.contains(lowerCaseSuggestedPattern)) {
				return new String[] { attributeNames[attributesIndex] };
			}
	    }
		return null;
	}

	/**
	 * Used to create strength key array from the node attribute columns from the selected nwb file, 
	 * except the node id column & the columns with "string" type.
	 */
	private String[] createNodeStrengthAttributeKeys(LinkedHashMap nodeSchema) {
		List<String> goodKeys = new ArrayList<String>();
		
		/*
		 * In order to skip "node id" options.
		 * */
		Iterator keysSkip = nodeSchema.keySet().iterator();
		keysSkip.next();
		
		for (Iterator keys = keysSkip; keys.hasNext(); ) {
			String key = keys.next().toString();
			
				/*
				 * Strength column cannot have the type "String".
				 * */
				 if(!nodeSchema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
					 goodKeys.add(key);
				 }
				
		}
		goodKeys.add(NO_STRENGTH_IDENTIFIER);
		return (String[]) goodKeys.toArray(new String[]{});
	}

	/**
	 * Used to create level key array from the node attribute columns from the selected nwb file, 
	 * except the node id column.
	 * @param nodeSchema
	 * @return
	 */
	private String[] createNodeLevelAttributeKeys(LinkedHashMap nodeSchema) {
		List<String> goodKeys = new ArrayList<String>();
		goodKeys.add(NO_LEVEL_IDENTIFIER);
		
		/*
		 * In order to skip "node id" options.
		 * */
		Iterator keysSkip = nodeSchema.keySet().iterator();
		keysSkip.next();
		
		for (Iterator keys = keysSkip; keys.hasNext(); ) {
			String key = keys.next().toString();
				goodKeys.add(key);
		}
		
		return (String[]) goodKeys.toArray(new String[]{});
	}

	private String[] createEdgeAttributeKeyArray(Map schema) {
		
		List goodKeys = new ArrayList();
		
		/*
		 * In order to skip "source" & "target" options for the edge weight column.
		 * */
		Iterator keysSkip = schema.keySet().iterator();
		keysSkip.next();
		keysSkip.next();
		
		for (Iterator keys = keysSkip; keys.hasNext(); ) {
			String key = keys.next().toString();
			if (!schema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
				goodKeys.add(key);
			}
		}
		goodKeys.add(NO_EDGE_WEIGHT_IDENTIFIER);
		return (String[]) goodKeys.toArray(new String[]{});
	}
}