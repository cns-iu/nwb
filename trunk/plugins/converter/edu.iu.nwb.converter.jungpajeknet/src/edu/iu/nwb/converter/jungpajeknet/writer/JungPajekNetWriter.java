package edu.iu.nwb.converter.jungpajeknet.writer;

import java.util.Dictionary;
import java.io.File;
import java.io.IOException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetWriter;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public JungPajekNetWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() {
 	
		File tempFile;
	        
	    String tempPath = System.getProperty("java.io.tmpdir");
	    File tempDir = new File(tempPath+File.separator+"temp");
	    if(!tempDir.exists())
	    	tempDir.mkdir();
	    try{
	    	tempFile = File.createTempFile("NWB-Session-", ".net", tempDir);
	    		
	    }catch (IOException e){
	    	logger.log(LogService.LOG_ERROR, "IOException", e);
	   		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.net");
    	}
    	if (tempFile != null){
    		try{
    			(new PajekNetWriter()).save((Graph)(data[0].getData()), 
    						tempFile.getPath()) ;
    			return new Data[]{new BasicData(tempFile, "file:application/pajek") };
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