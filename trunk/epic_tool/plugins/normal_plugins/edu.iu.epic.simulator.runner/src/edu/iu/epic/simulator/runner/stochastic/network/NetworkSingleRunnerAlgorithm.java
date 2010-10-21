package edu.iu.epic.simulator.runner.stochastic.network;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;

public class NetworkSingleRunnerAlgorithm extends EpidemicSimulatorAlgorithm {
	public static final String NETWORK_CORE_PID = "edu.iu.epic.simulator.core.network";
	
	public NetworkSingleRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		super(data, parameters, ciContext, bundleContext);
	}

	@Override
	protected String getCoreAlgorithmPID() {
		return NETWORK_CORE_PID;
	}
}
