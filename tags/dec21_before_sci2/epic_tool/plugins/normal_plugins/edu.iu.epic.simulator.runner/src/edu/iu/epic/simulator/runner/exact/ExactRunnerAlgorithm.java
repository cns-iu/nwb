package edu.iu.epic.simulator.runner.exact;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;

/**
 * For now, the exact runner adds nothing to the generic EpidemicSimulatorAlgorithm.
 */
public class ExactRunnerAlgorithm extends EpidemicSimulatorAlgorithm {
	public static final String EXACT_CORE_PID = "edu.iu.epic.simulator.core.exact";
	
	public ExactRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		super(data, parameters, ciContext, bundleContext);
	}

	@Override
	protected String getCoreAlgorithmPID() {
		return EXACT_CORE_PID;
	}
}
