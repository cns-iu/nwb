package edu.iu.epic.spemshell.runner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Random;
import java.util.Set;

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

public class SPEMShellRunnerAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {	
	public static final String START_DATE_ID = "startDate";
	public static final String NUMBER_OF_DAYS_ID = "days";
	public static final int DEFAULT_NUMBER_OF_DAYS = 730;
	public static final BasicAttributeDefinition NUMBER_OF_DAYS_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
							NUMBER_OF_DAYS_ID,
							"Number of Days",
							"Length of the simulation (in days)",
							AttributeDefinition.INTEGER,
							String.valueOf(DEFAULT_NUMBER_OF_DAYS));
	
	public static final String POPULATION_ID = "population";
	public static final int DEFAULT_POPULATION = 1000000;
	public static final BasicAttributeDefinition POPULATION_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
							POPULATION_ID,
							"Population",
							"Initial population",
							AttributeDefinition.INTEGER,
							String.valueOf(DEFAULT_POPULATION));
	
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	public static final String DEFAULT_START_DATE = "04/01/2007";
	public static final BasicAttributeDefinition START_DATE_ATTRIBUTE_DEFINITION =
		new DateAttributeDefinition(
							START_DATE_ID,
							"Infection start date",
							"First day of the infection(s).",
							AttributeDefinition.STRING,
							DEFAULT_START_DATE,
							DATE_PATTERN);
	
	/* Prepended to IDs so that we can recognize that kind of
	 * parameter in the Algorithm (to the exclusion of other kinds).
	 */
	public static final String MODEL_PARAMETER_PREFIX = "MODEL_PARAMETER_";
	public static final String COMPARTMENT_POPULATION_PREFIX =
		"COMPARTMENT_POPULATION_";
	public static final String INFECTION_PREFIX = "INFECTION_";
	public static final String LATENT_PREFIX = "LATENT_";
	public static final String RECOVERED_PREFIX = "RECOVERED_";
	
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
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				START_DATE_ATTRIBUTE_DEFINITION);
		
		
		final String seedDescription =
			"The seed value for the pseudo-random number generator.  " +
			"If you would like to reproduce results from an earlier run, " +
			"use the same seed, otherwise use the given default.";
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(
						"seed",
						"Random number generator seed",
						seedDescription,
						AttributeDefinition.INTEGER,
						String.valueOf((new Random()).nextInt())));
		
		try {
			ModelFileReader modelFileReader =
				new ModelFileReader(modelFile.getPath());
			
			// Add a parameter for the initial population of each compartment.
			Set<String> infectionCompartments = modelFileReader.getInfectionCompartments();
			for (String compartment : infectionCompartments) {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								COMPARTMENT_POPULATION_PREFIX + INFECTION_PREFIX + compartment,
								createCompartmentPopulationParameterName(
										compartment),
								createCompartmentPopulationParameterDescription(
										compartment),
								AttributeDefinition.INTEGER));
			}
			
//			/* As of version v0.1, asking SPEMShell to take initial populations
//			 * in a latent or recovered compartment appears to have no effect,
//			 * so for now we won't give the illusion of choice.
//			 */
//			// Add a parameter for the initial population of each compartment.
//			Set<String> latentCompartments = modelFileReader.getLatentCompartments();
//			for (String compartment : latentCompartments) {
//				newParameters.addAttributeDefinition(
//						ObjectClassDefinition.REQUIRED,
//						new BasicAttributeDefinition(
//								COMPARTMENT_POPULATION_PREFIX + LATENT_PREFIX + compartment,
//								createCompartmentPopulationParameterName(
//										compartment),
//								createCompartmentPopulationParameterDescription(
//										compartment),
//								AttributeDefinition.INTEGER));
//			}
//			
//			// Add a parameter for the initial population of each compartment.
//			Set<String> recoveredCompartments = modelFileReader.getRecoveredCompartments();
//			for (String compartment : recoveredCompartments) {
//				newParameters.addAttributeDefinition(
//						ObjectClassDefinition.REQUIRED,
//						new BasicAttributeDefinition(
//								COMPARTMENT_POPULATION_PREFIX + RECOVERED_PREFIX + compartment,
//								createCompartmentPopulationParameterName(
//										compartment),
//								createCompartmentPopulationParameterDescription(
//										compartment),
//								AttributeDefinition.INTEGER));
//			}
			
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
	
	
	private static class DateAttributeDefinition
			extends BasicAttributeDefinition {
		private String pattern;
		private SimpleDateFormat dateFormat;		
		
		public DateAttributeDefinition(
				String id,
				String name,
				String description,
				int type,
				String defaultValue,
				String datePattern) {
			super(id, name, description, type, defaultValue);
			
			this.pattern = datePattern;
			this.dateFormat = new SimpleDateFormat(datePattern);
			this.dateFormat.setLenient(true);
		}

		@Override
		public String validate(String value) {
			try {
				this.dateFormat.parse(value);
			} catch (ParseException e) {
				return (
						"Please enter a date in the format \"" + 
						this.pattern + 
						"\".");
			}
			
			return "";
		}
	}
}