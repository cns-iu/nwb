package edu.iu.epic.simulator.runner.stochastic;

import java.util.Random;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;

public abstract class BatchRunnerAlgorithmFactory extends EpidemicSimulatorAlgorithmFactory {
	public static final String NUMBER_OF_RUNS_PARAMETER_ID = "numberOfRuns";
	public static final int DEFAULT_NUMBER_OF_RUNS = 1;
	
	public static final String SEED_PARAMETER_ID = "seed";	
	
	
	@Override
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition ocd =
			(BasicObjectClassDefinition) super.mutateParameters(data, oldParameters);
		
		ocd.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(
						NUMBER_OF_RUNS_PARAMETER_ID,
						"Number of runs",
						"Number of times to run the simulation.",
						AttributeDefinition.INTEGER,
						String.valueOf(DEFAULT_NUMBER_OF_RUNS)));
		
		final String seedDescription =
			"The seed value for the pseudo-random number generator.  "
			+ "If you would like to reproduce results from an earlier run, "
			+ "use the same seed, otherwise use the given default.";
		ocd.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(
						SEED_PARAMETER_ID,
						"Random number generator seed",
						seedDescription,
						AttributeDefinition.INTEGER,
						String.valueOf((new Random()).nextInt())));
		
		return ocd;
	}
}
