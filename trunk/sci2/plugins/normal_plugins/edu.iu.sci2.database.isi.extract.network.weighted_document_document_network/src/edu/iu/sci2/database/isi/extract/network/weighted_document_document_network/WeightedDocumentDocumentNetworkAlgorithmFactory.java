package edu.iu.sci2.database.isi.extract.network.weighted_document_document_network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

public class WeightedDocumentDocumentNetworkAlgorithmFactory implements
		AlgorithmFactory, ParameterMutator {
	public static final String THRESHOLD_FIELD_ID = "threshold";
	public static final String COMPARISON_ALGORITHM_ID = "comparison_algorithm";

	@Override
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Object thresholdParameterValue = parameters.get(THRESHOLD_FIELD_ID);
		
		if (!(thresholdParameterValue instanceof Float)) {
			throw new AlgorithmCreationFailedException(
					"The threshold value provided must be of type '"
							+ Float.class.getName() + "' but was of type '"
							+ thresholdParameterValue.getClass().getName() + "'.");
		}

		float threshold = ((Float) thresholdParameterValue).floatValue();

		Object comparisonAlgorithmParameterValue = parameters
				.get(COMPARISON_ALGORITHM_ID);
		if (!(comparisonAlgorithmParameterValue instanceof String)) {
			throw new AlgorithmCreationFailedException(
					"The comparison algorithm value provided must be of type '"
							+ String.class.getName()
							+ "' but was of type '"
							+ comparisonAlgorithmParameterValue.getClass()
									.getName() + "'.");
		}

		ComparisonAlgorithm comparisonAlgorithm = ComparisonAlgorithm
				.valueOf((String) comparisonAlgorithmParameterValue);
		
		return new WeightedDocumentDocumentNetworkAlgorithm(data, threshold,
				(LogService) ciShellContext.getService(LogService.class
						.getName()), comparisonAlgorithm);
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		
		BasicObjectClassDefinition newParameters = MutateParameterUtilities
				.createNewParameters(oldParameters);

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (oldAttributeDefinitionID.equals(COMPARISON_ALGORITHM_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.cloneToDropdownAttributeDefinition(
								oldAttributeDefinition,
								formComparisonAlgorithmLabels(),
								formComparisonAlgorithmValues());
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
	
	private static Collection<String> formComparisonAlgorithmLabels() {
		Collection<String> orientationLabels = new ArrayList<String>(
				ComparisonAlgorithm.values().length);
		for (ComparisonAlgorithm algorithm : ComparisonAlgorithm.values()) {
			orientationLabels.add(algorithm.toString());
		}
		return orientationLabels;
	}

	private static Collection<String> formComparisonAlgorithmValues() {
		Collection<String> orientationValues = new ArrayList<String>(
				ComparisonAlgorithm.values().length);
		for (ComparisonAlgorithm algorithm : ComparisonAlgorithm.values()) {
			orientationValues.add(algorithm.toString());
		}
		return orientationValues;
	}
}