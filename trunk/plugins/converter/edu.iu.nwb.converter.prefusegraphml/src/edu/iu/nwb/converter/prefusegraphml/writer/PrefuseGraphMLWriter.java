package edu.iu.nwb.converter.prefusegraphml.writer;

import java.util.Dictionary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.io.GraphMLWriter;
import prefuse.data.io.DataIOException;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public PrefuseGraphMLWriter(Data[] data, Dictionary parameters, CIShellContext context) {
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
    			(new GraphMLWriter()).writeGraph((Graph)(data[0].getData()), 
    						new FileOutputStream(tempFile)) ;
    			return new Data[]{new BasicData(tempFile, File.class.getName()) };
    		}catch (DataIOException dioe){
    			return null;
    		}catch (IOException ioe){
    			//use guibuilder to display the exception
    			return null;
    		}
    	}
    	else{
//    		use guibuilder to display the exception
    		return null;
    	}
    	
    }
}