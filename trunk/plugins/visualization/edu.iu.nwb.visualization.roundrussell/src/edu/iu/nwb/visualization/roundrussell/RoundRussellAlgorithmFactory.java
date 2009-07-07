package edu.iu.nwb.visualization.roundrussell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
								edgeAttributes, 
								edgeAttributes));
			}  
			else if(id.equals("strengthcolumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								nodeStrengthAttribute, 
								nodeStrengthAttribute));
			}
			else if(id.equals("level1_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level2_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level3_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
								nodeLevelAttribute, 
								nodeLevelAttribute));
			}
			else if(id.equals("level4_column")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(attributeDefinitions[ii].getID(), 
								attributeDefinitions[ii].getName(), 
								attributeDefinitions[ii].getDescription(), 
								attributeDefinitions[ii].getType(), 
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
			String key = ""+keys.next();
			
				/*
				 * Strength column cannot have the type "String".
				 * */
				 if(!nodeSchema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
					 goodKeys.add(key);
				 }
				
		}
		goodKeys.add(NO_STRENGTH_IDENTIFIER);
		Collections.reverse(goodKeys);
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
		
		/*
		 * In order to skip "node id" options.
		 * */
		Iterator keysSkip = nodeSchema.keySet().iterator();
		keysSkip.next();
		
		for (Iterator keys = keysSkip; keys.hasNext(); ) {
			String key = ""+keys.next();
				goodKeys.add(key);
		}
		
		goodKeys.add(NO_LEVEL_IDENTIFIER);
		Collections.reverse(goodKeys);
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
			String key = ""+keys.next();
			if (!schema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
				goodKeys.add(key);
			}
		}
		goodKeys.add(NO_EDGE_WEIGHT_IDENTIFIER);
		Collections.reverse(goodKeys);
		return (String[]) goodKeys.toArray(new String[]{});
	}
}