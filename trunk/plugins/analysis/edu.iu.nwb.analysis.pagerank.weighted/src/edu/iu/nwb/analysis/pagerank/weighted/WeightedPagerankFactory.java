package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetMetadataAndCounts;
import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class WeightedPagerankFactory implements AlgorithmFactory, DataValidator, ParameterMutator {
	protected static final String TREAT_WEIGHT_AS_ONE = "Treat all edges as weight one.";
	/**
	 * The id of the Damping Factor {@link AttributeDefinition} from the
	 * METADATA.XML
	 */
	protected static final String DAMPENING_FACTOR_ID = "d";

	/**
	 * The id of the Weight {@link AttributeDefinition} that will be inserted
	 * into the algorithm parameters.
	 */
	protected static final String WEIGHT_ID = "weightAttribute";
	
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext context) {
		return new WeightedPagerank(data, parameters, context);
	}
    
	@Override
    public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		GetMetadataAndCounts networkInfo = new GetMetadataAndCounts();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(networkInfo);
		} catch (IOException e) {
			return "Unable to read NWB file.";
		} catch (ParsingException e) {
			return "Invalid NWB file format.";
		}
		
		int numberOfDirectedEdges = networkInfo.getDirectedEdgeCount();
		
		if(networkInfo.getUndirectedEdgeCount() > 0) {
			if(numberOfDirectedEdges > 0) {
				return "This network has both directed and undirected edges. The algorithm only works on entirely directed networks.";
			}
			return "This network is undirected. The algorithm only works on directed networks.";
		} else if(numberOfDirectedEdges == 0) {
			return "This network has no edges. The algorithm requires edges to work.";
		}

		return "";
	}
	
	/**
	 * Generate a list of possible weight columns adding in a default to treat
	 * all edges as weight = 1
	 * 
	 * @param schema
	 *            A mapping from the NWB property name to the NWB property type.
	 * @return An array of {@link String}s of NWB property names whose entries would be
	 *         valid types for weight.
	 */
	private static String[] findLikelyWeightColumns(Map<String, String> schema) {
		List<String> goodKeys = new ArrayList<String>();
		
		for (Entry<String, String> entry : schema.entrySet()) {
			if (NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.contains(entry.getValue())
					&& !NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES.containsKey(entry.getKey())) {
				goodKeys.add(entry.getKey());
			}
		}
		
		goodKeys.add(TREAT_WEIGHT_AS_ONE);
		
		Collections.reverse(goodKeys);
		
		return goodKeys.toArray(new String[goodKeys.size()]);
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
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

		String[] edgeAttributesArray = findLikelyWeightColumns(handler.getDirectedEdgeSchema());

		AttributeDefinition[] definitions = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(WEIGHT_ID, 
						"Weight Attribute", 
						"The attribute to use for weight", 
						AttributeDefinition.STRING, 
						edgeAttributesArray, 
						edgeAttributesArray));
		
		for(AttributeDefinition attributeDefinition : definitions) {
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attributeDefinition);
		}

		return definition;
	}
}