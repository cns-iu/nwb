package edu.iu.epic.simulator.runner.exact;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;

/**
 * For now, the exact runner adds nothing to the generic EpidemicSimulatorAlgorithmFactory
 */
public class ExactRunnerAlgorithmFactory extends EpidemicSimulatorAlgorithmFactory {
	private static BundleContext bundleContext;
	@Override
	protected void activate(ComponentContext componentContext) {
		ExactRunnerAlgorithmFactory.bundleContext =
			componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
	
	@Override
	public Algorithm createAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext context) {
		return new ExactRunnerAlgorithm(data, parameters, context, getBundleContext());
	}
}
