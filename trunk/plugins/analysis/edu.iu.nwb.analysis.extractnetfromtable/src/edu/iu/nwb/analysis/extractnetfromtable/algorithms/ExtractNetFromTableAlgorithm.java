package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

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
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;

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
	
	
	
	public Data[] execute() throws AlgorithmExecutionException {
		final prefuse.data.Table dataTable = (prefuse.data.Table) data[0].getData();

		String split = null;
		String extractColumn = null;
		Properties p = null;
		
		split = this.parameters.get("delimiter").toString();
		extractColumn = this.parameters.get("colName").toString();

		if(this.parameters.get("aff") != null){
			p = PropertyHandler.getProperties((String)this.parameters.get("aff"),this.logger);
		}

		try{
		final ExtractNetworkFromTable enft = new ExtractNetworkFromTable(logger,
				dataTable, extractColumn, split, p, false);

		final prefuse.data.Graph outputGraph = enft.getGraph();
		final Data outputData1 = new BasicData(outputGraph,
				prefuse.data.Graph.class.getName());
		final Dictionary graphAttributes = outputData1.getMetadata();
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, data[0]);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL,
				"Extracted Network on Column "+extractColumn);

		
		final prefuse.data.Table outputTable = enft.getTable();
		final Data outputData2 = new BasicData(outputTable,
				prefuse.data.Table.class.getName());	
		final Dictionary tableAttributes = outputData2.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Merge Table: based on "+extractColumn);

		return new Data[] { outputData1, outputData2 };
		}catch(InvalidColumnNameException ex){
			throw new AlgorithmExecutionException(ex.getMessage(),ex);
		}
	}
}