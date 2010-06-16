package edu.iu.nwb.converter.prefusecsv.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseCsvWriter implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	private Table inTable;
    
	
	public static class Factory implements AlgorithmFactory {
	    public Algorithm createAlgorithm(
	    		Data[] data, Dictionary parameters, CIShellContext context) {
	        return new PrefuseCsvWriter(data);
	    }
	}
    public PrefuseCsvWriter(Data[] data) {
		this.inTable = (Table) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {	    
	    try {
	    	File outCSVFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"PrefuseToCSV-", "csv");
			
			(new CSVTableWriter()).writeTable(
					inTable,
					new BufferedOutputStream(new FileOutputStream(outCSVFile)));
			return new Data[]{ new BasicData(outCSVFile, CSV_MIME_TYPE) };
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}    	
    }
}