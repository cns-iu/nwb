package edu.iu.epic.simulator.runner.exact;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;
import edu.iu.epic.simulator.runner.utility.postprocessing.DatToCsv;

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
	
	@Override
	protected Data[] prepareForDataManager(Data[] simulationOutputData)
			throws AlgorithmExecutionException {
		try {
			File datFile = (File) simulationOutputData[0].getData();
			File csvFile = new DatToCsv(datFile).convert();
			
			String label = (String) simulationOutputData[0].getMetadata().get(DataProperty.LABEL);
			
			return new Data[]{ datafyCsvFile(csvFile, label, this.data[0]) };
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Problem preparing results: " + e.getMessage(), e);
		} catch (NumberFormatException e) {
			throw new AlgorithmExecutionException(
					"Problem preparing results: " + e.getMessage(), e);
		}
	}
}
