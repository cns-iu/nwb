package edu.iu.nwb.converter.nwb.reader;

//Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;


import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBValidation implements AlgorithmFactory {
   
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	    */
	   public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	           CIShellContext context) {
		   return new NWBValidationAlg(dm, parameters, context);
	   }
	    
	   public class NWBValidationAlg implements Algorithm {
	        
	    	Data[] data;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        LogService logger;
	        
	        public NWBValidationAlg(Data[] dm, Dictionary parameters, CIShellContext context) {
	        	this.data = dm;
	            this.parameters = parameters;
	            this.ciContext = context;
	            logger = (LogService)context.getService(LogService.class.getName());
	        }

	        public Data[] execute() throws AlgorithmExecutionException {
		    	

				String fileHandler = (String) data[0].getData();
				File inData = new File(fileHandler);
				ValidateNWBFile validator = new ValidateNWBFile();
				try{ 
					validator.validateNWBFormat(inData);
					if(validator.getValidationResult()){						
						Data[] dm = new Data[] {new BasicData(inData, "file:text/nwb")};
						dm[0].getMetadata().put(DataProperty.LABEL, "NWB file: " + fileHandler);
						dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
	                	return dm;

					}else {
						throw new AlgorithmExecutionException(
								"Sorry, your file does not comply with the NWB File Format Specification.\n"+
								"Please review the latest NWB File Format Specification at "+
								"http://nwb.slis.indiana.edu/software.html, and update your file. \n"+
								validator.getErrorMessages());						
					}

				}catch (FileNotFoundException e){
					throw new AlgorithmExecutionException(
							"Unable to find the specified .nwb file for validation.",e);						
				}catch (IOException ioe){
					throw new AlgorithmExecutionException(
							"IO errors while validating the specified .nwb file.",ioe);					
				}
	        }
	      

	    }
}






  

    
    
    

