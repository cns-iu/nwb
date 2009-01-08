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

import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;

public class ExtractPaperCitationNetwork implements Algorithm,ProgressTrackable {
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
	private LogService logger;
	private ProgressMonitor progressMonitor;
	
	
	public ProgressMonitor getProgressMonitor() {
		
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	public ExtractPaperCitationNetwork(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {

		Table inTable = (Table)this.data[0].getData();

		String fileFormat = "isi";
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
			String citationColumnName = getCitationColumnByFileType(null);
			String paperColumnName = getPaperColumnByFileType(null);
			
			

			GraphContainer gc = GraphContainer.initializeGraph(inTable, citationColumnName, paperColumnName, true, metaData, this.logger,this.progressMonitor);
			final prefuse.data.Graph outputGraph = gc.buildGraph(citationColumnName, paperColumnName, "|", this.logger);
			final Data outputData1 = new BasicData(outputGraph,
					prefuse.data.Graph.class.getName());
			final Dictionary graphAttributes = outputData1.getMetadata();
			graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
			graphAttributes.put(DataProperty.PARENT, data[0]);
			graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			graphAttributes.put(DataProperty.LABEL,
			"Extracted paper-citation network");



			final prefuse.data.Table outputTable = ExtractNetworkFromTable.constructTable(outputGraph);

			final Data outputData2 = new BasicData(outputTable,
					prefuse.data.Table.class.getName());

			final Dictionary tableAttributes = outputData2.getMetadata();
			tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
			tableAttributes.put(DataProperty.PARENT, data[0]);
			tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
			tableAttributes.put(DataProperty.LABEL, "Paper information");

			return new Data[] { outputData1, outputData2 };
		}catch(InvalidColumnNameException ex){
			throw new AlgorithmExecutionException(ex.getMessage(),ex);
		}	
	
}

private String getFileTypeProperties(String fileType){
	String propertiesFileName = "/edu/iu/nwb/composite/extractpapercitationnetwork/metadata/";	
	return propertiesFileName+fileType+".properties";
}

private static String getCitationColumnByFileType(String fileType){
	return "Cited References";

}

private static String getPaperColumnByFileType(String fileType){
	return "Cite Me As";
}


}