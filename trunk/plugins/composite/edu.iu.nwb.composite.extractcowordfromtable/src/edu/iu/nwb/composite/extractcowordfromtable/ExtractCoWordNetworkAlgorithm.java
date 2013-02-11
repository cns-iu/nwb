package edu.iu.nwb.composite.extractcowordfromtable;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

public class ExtractCoWordNetworkAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;
	private AlgorithmFactory extractDirectedNetwork;
	private DataConversionService converter;
	private AlgorithmFactory bibliographicCoupling;
	private AlgorithmFactory deleteIsolates;

	public ExtractCoWordNetworkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
			AlgorithmFactory extractDirectedNetwork, DataConversionService converter,
			AlgorithmFactory bibliographicCoupling, AlgorithmFactory deleteIsolates) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
		this.extractDirectedNetwork = extractDirectedNetwork;
		this.converter = converter;
		this.bibliographicCoupling = bibliographicCoupling;
		this.deleteIsolates = deleteIsolates;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		this.logger.log (LogService.LOG_INFO, "Class: " + extractDirectedNetwork.getClass().getName());
		
		try
		{
			// Form a new parameters object which contains the target column as the combined string of
			// chosen columns.
			String targetColumn = ""; //removed use of StringBuild for 1.4 compliance.
			boolean firstColumnHasBeenAdded = false;
			Dictionary newParameters = new Hashtable();
			for (Enumeration parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();)
	    	{
	    		String parameterKey = (String)parameterKeys.nextElement();
	    		Object parameterValue = parameters.get(parameterKey);
	    		// Explode the current key based on "_" to separate the "column" and original_name.
	    		String[] explodedParameterKey = parameterKey.split("\\_");
	    		
	    		// Does the key start with "column_"?  (Is this a parameter meant for the target
	    		// column?) Is it checked?
	    		if (isAColumnCheckBoxParameter(explodedParameterKey)) 
	    		{
	    			if (columnCheckBoxWasSelected(parameterValue)) {
	    			//add column to list of columns we want to be part of the target.
	    			
	    			//If this is not the first column added, append a delimiter first as well.
	    				if (firstColumnHasBeenAdded) 
	    				{
	    					targetColumn += ",";
	    				}
	    				targetColumn += explodedParameterKey[1];
	    				firstColumnHasBeenAdded = true;
	    			} else 
	    			{
	    				//ignore check box if it was not selected
	    			}
	    		}	
	    		else //it was just a normal parameter
	    		{	
	    			//so add it to the new dictionary.
	    			newParameters.put(parameterKey, parameterValue);
	    		}
	    	}
			
			// Add the target column.
			newParameters.put("targetColumn", targetColumn.toString());
			
			//turn scientometrics data table into prefuse Graph of directed network
			Algorithm extractAlg = extractDirectedNetwork.createAlgorithm(data, newParameters, context);
			Data[] directedNetworkData = extractAlg.execute();
			//converter prefuse Graph of directed network into nwb graph file of directed network
			Data convertedNetworkData = converter.convert(directedNetworkData[0], "file:text/nwb");
			//turn nwb graph file of directed network into undirected similarity network
			Algorithm bibCouplingAlg = bibliographicCoupling.createAlgorithm(new Data[] { convertedNetworkData },
					new Hashtable(), context);
			Data[] coWordNetworkData = bibCouplingAlg.execute();
			//return undirected similarity network
			Algorithm deleteIsolatesAlg = deleteIsolates.createAlgorithm(new Data[] { coWordNetworkData[0] },
					new Hashtable(), context);
			coWordNetworkData = deleteIsolatesAlg.execute();
			addCorrectMetadata(coWordNetworkData[0], data[0]);
			return coWordNetworkData;
		} catch (ConversionException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
	
	 private Data addCorrectMetadata(Data networkData, Data parentData) {
			networkData.getMetadata().put(DataProperty.LABEL, "Co-Word Occurrence network");
	        networkData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
	        networkData.getMetadata().put(DataProperty.PARENT, parentData);
	        return networkData;
	    }
	 
	 
	 private boolean isAColumnCheckBoxParameter(String[] explodedParameterKey) {
		 return (explodedParameterKey.length > 1) && (explodedParameterKey[0].equals("column"));
	 }
	 
	 private boolean columnCheckBoxWasSelected(Object parameterValue) 
	 {
		//parameter represents whether a certain column is selected or not
		 boolean columnWasSelected = ((Boolean) parameterValue).booleanValue();
		 return columnWasSelected;
	 }
		 
}