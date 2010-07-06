package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class BlondelCommunityDetectionAlgorithmFactory implements
		AlgorithmFactory, ParameterMutator {
	private BundleContext bundleContext;
	
	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}
	
    @SuppressWarnings("unchecked")	// Dictionary<String, Object>
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
    	AlgorithmFactory blondelExecutableAlgorithmFactory =
    		AlgorithmUtilities.getAlgorithmFactoryByPID(
    			"edu.iu.nwb.shared.blondelexecutable", this.bundleContext);
    	
        return new BlondelCommunityDetectionAlgorithm(
        	blondelExecutableAlgorithmFactory, data, parameters, context);
    }
    
    @SuppressWarnings("unchecked")	// LinkedHashMap<String, String>
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	Data inData = data[0];
    	File inputNWBFile = (File)inData.getData();
    	
    	// Get the NWB metadata.
    	GetNWBFileMetadata nwbFileMetaDataGetter = new GetNWBFileMetadata();
    	NWBFileParser nwbParser;
    	
    	try {
    		nwbParser = new NWBFileParser(inputNWBFile);
    		nwbParser.parse(nwbFileMetaDataGetter);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	} catch (ParsingException e) {
    		throw new RuntimeException(e);
    	}

    	LinkedHashMap edgeSchema = getEdgeSchema(nwbFileMetaDataGetter);
    	BasicObjectClassDefinition newParameters =
    		MutateParameterUtilities.createNewParameters(oldParameters);
		AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		Collection<String> numberKeysTypes = Arrays.asList(
			NWBFileProperty.TYPE_INT, NWBFileProperty.TYPE_FLOAT);
		Collection<String> numberKeysToSkip = Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.ATTRIBUTE_TARGET);
		Collection<String> numberKeysToAdd = Arrays.asList(
			BlondelCommunityDetectionAlgorithm.NO_EDGE_WEIGHT_VALUE);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals(
					BlondelCommunityDetectionAlgorithm.WEIGHT_FIELD_ID))
			{
				newAttributeDefinition = MutateParameterUtilities.formAttributeDefinitionFromMap(
					oldAttributeDefinition,
					edgeSchema,
					numberKeysTypes,
					numberKeysToSkip,
					numberKeysToAdd);
			}
			
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
		
		return newParameters;
    }

    private LinkedHashMap<String, String> getEdgeSchema(GetNWBFileMetadata nwbFileMetaDataGetter) {
    	LinkedHashMap<String, String> directedEdgeSchema =
    		nwbFileMetaDataGetter.getDirectedEdgeSchema();

    	if (directedEdgeSchema != null) {
    		return directedEdgeSchema;
    	} else {
    		return nwbFileMetaDataGetter.getUndirectedEdgeSchema();
    	}
    }
}