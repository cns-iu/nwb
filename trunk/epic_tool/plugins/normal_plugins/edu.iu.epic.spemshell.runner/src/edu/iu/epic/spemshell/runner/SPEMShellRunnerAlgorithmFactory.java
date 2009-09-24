package edu.iu.epic.spemshell.runner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

import org.antlr.runtime.RecognitionException;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.epic.spemshell.runner.preprocessing.parsing.ModelFileReader;

/* TODO Design plan:
 * Given a model file (file:text/model or whatever)
 * Parse it to find parameters which need to have values set by the user
 * 		Mutate them into ObjectClassDefinition.
 * Also prompt for (and provide reasonable defaults for):
 * 		An initial population
 * 		Initial compartment populations (except susceptible)?
 * 		Number of days
 * 		Initial date
 * 		? Random number seed?
 * Create the appropriate .in file (and .mdl file.. patched to conform with Bruno or our style?)
 * in the default temp directory.  Make sure .in points at .mdl.
 * Also create an infections.txt?  Is this necessary?
 * Invoke the core static executable with data = the .in file.
 * Catch the output and return it.
 */
public class SPEMShellRunnerAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public static final int DEFAULT_NUMBER_OF_DAYS = 730;
	public static final BasicAttributeDefinition NUMBER_OF_DAYS_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
							"days",
							"Number of Days",
							"Length of the simulation (in days)",
							AttributeDefinition.INTEGER,
							String.valueOf(DEFAULT_NUMBER_OF_DAYS));
	
	public static final int DEFAULT_POPULATION = 1000000;
	public static final BasicAttributeDefinition POPULATION_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
							"population",
							"Population",
							"Initial population",
							AttributeDefinition.INTEGER,
							String.valueOf(DEFAULT_POPULATION));
	
	/* Prepended to IDs so that we can recognize that kind of
	 * parameter in the Algorithm (to the exclusion of other kinds).
	 */
	public static final String MODEL_PARAMETER_PREFIX = "MODEL_PARAMETER_";
	public static final String COMPARTMENT_POPULATION_PREFIX =
		"COMPARTMENT_POPULATION_";
	
	private static BundleContext bundleContext;	
	protected void activate(ComponentContext componentContext) {
		SPEMShellRunnerAlgorithmFactory.bundleContext =
			componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
    @SuppressWarnings("unchecked") // TODO
	public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
    	return new SPEMShellRunnerAlgorithm(data, parameters, context);
    }

    /* Add algorithm parameters:
     * - That always belong (like the initial total population or number of days).
     * - For the initial population of each compartment declared in the model file. // TODO Currently only infections
     * - For each model parameter that is needed and not already specified in the model file.
     */
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		File modelFile = (File) data[0].getData();
		
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				POPULATION_ATTRIBUTE_DEFINITION);
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				NUMBER_OF_DAYS_ATTRIBUTE_DEFINITION);
		
		try {
			ModelFileReader modelFileReader =
				new ModelFileReader(modelFile.getPath());
			
			// Add a parameter for the initial population of each compartment.
			// TODO Also request initial populations for compartments beyond infections?
			Collection<String> infectionCompartments =
				modelFileReader.getInfectionCompartments();			
			for (String infectionCompartment : infectionCompartments) {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								COMPARTMENT_POPULATION_PREFIX + infectionCompartment,
								createCompartmentPopulationParameterName(
										infectionCompartment),
								createCompartmentPopulationParameterDescription(
										infectionCompartment),
								AttributeDefinition.INTEGER));
			}			
			
			/* Add an algorithm parameter for each model parameter
			 * that is needed and not already specified.
			 */
			Collection<String> unboundReferencedParameters =
				modelFileReader.findUnboundReferencedParameters();			
			for (String unboundReferencedParameter : unboundReferencedParameters) {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								MODEL_PARAMETER_PREFIX + unboundReferencedParameter,
								createModelParametersAlgorithmParameterName(
										unboundReferencedParameter),
								createModelParametersAlgorithmParameterDescription(
										unboundReferencedParameter),
								AttributeDefinition.DOUBLE));
			}
		} catch (IOException e) {
			String message =
				"Error accessing model file to create parameters dialog; " +
				"continuing without custom parameters.";
			
			SPEMShellRunnerAlgorithm.logger.log(
					LogService.LOG_WARNING,
					message,
					e);
		} catch (RecognitionException e) {
			String message =
				"Error parsing model file to create parameters dialog; " +
				"continuing without custom parameters.";
			
			SPEMShellRunnerAlgorithm.logger.log(
					LogService.LOG_WARNING,
					message,
					e);
		}
		
		return newParameters;		
	}
	
	private static String createCompartmentPopulationParameterName(
			String compartmentName) {
		return "Initial population of " + compartmentName;
	}
	
	private static String createCompartmentPopulationParameterDescription(
			String compartmentName) {
		return "The initial population of the " + compartmentName + " compartment";
	}
	
	private static String createModelParametersAlgorithmParameterName(
			String modelParameterName) {
		return "Model parameter \"" + modelParameterName + "\"";
	}
	
	private static String createModelParametersAlgorithmParameterDescription(
			String modelParameterName) {
		return "Numeric value of the " + modelParameterName + " model parameter";
	}
}