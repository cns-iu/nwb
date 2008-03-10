package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;

public class ExtractNetFromTableAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;

	public ExtractNetFromTableAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		logger = (LogService) context.getService(LogService.class.getName());
	}

	public Properties getProperties(String fileName){
		
	

		final Properties aggregateDefs = new Properties();
		try {
			File f = new File(fileName);
			final FileInputStream in = new FileInputStream(f);
			aggregateDefs.load(in);
		} catch (final FileNotFoundException fnfe) {
			logger.log(LogService.LOG_ERROR, fnfe.getMessage());
			return null;
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage());
			return null;
		}
		return aggregateDefs;
	}
	
	public Data[] execute() {
		final prefuse.data.Table dataTable = (prefuse.data.Table) data[0]
		                                                               .getData();

		String split;
		String aggregateFunctions;
		String extractColumn;
		
		split = this.parameters.get("delimiter").toString();
		extractColumn = this.parameters.get("colName").toString();
		aggregateFunctions = this.parameters.get("aff").toString();

		final ExtractNetworkFromTable enft = new ExtractNetworkFromTable(logger,
				dataTable, extractColumn, split, this.getProperties(aggregateFunctions), false);


		final prefuse.data.Graph outputGraph = enft.getGraph();
		final prefuse.data.Table outputTable = enft.getTable();
		final Data outputData1 = new BasicData(outputGraph,
				prefuse.data.Graph.class.getName());
		final Data outputData2 = new BasicData(outputTable,
				prefuse.data.Table.class.getName());
		final Dictionary graphAttributes = outputData1.getMetaData();
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, data[0]);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL,
				"Extracted Network on Column "+extractColumn);

		final Dictionary tableAttributes = outputData2.getMetaData();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Unique Values from Column "+extractColumn);

		return new Data[] { outputData1, outputData2 };
	}
}