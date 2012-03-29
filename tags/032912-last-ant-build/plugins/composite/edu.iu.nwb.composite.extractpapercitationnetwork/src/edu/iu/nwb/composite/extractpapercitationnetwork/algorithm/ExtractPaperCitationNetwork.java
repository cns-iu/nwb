package edu.iu.nwb.composite.extractpapercitationnetwork.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;

public class ExtractPaperCitationNetwork implements Algorithm, ProgressTrackable {
	public static final String ISI_FILE_TYPE = "isi";
	public static final String PAPER_COLUMN_NAME = "Cite Me As";
	public static final String CITATION_COLUMN_NAME = "Cited References";
	
	private Data[] data;
	private LogService logger;
	private ProgressMonitor progressMonitor;

	public ExtractPaperCitationNetwork(Data[] data, CIShellContext context) {
		this.data = data;
		
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		Table inTable = (Table) this.data[0].getData();

		String fileFormatPropertiesFile = this.getFileTypeProperties(ISI_FILE_TYPE);

		final ClassLoader loader = getClass().getClassLoader();
		final InputStream fileTypePropertiesFile =
			loader.getResourceAsStream(fileFormatPropertiesFile);

		final Properties metaData = new Properties();
		try {
			metaData.load(fileTypePropertiesFile);
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		}

		try {
			GraphContainer gc =
				GraphContainer.initializeGraph(
						inTable,
						CITATION_COLUMN_NAME,
						PAPER_COLUMN_NAME,
						true,
						metaData,
						this.logger,
						this.progressMonitor);
			
			final Graph outputGraph =
				gc.buildGraph(CITATION_COLUMN_NAME,	PAPER_COLUMN_NAME, "|", false, this.logger);			
			final Data outputGraphData = createOutputGraphData(outputGraph);

			final Table outputTable = ExtractNetworkFromTable
					.constructTable(outputGraph);
			final Data outputTableData = createOutputTableData(outputTable);

			return new Data[]{ outputGraphData, outputTableData };
		} catch (InvalidColumnNameException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}

	}

	private Data createOutputTableData(final Table outputTable) {
		final Data outputTableData =
			new BasicData(outputTable, Table.class.getName());
		final Dictionary tableAttributes = outputTableData.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Paper information");
		return outputTableData;
	}

	private Data createOutputGraphData(final Graph outputGraph) {
		final Data outputGraphData = new BasicData(outputGraph, Graph.class.getName());
		final Dictionary graphAttributes = outputGraphData.getMetadata();
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, data[0]);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL, "Extracted paper-citation network");
		return outputGraphData;
	}

	private String getFileTypeProperties(String fileType) {
		String propertiesFileName = "/edu/iu/nwb/composite/extractpapercitationnetwork/metadata/";
		return propertiesFileName + fileType + ".properties";
	}
	
	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}
}