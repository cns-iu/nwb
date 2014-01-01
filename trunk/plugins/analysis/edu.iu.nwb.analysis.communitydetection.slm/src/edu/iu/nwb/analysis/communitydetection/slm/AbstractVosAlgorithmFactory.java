package edu.iu.nwb.analysis.communitydetection.slm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public abstract class AbstractVosAlgorithmFactory 
		implements AlgorithmFactory, ParameterMutator {
    
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

    	LinkedHashMap<String, String> edgeSchema = getEdgeSchema(nwbFileMetaDataGetter);
    	BasicObjectClassDefinition newParameters =
    		MutateParameterUtilities.createNewParameters(oldParameters);
		AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		Collection<String> numberKeysTypes = Arrays.asList(NWBFileProperty.TYPE_INT, NWBFileProperty.TYPE_FLOAT);
		Collection<String> numberKeysToSkip = Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.ATTRIBUTE_TARGET);
		Collection<String> numberKeysToAdd = Arrays.asList(
				AbstractVosAlgorithm.NO_EDGE_WEIGHT_VALUE);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
			if (oldAttributeDefinitionID.equals(
					AbstractVosAlgorithm.WEIGHT_FIELD_ID))
			{
				newAttributeDefinition = MutateParameterUtilities.formAttributeDefinitionFromMap(
					oldAttributeDefinition,
					edgeSchema,
					numberKeysTypes,
					numberKeysToSkip,
					numberKeysToAdd);
			}
			
			if (oldAttributeDefinitionID.equals(
					AbstractVosAlgorithm.MODULARITY_FUNCTION_FIELD_ID))
			{
				String[] labels = AbstractVosAlgorithm.MODULARITY_FUCNTION_MAP.keySet().toArray(new String[]{});
				newAttributeDefinition = new BasicAttributeDefinition(
						oldAttributeDefinition.getID(), 
						oldAttributeDefinition.getName(), 
						oldAttributeDefinition.getDescription(), 
						oldAttributeDefinition.getType(), 
						labels, 
						labels);
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