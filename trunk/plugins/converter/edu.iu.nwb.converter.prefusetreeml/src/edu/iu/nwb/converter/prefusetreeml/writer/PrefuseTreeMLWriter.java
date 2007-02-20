package edu.iu.nwb.converter.prefusetreeml.writer;

import java.util.Dictionary;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.io.GraphMLWriter;
import prefuse.data.io.DataIOException;
import prefuse.data.io.TreeMLWriter;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public PrefuseTreeMLWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger=(LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
		File tempFile;
        
	    String tempPath = System.getProperty("java.io.tmpdir");
	    File tempDir = new File(tempPath+File.separator+"temp");
	    if(!tempDir.exists())
	    	tempDir.mkdir();
	    try{
	    	tempFile = File.createTempFile("NWB-Session-", ".treeml.xml", tempDir);
	    		
	    }catch (IOException e){
	    	logger.log(LogService.LOG_ERROR, e.toString());
	   		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.treeml.xml");
    	}
    	if (tempFile != null){
    		try{
    			
    			(new TreeMLWriter()).writeGraph((Graph)(data[0].getData()), 
    						new BufferedOutputStream(new FileOutputStream(tempFile))) ;
    			return new Data[]{new BasicData(tempFile, "file:text/treeml+xml") };
    		}catch (DataIOException dioe){
    	   		logger.log(LogService.LOG_ERROR, "DataIOException: " + dioe.toString(), dioe);
    	   		return null;
    		}catch (IOException ioe){
    	   		logger.log(LogService.LOG_ERROR, "IOException", ioe);
    	   	 	return null;
    		}
    	}
    	else{
       		logger.log(LogService.LOG_ERROR, "Fail to generate a file in the temporary directory.");
       	 	return null;
    	}
    	
    }
}