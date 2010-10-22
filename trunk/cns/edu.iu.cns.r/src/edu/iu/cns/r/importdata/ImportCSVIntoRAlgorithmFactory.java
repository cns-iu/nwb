package edu.iu.cns.r.importdata;

import java.io.File;
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

public class ImportCSVIntoRAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String VARIABLE_NAME_IN_R_ID = "variableNameInR";
	public static final String VARIABLE_NAME_IN_R_DEFAULT = "importedTable";

	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	String variableNameInR = (String) parameters.get(VARIABLE_NAME_IN_R_ID);
    	RInstance rInstance = findRInstance(data);
    	File tableToImport = findTableToImport(data);

        return new ImportCSVIntoRAlgorithm(variableNameInR, rInstance, tableToImport, this.logger);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		try {
			RInstance rInstance = findRInstance(data);
			Set<String> allRObjectNames = rInstance.getAllObjectNamesFromR();
			String defaultTableName =
				determineValidDefaultVariableName(rInstance, allRObjectNames);
			BasicObjectClassDefinition newParameters =
				MutateParameterUtilities.createNewParameters(oldParameters);
			processAttributeDefinitions(
				oldParameters,
				newParameters,
				ObjectClassDefinition.REQUIRED,
				defaultTableName,
				allRObjectNames);
			processAttributeDefinitions(
				oldParameters,
				newParameters,
				ObjectClassDefinition.OPTIONAL,
				defaultTableName,
				allRObjectNames);

			return newParameters;
		} catch (IOException e) {
			throw new AlgorithmCreationFailedException(e.getMessage(), e);
		}
	}

	private RInstance findRInstance(Data[] data) {
		if (data[0].getData() instanceof RInstance) {
			return (RInstance) data[0].getData();
		} else {
			return (RInstance) data[1].getData();
		}
	}

	private File findTableToImport(Data[] data) {
		if (data[0].getData() instanceof RInstance) {
			return (File) data[1].getData();
		} else {
			return (File) data[0].getData();
		}
	}

	private String determineValidDefaultVariableName(
			RInstance rInstance, Set<String> allRObjectNames) throws IOException {
		String defaultVariableName = VARIABLE_NAME_IN_R_DEFAULT;
		int uniqueVariableNameCounter = 2;

		while (true) {
			if (allRObjectNames.contains(defaultVariableName)) {
				defaultVariableName = VARIABLE_NAME_IN_R_DEFAULT + uniqueVariableNameCounter;
				uniqueVariableNameCounter++;
			} else {
				return defaultVariableName;
			}
		}
	}

	private void processAttributeDefinitions(
			ObjectClassDefinition oldParameters,
			BasicObjectClassDefinition newParameters,
			int attributeDefinitionType,
			String defaultTableName,
			final Set<String> allRObjectNames) {

		for (AttributeDefinition oldAttributeDefinition :
				oldParameters.getAttributeDefinitions(attributeDefinitionType)) {
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (VARIABLE_NAME_IN_R_ID.equals(oldAttributeDefinition.getID())) {

				newAttributeDefinition = new BasicAttributeDefinition(
    					oldAttributeDefinition.getID(),
    					oldAttributeDefinition.getName(),
    					oldAttributeDefinition.getDescription(),
    					oldAttributeDefinition.getType(),
    					defaultTableName) {
    				@Override
    				public String validate(String value) {
    					String preValidation = super.validate(value);

    					if (preValidation != null) {
    						return preValidation;
    					} else {
    						if (allRObjectNames.contains(value)) {
    							String format =
    								"%s is already declared in this R instance.  " +
    								"Please choose a different variable name.";
    							String errorMessage = String.format(format, value);

    							return errorMessage;
    						}
    						// TODO: Check if value is also a valid R variable name.
    						else {
    							return null;
    						}
    					}
    				}
    			};
			}

			newParameters.addAttributeDefinition(attributeDefinitionType, newAttributeDefinition);
		}
	}
}