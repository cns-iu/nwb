package edu.iu.nwb.templates.staticexecutable.nwb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;


public class HelperFactory implements AlgorithmFactory, DataValidator, ParameterMutator {
	protected static final String DEFAULT_WEIGHT = "Treat all edges as weight one.";
	private ComponentContext componentContext;

    protected void activate(ComponentContext componentContext) {
    	this.componentContext = componentContext;
    }

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new Helper(data, parameters, ciShellContext, componentContext);
    }

	public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			return "Unable to read NWB file.";
		} catch (ParsingException e) {
			return "Invalid NWB file format.";
		}
		return "";
	}
	
	private String[] createKeyArray(Map<String, String> schema) {
		List<String> goodkeys = new ArrayList<String>();

		for (String key : schema.keySet()) {
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

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata handler = new GetNWBFileMetadata();
		
		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(handler);
		} catch (IOException e1) {
			return oldParameters;
		} catch (ParsingException e) {
			return oldParameters;
		}

		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		LinkedHashMap<String, String> undirectedEdgeSchema = handler.getUndirectedEdgeSchema();
		System.err.println("undirectedEdgeSchema: " + undirectedEdgeSchema);

		if ((undirectedEdgeSchema == null) || (undirectedEdgeSchema.keySet().size() == 0)) {
			Dictionary<?, ?> properties = this.componentContext.getProperties();
			Object labelObject = properties.get("label");

			if (labelObject == null) {
				labelObject = properties.get("service.pid");

				if (labelObject == null) {
					labelObject = "This algorithm";
				}
			}

			String label = labelObject.toString();

			String exceptionMessage =
				String.format("%s expects undirected edges, but none were found.", label);
			throw new AlgorithmCreationFailedException(exceptionMessage);
		}

		String[] edgeAttributesArray = createKeyArray(undirectedEdgeSchema);
		AttributeDefinition[] definitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		newParameters.addAttributeDefinition(
			ObjectClassDefinition.REQUIRED,
			new BasicAttributeDefinition(
				"weightAttribute", 
				"Weight Attribute", 
				"The attribute to use for weight", 
				AttributeDefinition.STRING, 
				edgeAttributesArray, 
				edgeAttributesArray));

		for (AttributeDefinition definition : definitions) {
			if (!"DROPALWAYS".equals(definition.getName())) {
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, definition);
			}
		}

		return newParameters;
	}
}