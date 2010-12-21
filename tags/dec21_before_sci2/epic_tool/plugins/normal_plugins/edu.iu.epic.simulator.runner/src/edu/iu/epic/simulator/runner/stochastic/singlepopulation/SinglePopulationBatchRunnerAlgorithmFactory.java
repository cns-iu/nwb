package edu.iu.epic.simulator.runner.stochastic.singlepopulation;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;
import edu.iu.epic.simulator.runner.stochastic.BatchRunnerAlgorithmFactory;

public class SinglePopulationBatchRunnerAlgorithmFactory extends BatchRunnerAlgorithmFactory {
	private static BundleContext bundleContext;
	protected void activate(ComponentContext componentContext) {
		bundleContext = componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciContext) {
		return new SinglePopulationBatchRunnerAlgorithm(
				data, parameters, ciContext, getBundleContext());
	}
	
	
	@Override
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition ocd =
			(BasicObjectClassDefinition) super.mutateParameters(data, oldParameters);
		
		ocd.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				EpidemicSimulatorAlgorithmFactory.POPULATION_ATTRIBUTE_DEFINITION);
		
		return ocd;
	}
}
