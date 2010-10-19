package edu.iu.epic.simulator.runner.singlepopulation;

import java.util.Dictionary;
import java.util.Random;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;

public class SinglePopulationSingleRunnerAlgorithmFactory
		extends EpidemicSimulatorAlgorithmFactory {
	public static final String SEED_PARAMETER_ID = "seed";
		
	private static BundleContext bundleContext;
	@Override
	protected void activate(ComponentContext componentContext) {
		SinglePopulationSingleRunnerAlgorithmFactory.bundleContext =
			componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciContext) {
		return new SinglePopulationSingleRunnerAlgorithm(
				data, parameters, ciContext, getBundleContext());
	}	
	
	@Override
	public ObjectClassDefinition mutateParameters(Data[] data, 
								ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition ocd =
			(BasicObjectClassDefinition) super.mutateParameters(data, oldParameters);
		
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
