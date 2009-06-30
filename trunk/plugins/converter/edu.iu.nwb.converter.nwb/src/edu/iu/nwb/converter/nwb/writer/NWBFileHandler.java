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
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;


import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.converter.nwb.common.NWBFileProperty;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBFileHandler implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new NWBFileHandlerAlg(data, parameters, context);
    }
    
    public class NWBFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
      
        
        public NWBFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.ciContext = context;
            logger = (LogService)ciContext.getService(LogService.class.getName());
	    
        }

        public Data[] execute() throws AlgorithmExecutionException {

        	Object inData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inData instanceof File && format.equals(NWBFileProperty.NWB_MIME_TYPE)){
        		
				ValidateNWBFile validator = new ValidateNWBFile();
				try{ 
					validator.validateNWBFormat((File)inData);
					if(validator.getValidationResult()){
						  
					}else {
						this.logger.log(LogService.LOG_WARNING,"This file does not seem to comply with the NWB format specification. Forwarding anyways.\n" +
								"Please notify the developers of the algorithm you are using or the NWB team.");
						
					}
					return new Data[]{new BasicData(inData, NWBFileProperty.NWB_FILE_TYPE)};

				}catch (FileNotFoundException e){
					throw new AlgorithmExecutionException( 
							"Could not find the specified .nwb file to write.",e);	
					
				}catch (IOException ioe){
					throw new AlgorithmExecutionException(
							"Errors writing the specified .nwb file.",ioe);
					
				}        		        		
        	}
        	else {
        		if (!(inData instanceof File))        				
        			throw new AlgorithmExecutionException( "Expect a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals(NWBFileProperty.NWB_MIME_TYPE))
        			throw new AlgorithmExecutionException( "Expect "+NWBFileProperty.NWB_MIME_TYPE+", but the input format is "+format);
       			return null;
        	}

        }
    }
}