package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;

public class ExtractAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext ciContext;
	LogService logger;

	public ExtractAlgorithm(Data[] dm, Dictionary parameters,
			CIShellContext cContext) {
		data = dm;
		this.parameters = parameters;
		ciContext = cContext;
		logger = (LogService) ciContext.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException{
		final prefuse.data.Table dataTable = (prefuse.data.Table) data[0]
				.getData();

		final ClassLoader loader = getClass().getClassLoader();
		final InputStream in = loader
				.getResourceAsStream("/edu/iu/nwb/analysis/extractcoauthorship/metadata/Operations.properties");
		
		final Properties metaData = new Properties();
		try {
			metaData.load(in);
		} catch (final FileNotFoundException fnfe) {
			logger.log(LogService.LOG_ERROR, fnfe.getMessage());
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage());
		}
		try{
		final ExtractNetworkFromTable enft = new ExtractNetworkFromTable(logger,
				dataTable, "Authors", "|", metaData, false);
		
		
		final prefuse.data.Graph outputGraph = enft.getGraph();
		final prefuse.data.Table outputTable = enft.getTable();
		final Data outputData1 = new BasicData(outputGraph,
				prefuse.data.Graph.class.getName());
		final Data outputData2 = new BasicData(outputTable,
				prefuse.data.Table.class.getName());
		final Dictionary graphAttributes = outputData1.getMetadata();
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, data[0]);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL,
				"Extracted Co-Authorship Network");

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
}
