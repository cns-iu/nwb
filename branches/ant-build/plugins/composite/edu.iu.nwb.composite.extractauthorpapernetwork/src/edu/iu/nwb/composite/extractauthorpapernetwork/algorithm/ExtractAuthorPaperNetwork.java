package edu.iu.nwb.composite.extractauthorpapernetwork.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;

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
import edu.iu.nwb.composite.extractauthorpapernetwork.metadata.AuthorPaperFormat;

public class ExtractAuthorPaperNetwork implements Algorithm, ProgressTrackable {
	private Data inData;
	private LogService logger;
	private Table table;
	private String fileFormat;
	private String fileFormatPropertiesFileName;
	private ProgressMonitor progressMonitor;

	public ExtractAuthorPaperNetwork(
			Table table, String fileFormat, Data inData, LogService logger) {
		this.table = table;
		this.fileFormat = fileFormat;
		this.inData = inData;
		this.logger = logger;
		
		this.fileFormatPropertiesFileName = getFileTypePropertiesFileName(this.fileFormat);
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Graph outputNetwork = constructNetwork();
			Data graphData = wrapOutputNetworkAsData(outputNetwork);

			Table outputTable = ExtractNetworkFromTable.constructTable(outputNetwork);
			Data tableData = wrapOutputTableAsData(outputTable);

			return new Data[] { graphData, tableData };
		} catch (InvalidColumnNameException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;

	}

	private Data wrapOutputNetworkAsData(Graph outputNetwork) throws InvalidColumnNameException {
		Data networkData = new BasicData(outputNetwork, Graph.class.getName());
		Dictionary<String, Object> graphAttributes = networkData.getMetadata();
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, inData);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL, "Extracted author-paper network");

		return networkData;
	}

	private Data wrapOutputTableAsData(Table outputTable) {
		Data tableData = new BasicData(outputTable, Table.class.getName());
		Dictionary<String, Object> tableAttributes = tableData.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, inData);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "author-paper information");

		return tableData;
	}

	private Graph constructNetwork() throws InvalidColumnNameException {
		Properties aggregateProperties =
			loadAggregatePropertiesFile(this.fileFormatPropertiesFileName);

		String authorColumn =
			AuthorPaperFormat.AUTHOR_NAME_COLUMNS_BY_FORMATS.get(this.fileFormat);
		String paperColumn = AuthorPaperFormat.PAPER_NAME_COLUMNS_BY_FORMATS.get(this.fileFormat);
		GraphContainer graphContainer = GraphContainer.initializeGraph(
			this.table,
			authorColumn,
			paperColumn,
			true,
			aggregateProperties,
			this.logger,
			this.progressMonitor);
		// TODO Test whether we can really hard-core "|".  Use sampledata bibtex etc.
		Graph outputNetwork = graphContainer.buildGraph(
			authorColumn, paperColumn, "|", false, this.logger);

		return outputNetwork;
	}

	private Properties loadAggregatePropertiesFile(String fileFormatPropertiesFileName) {
		InputStream fileTypePropertiesFile =
			getFileTypePropertiesFile(this.fileFormatPropertiesFileName);

		Properties aggregateProperties = new Properties();

		try {
			aggregateProperties.load(fileTypePropertiesFile);
		} catch (FileNotFoundException e) {
			this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			this.logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		}

		return aggregateProperties;
	}

	private InputStream getFileTypePropertiesFile(String fileFormatPropertiesFile) {
		ClassLoader loader = getClass().getClassLoader();

		return loader.getResourceAsStream(fileFormatPropertiesFile);
	}
	
	private static String getFileTypePropertiesFileName(String fileType){
		String propertiesFileName = "/edu/iu/nwb/composite/extractauthorpapernetwork/metadata/";
		
		return propertiesFileName + fileType + ".properties";
	}
}