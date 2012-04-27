package edu.iu.sci2.reader.googlescholar;

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

public abstract class AbstractCitationAlgorithmFactory 
			implements AlgorithmFactory, ParameterMutator {
	
	protected static final String AUTHOR_FIELD_ID = "author";
	protected static final String DELIMITER_FIELD_ID = "delimiter";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.
	 * cishell.framework.data.Data[], java.util.Dictionary,
	 * org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] data, 
			Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		
		String authorColumnName = parameters.get(AUTHOR_FIELD_ID).toString();
		String delimiter = parameters.get(DELIMITER_FIELD_ID).toString();
		
		return createAlgorithm(data, authorColumnName, delimiter, ciShellContext);
	}
	
	protected abstract Algorithm createAlgorithm(
			Data[] data, String authorColumnName, String delimiter, CIShellContext ciShellContext);
	
	@Override
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

			if (oldAttributeDefinitionID.equals(AUTHOR_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition,
								table);
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
