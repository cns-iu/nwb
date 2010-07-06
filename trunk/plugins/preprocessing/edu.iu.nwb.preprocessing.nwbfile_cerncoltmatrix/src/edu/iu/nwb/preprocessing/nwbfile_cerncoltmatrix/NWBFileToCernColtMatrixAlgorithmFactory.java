package edu.iu.nwb.preprocessing.nwbfile_cerncoltmatrix;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class NWBFileToCernColtMatrixAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public static final Collection<String> DEFAULT_NUMBER_KEYS_TO_ADD =
		Arrays.asList("unweighted");
	
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new NWBFileToCernColtMatrixAlgorithm(data, parameters, context);
    }
    
    // Mutate to find all  
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	Data inData = data[0];
    	File inputNWBFile = (File)inData.getData();
    	
    	// TODO: Create empty parameters, dude!
    	BasicObjectClassDefinition newParameters =
    		MutateParameterUtilities.createNewParameters(oldParameters);
    	AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
    	
    	Map<String, String> edgeMetadata;

    	try {
    		edgeMetadata = NWBFileUtilities.getEdgeMetadata(inputNWBFile);
    	} catch (NWBMetadataParsingException nwbMetadataParsingException) {
    		throw new RuntimeException(nwbMetadataParsingException);
    	}

    	for (int ii = 0; ii < oldAttributeDefinitions.length; ii++) {
    		AttributeDefinition oldAttributeDefinition =
    			oldAttributeDefinitions[ii];
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition =
				oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals(
					NWBFileToCernColtMatrixAlgorithm.WEIGHT_FIELD_ID)) {
				newAttributeDefinition =
					MutateParameterUtilities.formAttributeDefinitionFromMap(
						oldAttributeDefinition,
						edgeMetadata,
						NWBFileUtilities.DEFAULT_NUMBER_KEY_TYPES,
						NWBFileUtilities.DEFAULT_NUMBER_KEYS_TO_SKIP,
						DEFAULT_NUMBER_KEYS_TO_ADD);
			}
			
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
    	
    	return newParameters;
    }
}