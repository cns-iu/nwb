/**
 * 
 */
package edu.iu.nwb.converter.pajekmat.reader;

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

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.ValidateMATFile;

/**
 * @author Timothy Kelley
 *
 */
public class MATValidation implements AlgorithmFactory {

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		// TODO Auto-generated method stub
		return new MATValidationAlg(data, parameters, context);
	}

	
	public class MATValidationAlg implements Algorithm {
        
    	Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
        
        public MATValidationAlg(Data[] dm, Dictionary parameters, CIShellContext context) {
        	this.data = dm;
            this.parameters = parameters;
            this.ciContext = context;
            logger = (LogService)ciContext.getService(LogService.class.getName());
        }

        public Data[] execute() throws AlgorithmExecutionException {
	    

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
			ValidateMATFile validator = new ValidateMATFile();
			try{ 
				validator.validateMATFormat(inData);
				if(validator.getValidationResult()){						
					Data[] dm = new Data[] {new BasicData(inData, MATFileProperty.MAT_MIME_TYPE)};
					dm[0].getMetadata().put(DataProperty.LABEL, "Pajek .mat file: " + fileHandler);
					dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
                	return dm;

				}else {
					//System.out.println(">>>wrong format: "+validator.getErrorMessages());
					logger.log(LogService.LOG_ERROR,"Sorry, your file does not comply with the .mat File Format Specification.\n"+
							"Please review the latest NET File Format Specification at "+
							"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n\n"+
							validator.getErrorMessages());
					return null;
				}

			}catch (FileNotFoundException e){
				throw new AlgorithmExecutionException("Could not find the Pajek .mat file to validate.",e);	
			}catch (IOException ioe){
				throw new AlgorithmExecutionException("IO Errors while reading the specified Pajek .mat file.",ioe);
			}
        }
      

    }

}
