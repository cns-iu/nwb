package edu.iu.cns.r.exportdata;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.cns.r.utility.RInstance;

public class ExportCSVFromRAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String VARIABLE_NAME_IN_R_ID = "variableNameInR";

	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

	public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
    	String variableNameInR = (String) parameters.get(VARIABLE_NAME_IN_R_ID);
    	RInstance rInstance = (RInstance) inputData.getData();

        return new ExportCSVFromRAlgorithm(inputData, variableNameInR, rInstance, this.logger);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		try {
			RInstance rInstance = (RInstance) data[0].getData();
			Set<String> allRObjectNames = rInstance.getAllObjectNamesFromR();
			BasicObjectClassDefinition newParameters =
				MutateParameterUtilities.createNewParameters(oldParameters);
			processAttributeDefinitions(
				oldParameters,
				newParameters,
				ObjectClassDefinition.REQUIRED,
				allRObjectNames);
			processAttributeDefinitions(
				oldParameters,
				newParameters,
				ObjectClassDefinition.OPTIONAL,
				allRObjectNames);

			return newParameters;
		} catch (IOException e) {
			throw new AlgorithmCreationFailedException(e.getMessage(), e);
		}
	}

	private void processAttributeDefinitions(
			ObjectClassDefinition oldParameters,
			BasicObjectClassDefinition newParameters,
			int attributeDefinitionType,
			final Set<String> allRObjectNames) {

		for (AttributeDefinition oldAttributeDefinition :
				oldParameters.getAttributeDefinitions(attributeDefinitionType)) {
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (VARIABLE_NAME_IN_R_ID.equals(oldAttributeDefinition.getID())) {
				String[] options = allRObjectNames.toArray(new String[0]);

				newAttributeDefinition = new BasicAttributeDefinition(
    					oldAttributeDefinition.getID(),
    					oldAttributeDefinition.getName(),
    					oldAttributeDefinition.getDescription(),
    					oldAttributeDefinition.getType(),
    					options,
    					options);
			}

			newParameters.addAttributeDefinition(attributeDefinitionType, newAttributeDefinition);
		}
	}
}