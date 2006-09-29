package edu.iu.nwb.nwbpersisters;

import java.util.Dictionary;
import java.io.File;
import java.io.IOException;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBWriter implements AlgorithmFactory {
	   private MetaTypeProvider provider;

	   protected void activate(ComponentContext ctxt) {
	    	MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
	        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());  	        
	   }
	   
	   protected void deactivate(ComponentContext ctxt) {
	        provider = null;
	   }
	   
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	    */
	   public MetaTypeProvider createParameters(Data[] dm) {
	        return provider;
	   }
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new NWBWriterPersister(dm, parameters, context);
	    }
	    
	    private class NWBWriterPersister implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        LogService logger;
	        
	        public NWBWriterPersister(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	            logger = (LogService) ciContext.getService(LogService.class.getName());

	        }

	        public Data[] execute() {
	        	File tempFile;
	        
//	        	String tempPath = System.getProperty("user.dir"); 
	        	String tempPath = System.getProperty("java.io.tmpdir");
	        	File tempDir = new File(tempPath+File.separator+"temp");
	        	if(!tempDir.exists())
	        		tempDir.mkdir();
	        	try{
	        		tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);
	        		
	        	}catch (IOException e){
	        		logger.log(LogService.LOG_ERROR, e.toString());
	        		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

	        	}
//	            String fileHandler = (String) parameters.get("edu.iu.nwb.nwbpersisters.NWBReader.fileInput");
	        	if (tempFile != null){
//	        		System.out.println("in NWBWriterPersister execute method, tempFile= "+tempFile.getPath());
	        	
	        		(new NWBFile()).save((NWBModel)(dm[0].getData()), tempFile) ;
	        	}
	            return new Data[]{new BasicData(tempFile, File.class.getName()) };
	        }
	    }
}






  

    
    
    

