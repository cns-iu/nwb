package edu.iu.nwb.converter.prefuseisi.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.CSVTableWriter;
import prefuse.data.io.DataIOException;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseCsvWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public PrefuseCsvWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger=(LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
		File tempFile;
        
	    String tempPath = System.getProperty("java.io.tmpdir");
	    File tempDir = new File(tempPath+File.separator+"temp");
	    if(!tempDir.exists()) {
	    	boolean successful = tempDir.mkdir();
	    	if (! successful) {
	    		throw new AlgorithmExecutionException("Unable to create temporary directory " + tempDir.toString());
	    	}
	    }
	    try{
	    	tempFile = File.createTempFile("NWB-Session-", ".csv", tempDir);
	    		
	    }catch (IOException e){
	    	logger.log(LogService.LOG_WARNING, e.toString(), e);
	   		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.csv");
    	}
    	if (tempFile != null){
    		try{
    			
    			(new CSVTableWriter()).writeTable((Table)(data[0].getData()), 
    						new BufferedOutputStream(new FileOutputStream(tempFile))) ;
    			return new Data[]{new BasicData(tempFile, "file:text/csv") };
    		}catch (DataIOException dioe){
    	   		throw new AlgorithmExecutionException("DataIOException: " + dioe.toString(), dioe);
    		}catch (IOException ioe){
    	   		throw new AlgorithmExecutionException("IOException", ioe);
    		}
    	}
    	else{
       		throw new AlgorithmExecutionException("Fail to generate a file in the temporary directory.");
    	}
    	
    }
}