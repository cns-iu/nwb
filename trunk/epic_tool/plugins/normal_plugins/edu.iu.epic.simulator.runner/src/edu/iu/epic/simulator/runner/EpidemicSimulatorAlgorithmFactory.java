package edu.iu.epic.simulator.runner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import edu.iu.epic.modeling.compartment.model.Compartment;
import edu.iu.epic.modeling.compartment.model.Compartment.CompartmentNameOrdering;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.InvalidParameterExpressionException;

public abstract class EpidemicSimulatorAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public static final String INITIAL_COMPARTMENT_PARAMETER_ID = "initialCompartment";
	
	// TODO Move into METADATA.xml
	public static final String NUMBER_OF_DAYS_ID = "days";
	public static final int DEFAULT_NUMBER_OF_DAYS = 730;
	public static final BasicAttributeDefinition NUMBER_OF_DAYS_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
				NUMBER_OF_DAYS_ID,
				"Number of days",
				"Length of the simulation (in days)",
				AttributeDefinition.INTEGER,
				String.valueOf(DEFAULT_NUMBER_OF_DAYS));
	
	// TODO Move into METADATA.xml
	public static final String POPULATION_ID = "population";
	public static final int DEFAULT_POPULATION = 1000000;
	public static final BasicAttributeDefinition POPULATION_ATTRIBUTE_DEFINITION =
		new BasicAttributeDefinition(
				POPULATION_ID,
				"Population",
				"Initial population",
				AttributeDefinition.INTEGER,
				String.valueOf(DEFAULT_POPULATION));
	
	// TODO Move into METADATA.xml
	public static final String START_DATE_ID = "startDate";
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
	
	public static final String MODEL_PARAMETER_PREFIX = "MODEL_PARAMETER_";
	
	public static final String INFECTOR_SEED_POPULATION_PREFIX = "INFECTOR_SEED_POPULATION_";
	public static final int DEFAULT_INFECTOR_SEED_POPULATION = 0;
	
	public static final String INITIAL_DISTRIBUTION_PREFIX = "INITIAL_DISTRIBUTION_";
	private static final float DEFAULT_INITIAL_DISTRIBUTION_FRACTION = 0.0f;
	
	
	public abstract Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context);

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Model model = (Model) data[0].getData();
		
		BasicObjectClassDefinition newParameters =
			addInitialCompartmentDropdown(oldParameters, model);
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, NUMBER_OF_DAYS_ATTRIBUTE_DEFINITION);
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,	START_DATE_ATTRIBUTE_DEFINITION);
		
		addInfectorSeedPopulations(model, newParameters);		
	
		addInitialDistributions(model, newParameters);
		
		
		
		/* Add an algorithm parameter for each model parameter
		 * that is needed and not already specified.
		 */
		Collection<String> unboundReferencedParameters;
		try {
			unboundReferencedParameters = model.listUnboundReferencedParameters();
		} catch (InvalidParameterExpressionException e) {
			EpidemicSimulatorAlgorithm.getLogger().log(LogService.LOG_ERROR,
					"Could not create parameters dialog due to error "
					+ "calculating unbound, referenced parameters: "
					+ e.getMessage(),
					e);
			return oldParameters;
		}		
		for (String unboundReferencedParameter
				: Ordering.natural().sortedCopy(unboundReferencedParameters)) {
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
		
		return newParameters;		
	}

	private void addInitialDistributions(Model model, BasicObjectClassDefinition newParameters) {
		// Add a parameter for the initial distribution into each compartment
		for (Compartment compartment
				: CompartmentNameOrdering.BY_COMPARTMENT.sortedCopy(model.getCompartments())) {
			String compartmentName = compartment.getName();
			
			String id =	INITIAL_DISTRIBUTION_PREFIX + compartmentName;
			
			// Weak attempt to default distribution into the susceptible compartment.
			float defaultValue = DEFAULT_INITIAL_DISTRIBUTION_FRACTION;
			if (compartmentName.equalsIgnoreCase("S")) {
				defaultValue = 1.0f;
			}
			
			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
						id,
						createInitialDistributionParameterName(compartmentName),
						createInitialDistributionParameterDescription(compartmentName),
						AttributeDefinition.FLOAT,
						String.valueOf(defaultValue)));
		}
	}

	private void addInfectorSeedPopulations(Model model, BasicObjectClassDefinition newParameters) {
		// Add a parameter for the seed population of each infector compartment.
		for (Compartment infector
				: CompartmentNameOrdering.BY_COMPARTMENT.sortedCopy(
						model.getInfectedCompartments())) {
			String compartmentName = infector.getName();
			
			String id =	INFECTOR_SEED_POPULATION_PREFIX + compartmentName;
			
			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
						id,
						createInfectorSeedPopulationParameterName(compartmentName),
						createInfectorSeedPopulationParameterDescription(compartmentName),
						AttributeDefinition.INTEGER,
						String.valueOf(DEFAULT_INFECTOR_SEED_POPULATION)));
		}
	}

	private BasicObjectClassDefinition addInitialCompartmentDropdown(
			ObjectClassDefinition oldParameters, Model model) {
		Collection<String> compartmentNames = Sets.newTreeSet(CompartmentNameOrdering.BY_NAME);
		compartmentNames.addAll(model.getCompartmentNames());
		
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.mutateToDropdown(
					oldParameters,
					INITIAL_COMPARTMENT_PARAMETER_ID,
					compartmentNames,
					compartmentNames);
		
		return newParameters;
	}

	private String createInitialDistributionParameterName(String compartmentName) {
		return "Initial population distribution to " + compartmentName;
	}

	private String createInitialDistributionParameterDescription(String compartmentName) {
		return "Fraction of the initial population belonging to the " + compartmentName 
		+ " compartment.  The sum of the fractions for each compartment must be exactly 1.0";
	}

	private static String createInfectorSeedPopulationParameterName(String compartmentName) {
		return "Seed population for infector " + compartmentName;
	}

	private static String createInfectorSeedPopulationParameterDescription(String compartmentName) {
		return "The initial population of the " + compartmentName + " infector compartment.";
	}

	private static String createModelParametersAlgorithmParameterName(String modelParameterName) {
		return "Model parameter \"" + modelParameterName + "\"";
	}

	private static String createModelParametersAlgorithmParameterDescription(
			String modelParameterName) {
		return "Numeric value of the " + modelParameterName + " model parameter";
	}

	protected static class DateAttributeDefinition
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
				return ("Please enter a date in the format \"" + this.pattern + "\".");
			}
			
			return "";
		}
	}
}
