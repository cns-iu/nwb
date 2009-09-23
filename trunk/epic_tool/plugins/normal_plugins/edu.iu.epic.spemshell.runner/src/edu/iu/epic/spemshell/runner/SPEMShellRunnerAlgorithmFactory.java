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
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/* TODO:
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
 * Also create cfg file (runs=1 for now; outvals only as necessary).
 * Invoke the core static executable with data = the .in file.
 * Catch the output and return it.
 */
public class SPEMShellRunnerAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String MODEL_PARAMETER_PREFIX = "MODEL_PARAMETER_";
	public static final String COMPARTMENT_POPULATION_PREFIX = "COMPARTMENT_POPULATION_";
	
	protected static BundleContext bundleContext;	
	protected void activate(ComponentContext componentContext) {
		SPEMShellRunnerAlgorithmFactory.bundleContext =
			componentContext.getBundleContext();
	}
	
    @SuppressWarnings("unchecked") // TODO
	public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
    	return new SPEMShellRunnerAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		File modelFile = (File) data[0].getData();
		
		BasicObjectClassDefinition newParameters = MutateParameterUtilities.createNewParameters(oldParameters);
		
//		AttributeDefinition[] oldAttributeDefinitions =
//			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
//		
//		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
//			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
//			
//			if(oldAttributeDefinitionID.equals("DUMMY_AD")) {
//				// Don't copy over the dummy; just do nothing.
//			}		
//		}
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition("population", "population", "population", AttributeDefinition.INTEGER, "1000000"));
		
		newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition("days", "days", "days", AttributeDefinition.INTEGER, "730"));
		
		try {
			ModelFileReader modelFileReader =
				new ModelFileReader(modelFile.getPath());
			
			// TODO Catch other initial populations beyond infections, too.
			Collection<String> infectionCompartments =
				modelFileReader.getInfectionCompartments();			
			for (String infectionCompartment : infectionCompartments) {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								COMPARTMENT_POPULATION_PREFIX + infectionCompartment,
								infectionCompartment,
								infectionCompartment,
								AttributeDefinition.INTEGER));
			}			
			
			// TODO Test
			Collection<String> unboundReferencedParameters =
				modelFileReader.findUnboundReferencedParameters();			
			for (String unboundReferencedParameter : unboundReferencedParameters) {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								MODEL_PARAMETER_PREFIX + unboundReferencedParameter,
								unboundReferencedParameter,
								unboundReferencedParameter,
								AttributeDefinition.DOUBLE));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newParameters;		
	}
}