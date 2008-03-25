package edu.iu.nwb.visualization.drl;

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
import org.osgi.framework.BundleContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;


public class VxOrdAlgorithmFactory implements AlgorithmFactory, DataValidator, ParameterMutator {
    private BundleContext bContext;
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new VxOrdAlgorithm(data, parameters, context, bContext);
    }
    
	public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			return "Invalid nwb file";
		} catch (ParsingException e) {
			return "Invalid nwb file";
		}
		
		if (handler.getUndirectedEdgeSchema() != null) {
			return "";
		} else {
			return "DrL can only process undirected networks."; 
		}
	}
    
	
	private String[] createKeyArray(Map schema) {
		List goodkeys = new ArrayList();
		
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = ""+keys.next();
			if (!schema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
				goodkeys.add(key);
			}
		}
		
		Collections.reverse(goodkeys);
		
		return (String[]) goodkeys.toArray(new String[]{});
	}
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
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

		String[] edgeAttributesArray = createKeyArray(handler.getUndirectedEdgeSchema());

		AttributeDefinition[] definitions = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			if(id.equals("edgeWeight")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(definitions[ii].getID(), 
								definitions[ii].getName(), 
								definitions[ii].getDescription(), 
								definitions[ii].getType(), 
								edgeAttributesArray, 
								edgeAttributesArray));
			}  else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		return definition;
	}
}