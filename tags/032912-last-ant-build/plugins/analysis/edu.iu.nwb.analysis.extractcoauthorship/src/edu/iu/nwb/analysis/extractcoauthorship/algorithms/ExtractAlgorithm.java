package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

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
import edu.iu.nwb.analysis.extractcoauthorship.metadata.SupportedFileTypes;
import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;

public class ExtractAlgorithm implements Algorithm, SupportedFileTypes,ProgressTrackable {
	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;
	ProgressMonitor progressMonitor;

	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
		
	}

	public ExtractAlgorithm(Data[] dm, Dictionary parameters,
			CIShellContext cContext) {
		data = dm;
		this.parameters = parameters;
		ciContext = cContext;
		logger = (LogService) ciContext.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException{
		final Table dataTable = (Table) data[0]
				.getData();

		
		String fileFormat = this.parameters.get("fileFormat").toString();
		String fileFormatPropertiesFile = this.getFileTypeProperties(fileFormat);
		
		final ClassLoader loader = getClass().getClassLoader();
		final InputStream fileTypePropertiesFile = loader
				.getResourceAsStream(fileFormatPropertiesFile);
		
		final Properties metaData = new Properties();
		try {
			metaData.load(fileTypePropertiesFile);
		} catch (final FileNotFoundException fnfe) {
			logger.log(LogService.LOG_ERROR, fnfe.getMessage(), fnfe);
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage(), ie);
		}
		try{
			String authorColumn = CitationFormat.getAuthorColumnByName(fileFormat);
			GraphContainer gc = GraphContainer.initializeGraph(dataTable, authorColumn, authorColumn, false, metaData, this.logger,this.progressMonitor);
			final Graph outputGraph = gc.buildGraph(authorColumn, authorColumn, "|", false, this.logger);
			final Data outputData1 = new BasicData(outputGraph,
					Graph.class.getName());
			final Dictionary graphAttributes = outputData1.getMetadata();
			graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
			graphAttributes.put(DataProperty.PARENT, data[0]);
			graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			graphAttributes.put(DataProperty.LABEL,
					"Extracted Co-Authorship Network");
		
		
		
		final Table outputTable = ExtractNetworkFromTable.constructTable(outputGraph);
	
		final Data outputData2 = new BasicData(outputTable,
				Table.class.getName());

		final Dictionary tableAttributes = outputData2.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Author information");

		return new Data[] { outputData1, outputData2 };
		}catch(InvalidColumnNameException ex){
			throw new AlgorithmExecutionException(ex.getMessage(),ex);
		}
	}
	
	private String getFileTypeProperties(String fileType){
		String propertiesFileName = "/edu/iu/nwb/analysis/extractcoauthorship/metadata/";
		
		return propertiesFileName+fileType+".properties";
	}
}
