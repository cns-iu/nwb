package edu.iu.nwb.converter.nwb.reader;

//Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.guibuilder.GUIBuilderService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBValidation implements AlgorithmFactory {
	   
	   protected void activate(ComponentContext ctxt) {}
	   protected void deactivate(ComponentContext ctxt) { }
	
	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	    */
	   public MetaTypeProvider createParameters(Data[] dm) {
	        return null;
	   }
	    
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
	        
	        public NWBValidationAlg(Data[] dm, Dictionary parameters, CIShellContext context) {
	        	this.data = dm;
	            this.parameters = parameters;
	            this.ciContext = context;
	        }

	        public Data[] execute() {
		    	GUIBuilderService guiBuilder = 
		    			(GUIBuilderService)ciContext.getService(GUIBuilderService.class.getName());

				String fileHandler = (String) data[0].getData();
				File inData = new File(fileHandler);
				ValidateNWBFile validator = new ValidateNWBFile();
				try{ 
					validator.validateNWBFormat(inData);
					if(validator.getValidationResult()){						
						Data[] dm = new Data[] {new BasicData(inData, "file:text/nwb")};
						dm[0].getMetaData().put(DataProperty.LABEL, "NWB file: " + fileHandler);
						dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
	                	return dm;

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
	      

	    }
}






  

    
    
    

