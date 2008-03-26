/**
 * 
 */
package edu.iu.nwb.converter.pajekmat.writer;

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
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.ValidateMATFile;

/**
 * @author Timothy Kelley
 *
 */
public class MATFileHandler implements AlgorithmFactory {

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		// TODO Auto-generated method stub
		return new MATFileHandlerAlg(data, parameters, context);
	}
	
	public class MATFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
        
        public MATFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.ciContext = context;
            logger = (LogService)ciContext.getService(LogService.class.getName());

        }

        public Data[] execute() throws AlgorithmExecutionException {

        	Object inData = data[0].getData();
        	ValidateMATFile validator = new ValidateMATFile();
				try{ 
					validator.validateMATFormat((File)inData);
					if(validator.getValidationResult()){
						return new Data[]{new BasicData(inData, MATFileProperty.MAT_FILE_TYPE)};  
					}else {
						logger.log(LogService.LOG_WARNING, 
								"Sorry, your file does not seem to comply with the Pajek .mat File Format Specification." +
								"We are writing it out anyway.\n"+
								"Please review the latest NET File Format Specification at "+
								"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file." 
								+ validator.getErrorMessages());
						return null;
					}
					
				}catch (FileNotFoundException e){
					throw new AlgorithmExecutionException("Could not find the given .mat file to validate",e);	
				}catch (IOException ioe){
					throw new AlgorithmExecutionException("Errors reading the given .mat file.", ioe);
				}        		        		
        	}

        }
    }

