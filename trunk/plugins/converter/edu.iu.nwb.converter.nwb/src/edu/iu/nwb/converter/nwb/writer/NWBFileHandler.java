package edu.iu.nwb.converter.nwb.writer;

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

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBFileHandler implements AlgorithmFactory {

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new NWBFileHandlerAlgorithm(data, parameters, context);
    }
    
    public class NWBFileHandlerAlgorithm implements Algorithm {
    	private Object inData;
		private String format;
        
        
        public NWBFileHandlerAlgorithm(
        		Data[] data, Dictionary parameters, CIShellContext context) {
        	this.inData = data[0].getData();
        	this.format = data[0].getFormat();	    
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	if (inData instanceof File) {
        		if (NWBFileProperty.NWB_MIME_TYPE.equals(format)) {        		
					ValidateNWBFile validator = new ValidateNWBFile();
					
					try { 
						validator.validateNWBFormat((File)inData);
						if (validator.getValidationResult()) {
							return new Data[]{ new BasicData(
									inData, NWBFileProperty.NWB_FILE_TYPE) };
						} else {
							throw new AlgorithmExecutionException(
								"This file does not seem to comply with the NWB "
								+ "format specification. Forwarding anyways.\n"
								+ "Please notify the developers of the algorithm "
								+ "you are using or the NWB team.");
						}
					} catch (FileNotFoundException e) {
						String message =
							"Couldn't find NWB file to validate: " + e.getMessage();
						throw new AlgorithmExecutionException(message, e);
					} catch (IOException e) {
						String message = "File access error: " + e.getMessage();
						throw new AlgorithmExecutionException(message, e);					
					}
        		} else {
        			String message =
        				"Error: Expected " + NWBFileProperty.NWB_MIME_TYPE
        				+ ", but the input format is " + format;
        			throw new AlgorithmExecutionException(message);
        		}
        	} else {
        		String message =
        			"Expect a File, but the input data is "
    				+ inData.getClass().getName();
        		throw new AlgorithmExecutionException(message);
        	}
        }
    }
}