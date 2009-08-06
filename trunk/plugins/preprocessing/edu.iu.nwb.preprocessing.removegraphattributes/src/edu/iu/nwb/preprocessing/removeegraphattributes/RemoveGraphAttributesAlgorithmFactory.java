package edu.iu.nwb.preprocessing.removeegraphattributes;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.RemovableAttributeReader;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.BooleanMutator;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.DummyAttributeDefinitionRemover;
import edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation.BooleanMutator.DescriptionCreator;

public abstract class RemoveGraphAttributesAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public abstract Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context);
	public abstract RemovableAttributeReader getAttributeReader(File inNWBFile)
		throws AlgorithmExecutionException;
	public abstract LogService getLogger();
	
	
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		try {
			// Determine the keys of the attributes that can be removed
			File inNWBFile = (File) data[0].getData();
			RemovableAttributeReader reader = getAttributeReader(inNWBFile);
			Collection removableAttributeKeys =
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
		} catch (AlgorithmExecutionException e) {
			String message =
				"Warning: Unable to mutate parameters due to: " + e.getMessage();
			
			getLogger().log(LogService.LOG_WARNING, message, e);
			
			return oldParameters;
		}
	}
	
	
	
	private class RemoveDescriptionCreator
			implements DescriptionCreator {
		public String createDescriptionFrom(String name) {
			return "Remove attribute " + name + "?";
		}
	}
}
