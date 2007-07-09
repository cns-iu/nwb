package edu.iu.nwb.converter.prefusexgmml.writer;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.XMLGraphWriter;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public PrefuseXGMMLWriter(Data[] data, Dictionary parameters, CIShellContext context) {
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
	    	tempFile = File.createTempFile("NWB-Session-", ".net", tempDir);
	    		
	    }catch (IOException e){
	    	logger.log(LogService.LOG_ERROR, e.toString());
	   		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");
    	}
    	if (tempFile != null){
    		try{
    			(new XMLGraphWriter()).writeGraph((Graph)(data[0].getData()), 
    						tempFile) ;
    			return new Data[]{new BasicData(tempFile, "file:text/xgmml+xml") };
    		}catch (IOException ioe){

    			return null;
    		}
    	}
    	else{

    		return null;
    	}


    }
}