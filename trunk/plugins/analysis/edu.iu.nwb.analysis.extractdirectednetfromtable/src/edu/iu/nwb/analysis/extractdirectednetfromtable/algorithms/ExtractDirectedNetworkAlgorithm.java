package edu.iu.nwb.analysis.extractdirectednetfromtable.algorithms;

import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;

public class ExtractDirectedNetworkAlgorithm implements Algorithm, ProgressTrackable {

	private Data[] data;
	private Dictionary parameters;
	private LogService logger;
	private ProgressMonitor progressMonitor;	


	public ExtractDirectedNetworkAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.data = data;
		this.parameters = parameters;

		this.logger = (LogService) context.getService(LogService.class.getName());
	}
	
	//TODO: Move parameter extraction into constructor
	public Data[] execute() throws AlgorithmExecutionException {
		final Table dataTable = (Table) data[0].getData();

		String delimiter = (String) parameters
				.get(ExtractDirectedNetworkAlgorithmFactory.DELIMITER_PARAMETER_ID);
		String sourceColumnName = (String) parameters
				.get(ExtractDirectedNetworkAlgorithmFactory.SOURCE_COLUMN_NAME_PARAMETER_ID);
		String targetColumnName = (String) parameters
				.get(ExtractDirectedNetworkAlgorithmFactory.TARGET_COLUMN_NAME_PARAMETER_ID);

		Object functionFile = parameters.get(
				ExtractDirectedNetworkAlgorithmFactory.AGGREGATION_FUNCTION_FILE_PARAMETER_ID);
		Properties functions = null;
		if (functionFile != null) {
			functions = PropertyHandler.getProperties(functionFile.toString(), logger);
		}

		try {
			GraphContainer gc =
				GraphContainer.initializeGraph(
						dataTable,
						sourceColumnName,
						targetColumnName,
						true,
						functions,
						logger,
						progressMonitor);

			Graph directedNetwork =
				gc.buildGraph(sourceColumnName, targetColumnName, delimiter, logger);

			
			BasicDataPlus outData =
				new BasicDataPlus(directedNetwork, DataProperty.NETWORK_TYPE, data[0]);
			outData.markAsModified();
			outData.setLabel(
					"Network with directed edges from " + sourceColumnName +
					" to " + targetColumnName + ".");
			
			return new Data[]{ outData };
		} catch (InvalidColumnNameException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}
	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}
}