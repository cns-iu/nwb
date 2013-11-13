package edu.iu.sci2.preprocessing.scholar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class GenericPublicationAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary<String, Object> parameters;
    LogService log;
    
    public GenericPublicationAlgorithm(Data[] data,
    				  Dictionary<String, Object> parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.log = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Table inputTable = (Table) data[0].getData();
    	
    	
    	String filePath = parameters.get("file").toString(); // full local file path, inputted by user
    	
    	Table finalTable = null;
    	try {
			URL configUrl = new File(filePath).toURI().toURL();
			finalTable = TableUtilities.standardizeTable(configUrl, inputTable);
		} catch (IOException e) {
			String errorMsg = "An error occurred while attempting to read the properties file. " +
							"Please check that a properly formatted header map file, with a .hmap " +
							"file extension, was selected. Please see this wiki page for details on " +
							"the .hmap specification: " +
							"http://wiki.cns.iu.edu/display/SCI2TUTORIAL/3.8+Header+Map+Files";
			throw new AlgorithmExecutionException(errorMsg);
		}
    	
        Data[] outputData = formatAsData(finalTable);
        return outputData;
    }
    
	private Data[] formatAsData(Table inputTable) throws AlgorithmExecutionException {
		try {
			Data[] dm = new Data[] {new BasicData(inputTable, Table.class.getName())};
			dm[0].getMetadata().put(DataProperty.LABEL, "Table with Generic Headers");
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			dm[0].getMetadata().put(DataProperty.PARENT, data[0]);
			return dm;
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}