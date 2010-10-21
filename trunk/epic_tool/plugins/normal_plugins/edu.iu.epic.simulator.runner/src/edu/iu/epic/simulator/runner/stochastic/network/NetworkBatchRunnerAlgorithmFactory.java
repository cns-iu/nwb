package edu.iu.epic.simulator.runner.stochastic.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import edu.iu.epic.simulator.runner.stochastic.BatchRunnerAlgorithmFactory;

public class NetworkBatchRunnerAlgorithmFactory extends BatchRunnerAlgorithmFactory {
	private static BundleContext bundleContext;
	protected void activate(ComponentContext componentContext) {
		bundleContext =	componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciContext) {
		return new NetworkBatchRunnerAlgorithm(data, parameters, ciContext, getBundleContext());
	}
}
