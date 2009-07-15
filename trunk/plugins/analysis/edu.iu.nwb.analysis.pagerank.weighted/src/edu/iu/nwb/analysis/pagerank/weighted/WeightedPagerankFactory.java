package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	protected static final String DEFAULT_WEIGHT = "Treat all edges as weight one.";
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new WeightedPagerank(data, parameters, context);
    }
    
    public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		GetMetadataAndCounts networkInfo = new GetMetadataAndCounts();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(networkInfo);
		} catch (IOException e1) {
			return "Unable to read NWB file.";
		} catch (ParsingException e) {
			return "Invalid NWB file format.";
		}
		
		int numberOfDirectedEdges = networkInfo.getDirectedEdgeCount();
		
		if(networkInfo.getUndirectedEdgeCount() > 0) {
			if(numberOfDirectedEdges > 0) {
				return "This network has both directed and undirected edges. The algorithm only works on entirely directed networks.";
			} else {
				return "This network is undirected. The algorithm only works on directed networks.";
			}
		} else if(numberOfDirectedEdges == 0) {
			return "This network has no edges. The algorithm requires edges to work.";
		}
		
		
		return "";
	}
	
	private String[] createKeyArray(Map<String, String> schema) {
		List<String> goodkeys = new ArrayList<String>();
		
		for (Iterator<String> keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = keys.next();
			if (!schema.get(key).equals(NWBFileProperty.TYPE_STRING) &&
				!"source".equals(key) &&
				!"target".equals(key)) {
				goodkeys.add(key);
			}
		}
		
		goodkeys.add(DEFAULT_WEIGHT);
		
		Collections.reverse(goodkeys);
		
		return (String[]) goodkeys.toArray(new String[]{});
	}

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

		String[] edgeAttributesArray = createKeyArray(handler.getDirectedEdgeSchema());

		AttributeDefinition[] definitions = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition("weightAttribute", 
						"Weight Attribute", 
						"The attribute to use for weight", 
						AttributeDefinition.STRING, 
						edgeAttributesArray, 
						edgeAttributesArray));
		
		
		for(int ii = 0; ii < definitions.length; ii++) {
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
		}

		return definition;
	}
}