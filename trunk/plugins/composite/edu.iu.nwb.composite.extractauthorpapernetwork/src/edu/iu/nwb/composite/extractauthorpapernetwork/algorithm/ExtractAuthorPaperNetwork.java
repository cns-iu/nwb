package edu.iu.nwb.composite.extractauthorpapernetwork.algorithm;

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

import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.composite.extractauthorpapernetwork.metadata.SupportedFileTypes;

public class ExtractAuthorPaperNetwork implements Algorithm,ProgressTrackable,SupportedFileTypes {

	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;
	ProgressMonitor progressMonitor;

	public ExtractAuthorPaperNetwork(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.ciContext = context;
		this.logger = (LogService) ciContext.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		final prefuse.data.Table dataTable = (prefuse.data.Table) data[0]
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
			logger.log(LogService.LOG_ERROR, fnfe.getMessage());
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage());
		}
		try{
			String authorColumn = AuthorPaperFormat.getAuthorColumnByName(fileFormat);
			String paperColumn = AuthorPaperFormat.getPaperColumnByName(fileFormat);
			GraphContainer gc = GraphContainer.initializeGraph(dataTable, authorColumn, paperColumn, true, metaData, this.logger,this.progressMonitor);
			final prefuse.data.Graph outputGraph = gc.buildGraph(authorColumn, paperColumn, "|", this.logger);
			final Data outputData1 = new BasicData(outputGraph,
					prefuse.data.Graph.class.getName());
			final Dictionary graphAttributes = outputData1.getMetadata();
			graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
			graphAttributes.put(DataProperty.PARENT, data[0]);
			graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			graphAttributes.put(DataProperty.LABEL,
					"Extracted author-paper network");



			final prefuse.data.Table outputTable = ExtractNetworkFromTable.constructTable(outputGraph);

			final Data outputData2 = new BasicData(outputTable,
					prefuse.data.Table.class.getName());

			final Dictionary tableAttributes = outputData2.getMetadata();
			tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
			tableAttributes.put(DataProperty.PARENT, data[0]);
			tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
			tableAttributes.put(DataProperty.LABEL, "author-paper information");

			return new Data[] { outputData1, outputData2 };
		}catch(InvalidColumnNameException ex){
			throw new AlgorithmExecutionException(ex.getMessage(),ex);
		}
	}



	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;

	}
	
	private String getFileTypeProperties(String fileType){
		String propertiesFileName = "/edu/iu/nwb/composite/extractauthorpapernetwork/metadata/";
		
		return propertiesFileName+fileType+".properties";
	}

}