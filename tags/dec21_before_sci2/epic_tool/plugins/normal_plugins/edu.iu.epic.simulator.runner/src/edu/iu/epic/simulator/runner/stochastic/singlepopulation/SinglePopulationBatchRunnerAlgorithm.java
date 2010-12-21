package edu.iu.epic.simulator.runner.stochastic.singlepopulation;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.stochastic.BatchRunnerAlgorithm;

public class SinglePopulationBatchRunnerAlgorithm extends BatchRunnerAlgorithm {
	public SinglePopulationBatchRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		super(data, parameters, ciContext, bundleContext);
	}
	
	@Override
	public Algorithm getSingleRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> singleParameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		return new SinglePopulationSingleRunnerAlgorithm(
				data, singleParameters, ciContext, bundleContext);
	}
}
