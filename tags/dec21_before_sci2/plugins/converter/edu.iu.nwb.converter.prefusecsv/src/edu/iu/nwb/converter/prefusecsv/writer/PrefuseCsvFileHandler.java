package edu.iu.nwb.converter.prefusecsv.writer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

/**
 * @author Weixia(Bonnie) Huang 
 */    
public class PrefuseCsvFileHandler implements Algorithm {
	public static final String CSV_FILE_EXT = "file-ext:csv";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	private Object inData;
	private String inFormat;
    
	
	public static class Factory implements AlgorithmFactory {
	    public Algorithm createAlgorithm(
	    		Data[] data, Dictionary parameters, CIShellContext context) {
	        return new PrefuseCsvFileHandler(data);
	    }
	}
    public PrefuseCsvFileHandler(Data[] data) {
		inData = data[0].getData();
		inFormat = data[0].getFormat();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	if (inData instanceof File) {
    		if (CSV_MIME_TYPE.equals(inFormat)) {
    			return new Data[]{ new BasicData(inData, CSV_FILE_EXT) };
    		} else {
    			throw new AlgorithmExecutionException(
    					"Expect " + CSV_MIME_TYPE
    					+ ", but the input format is "
    					+ inFormat);
    		}
    	} else {
    		throw new AlgorithmExecutionException(
					"Expect a File, but the input data is "
					+ inData.getClass().getName());
    	}
    }
}