package edu.iu.nwb.converter.nwb.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBValidation implements AlgorithmFactory {   
   /**
    * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
    */
   public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
           CIShellContext context) {
	   return new NWBValidationAlgorithm(dm, parameters, context);
   }
    
   public class NWBValidationAlgorithm implements Algorithm {	        
        private String inNWBFileName;

		public NWBValidationAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        	this.inNWBFileName = (String) data[0].getData();
        }

        public Data[] execute() throws AlgorithmExecutionException {				
			File inData = new File(inNWBFileName);
			ValidateNWBFile validator = new ValidateNWBFile();
			try{ 
				validator.validateNWBFormat(inData);
				if(validator.getValidationResult()) {						
					return createOutData(inData);
				} else {
					throw new AlgorithmExecutionException(
						"Sorry, your file does not comply with the NWB File Format Specification.\n"+
						"Please review the latest NWB File Format Specification at "+
						"http://nwb.cns.iu.edu/doc.html and update your file. \n"+
						validator.getErrorMessages());						
				}

			} catch (FileNotFoundException e) {
				String message =
					"Error: Couldn't find NWB file to validate: "
					+ e.getMessage();
				throw new AlgorithmExecutionException(message, e);						
			} catch (IOException e) {
				String message = "File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);					
			}
        }

		private Data[] createOutData(File inData) {
			Data[] dm = new Data[]{ new BasicData(
					inData, NWBFileProperty.NWB_MIME_TYPE) };
			dm[0].getMetadata().put(
					DataProperty.LABEL, "NWB file: " + inNWBFileName);
			dm[0].getMetadata().put(
					DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return dm;
		}
    }
}






  

    
    
    

