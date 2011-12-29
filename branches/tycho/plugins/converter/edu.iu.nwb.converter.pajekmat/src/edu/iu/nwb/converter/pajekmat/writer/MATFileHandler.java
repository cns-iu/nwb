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
import edu.iu.nwb.converter.pajekmat.common.MATFileValidator;

/**
 * @author Timothy Kelley
 *
 */
public class MATFileHandler implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new MATFileHandlerAlg(data, parameters, context);
	}
	
	public class MATFileHandlerAlg implements Algorithm {
		private File inMatFile;
		
        
        public MATFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
        	inMatFile = (File) data[0].getData();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	try {
				MATFileValidator validator = new MATFileValidator();
				validator.validateMATFormat(inMatFile);
				if (validator.getValidationResult()){
					return new Data[]{ new BasicData(
							inMatFile, MATFileProperty.MAT_FILE_TYPE) };  
				} else {
					String message =
						"Sorry, your file does not seem to comply with the "
						+ "Pajek .mat File Format Specification."
						+ "We are writing it out anyway.\n"
						+ "Please review the latest NET File Format "
						+ "Specification at "
						+ "http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf"
						+ ", and update your file." 
						+ validator.getErrorMessages();
					throw new AlgorithmExecutionException(message);
				}				
			} catch (FileNotFoundException e) {
				String message = "Couldn't find Pajek .mat file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (IOException e) {
				String message = "File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
        }
    }
}

