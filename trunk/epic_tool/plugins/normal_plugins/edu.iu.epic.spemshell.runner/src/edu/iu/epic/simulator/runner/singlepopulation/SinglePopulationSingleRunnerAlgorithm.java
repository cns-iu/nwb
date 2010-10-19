package edu.iu.epic.simulator.runner.singlepopulation;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;

public class SinglePopulationSingleRunnerAlgorithm extends EpidemicSimulatorAlgorithm {	
	public static final String SINGLE_POPULATION_CORE_PID =
		"edu.iu.epic.simulator.core.singlepopulation";
	
	public SinglePopulationSingleRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		super(data, parameters, ciContext, bundleContext);
	}
	
	@Override
	protected String getCoreAlgorithmPID() {
		return SINGLE_POPULATION_CORE_PID;
	}
}