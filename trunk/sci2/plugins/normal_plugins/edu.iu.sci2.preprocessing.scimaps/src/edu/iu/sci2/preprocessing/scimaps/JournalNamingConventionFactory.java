package edu.iu.sci2.preprocessing.scimaps;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class JournalNamingConventionFactory implements AlgorithmFactory, ParameterMutator {
	public static final String JOURNAL_FIELD_ID = "journal";
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary<String, Object> parameters,
    								 CIShellContext ciShellContext) {
		
        return new JournalNamingConvention(data, parameters, ciShellContext);
    }
    
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inputData = data[0];
		Table table = (Table) inputData.getData();

		BasicObjectClassDefinition newParameters = MutateParameterUtilities
				.createNewParameters(oldParameters);

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (oldAttributeDefinitionID.equals(JOURNAL_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition, table);
			} 

			/*
			 * This can take optional ADs and mutate them needlessly into
			 * required ones, so be careful.
			 */
			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}

		return newParameters;
	}
}