package edu.iu.nwb.preprocessing.removeegraphattributes;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.NWBRemovableAttributeReader;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.BooleanMutator;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.DummyAttributeDefinitionRemover;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.BooleanMutator.DescriptionCreator;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public abstract class RemoveGraphAttributesAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public abstract Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context);
	public abstract NWBRemovableAttributeReader createAttributeReader(
			File inNWBFile)
				throws NWBMetadataParsingException;
	public abstract LogService getLogger();
	
	
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		try {
			// Determine the keys of the attributes that can be removed
			File inNWBFile = (File) data[0].getData();
			NWBRemovableAttributeReader reader =
				createAttributeReader(inNWBFile);
			Collection<String> removableAttributeKeys =
				reader.determineRemovableAttributeKeys();

			// Remove the dummy AttributeDefinition from oldParameters
			DummyAttributeDefinitionRemover dummyRemover =
				new DummyAttributeDefinitionRemover();
			ObjectClassDefinition properParameters =
				dummyRemover.removeFrom(oldParameters);
			
			// Add a Boolean parameter (check-box) for each removable attribute		
			BooleanMutator booleanMutator =
				new BooleanMutator(removableAttributeKeys,
								   new RemoveDescriptionCreator());		
			
			return booleanMutator.addOptionsTo(properParameters);
		} catch (NWBMetadataParsingException e) {
			String message =
				"Error while mutating user parameters: " + e.getMessage();
			
			getLogger().log(LogService.LOG_WARNING, message, e);
			
			return oldParameters;
		}
	}
	
	
	
	protected static class RemoveDescriptionCreator
			implements DescriptionCreator {
		public String createFromName(String name) {
			return "Remove the " + name + " attribute?";
		}
	}
}
