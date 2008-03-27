package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;

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
	
	public boolean validateProperties(final FileInputStream fis) throws IOException{
		final BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		boolean wellFormed = true;
		String line;
		Pattern p = Pattern.compile("^.*\\..*=.*\\..*");
		Matcher m;
		
		while((line = br.readLine()) != null){		
			if(line.startsWith("node.") || line.startsWith("edge.")){				
				m = p.matcher(line.subSequence(0, line.length())).reset();
				if(!m.find()){
					wellFormed = false;	
				}
			}	
		}
		br.close();	
		return wellFormed;		
	}

	public Properties getProperties(String fileName) throws AlgorithmExecutionException {
		final Properties aggregateDefs = new Properties();
		boolean wellFormed = true;
		
		try {
			File f = new File(fileName);
			FileInputStream in = new FileInputStream(f);
			
			wellFormed = validateProperties(in);
			
			if(wellFormed){
				in = new FileInputStream(f);
				aggregateDefs.load(in);
			}
			else{
				logger.log(LogService.LOG_WARNING, "Your Aggregate Function File did not follow the specified format.\n" +
						"Continuing the extraction without additional analysis.");
				//need to add a documentation link about how to specify the aggregate function file.
			}
		} 
		catch (final FileNotFoundException fnfe) {
			throw new AlgorithmExecutionException(fnfe.getMessage(), fnfe);
		} catch (final IOException ie) {
			throw new AlgorithmExecutionException(ie.getMessage(), ie);
		}
		return aggregateDefs;
	}
	
	public Data[] execute() throws AlgorithmExecutionException {
		final prefuse.data.Table dataTable = (prefuse.data.Table) data[0].getData();

		String split = null;
		String extractColumn = null;
		Properties p = null;
		
		split = this.parameters.get("delimiter").toString();
		extractColumn = this.parameters.get("colName").toString();

		if(this.parameters.get("aff") != null){
			p = this.getProperties((String)this.parameters.get("aff"));
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
		tableAttributes.put(DataProperty.LABEL, "Unique Values from Column "+extractColumn);

		return new Data[] { outputData1, outputData2 };
		}catch(InvalidColumnNameException ex){
			throw new AlgorithmExecutionException(ex.getMessage(),ex);
		}
	}
}