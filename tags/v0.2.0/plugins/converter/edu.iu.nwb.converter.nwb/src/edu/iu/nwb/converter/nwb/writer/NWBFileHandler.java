package edu.iu.nwb.converter.nwb.writer;

//Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.nwb.common.NWBFileProperty;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBFileHandler implements AlgorithmFactory {
    private MetaTypeProvider provider;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new NWBFileHandlerAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
    
    public class NWBFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
        GUIBuilderService guiBuilder;
        
        public NWBFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
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
        	if(inData instanceof File && format.equals(NWBFileProperty.NWB_MIME_TYPE)){
        		
				ValidateNWBFile validator = new ValidateNWBFile();
				try{ 
					validator.validateNWBFormat((File)inData);
					if(validator.getValidationResult()){
						return new Data[]{new BasicData(inData, NWBFileProperty.NWB_FILE_TYPE)};  
					}else {
						guiBuilder.showError("Bad NWB Format", 
								"Sorry, your file does not comply with the NWB File Formate Specification.",
								"Sorry, your file does not comply with the NWB File Formate Specification.\n"+
								"Please review the latest NWB File Formate Specification at "+
								"http://nwb.slis.indiana.edu/software.html, and update your file.");
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
        			logger.log(LogService.LOG_ERROR, "Expect a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals(NWBFileProperty.NWB_MIME_TYPE))
        			logger.log(LogService.LOG_ERROR, "Expect "+NWBFileProperty.NWB_MIME_TYPE+", but the input format is "+format);
       			return null;
        	}

        }
    }
}