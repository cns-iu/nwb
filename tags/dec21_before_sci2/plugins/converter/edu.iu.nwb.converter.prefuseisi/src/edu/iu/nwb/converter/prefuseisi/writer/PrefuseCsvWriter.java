package edu.iu.nwb.converter.prefuseisi.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import prefuse.data.io.CSVTableWriter;
import prefuse.data.io.DataIOException;
/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseCsvWriter implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	private Table inTable;
    
	
    public PrefuseCsvWriter(Data[] data) {
		this.inTable = (Table) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			File outCSVFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"CSV-", "csv");
			
			OutputStream outStream =
				new BufferedOutputStream(new FileOutputStream(outCSVFile));
			(new CSVTableWriter()).writeTable(inTable, outStream) ;
			
			return new Data[]{ new BasicData(outCSVFile, CSV_MIME_TYPE) };
		} catch (DataIOException e){
	   		throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e){
	   		throw new AlgorithmExecutionException(e.getMessage(), e);
		}    	
    }
}