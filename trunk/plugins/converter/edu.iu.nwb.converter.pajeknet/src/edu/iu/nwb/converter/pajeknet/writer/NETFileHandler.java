/**
 * 
 */
package edu.iu.nwb.converter.pajeknet.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;

/**
 * @author Timothy Kelley
 *
 */
public class NETFileHandler implements AlgorithmFactory {
	 private MetaTypeProvider provider;
	 
	 protected void activate(ComponentContext ctxt) {
	        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
	        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
	    }
	    protected void deactivate(ComponentContext ctxt) {
	        provider = null;
	    }

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		// TODO Auto-generated method stub
		return new NETFileHandlerAlg(data, parameters, context);
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return provider;
	}
	
	public class NETFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
        GUIBuilderService guiBuilder;
        
        public NETFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.ciContext = context;
            logger = (LogService)ciContext.getService(LogService.class.getName());
	    	guiBuilder = 
    			(GUIBuilderService)ciContext.getService(GUIBuilderService.class.getName());

        }

        public Data[] execute() {

        	Object inData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inData instanceof File && format.equals(NETFileProperty.NET_MIME_TYPE)){
        		
				ValidateNETFile validator = new ValidateNETFile();
				try{ 
					validator.validateNETFormat((File)inData);
					if(validator.getValidationResult()){
						return new Data[]{new BasicData(inData, NETFileProperty.NET_FILE_TYPE)};  
					}else {
						guiBuilder.showError("Bad NET Format", 
								"Sorry, your file does not comply with the NET File Formate Specification.",
								"Sorry, your file does not comply with the NET File Formate Specification.\n"+
								"Please review the latest NET File Formate Specification at "+
								"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file.");
						return null;
					}

				}catch (FileNotFoundException e){
					guiBuilder.showError("File Not Found Exception", 
							"Got an File Not Found Exception",e);	
					return null;
				}catch (IOException ioe){
					guiBuilder.showError("IOException", 
							"Got an IOException",ioe);
					return null;
				}        		        		
        	}
        	else {
        		if (!(inData instanceof File))        				
        			logger.log(LogService.LOG_ERROR, "Expected a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals(NETFileProperty.NET_MIME_TYPE))
        			logger.log(LogService.LOG_ERROR, "Expected "+NETFileProperty.NET_MIME_TYPE+", but the input format is "+format);
       			return null;
        	}

        }
    }

}
