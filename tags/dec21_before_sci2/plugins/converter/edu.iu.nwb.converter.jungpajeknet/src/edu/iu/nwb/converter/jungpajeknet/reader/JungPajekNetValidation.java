package edu.iu.nwb.converter.jungpajeknet.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.io.PajekNetReader;


public class JungPajekNetValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
        return new JungPajekNetValidationAlg(data, parameters, context);
    }
    
    
    public class JungPajekNetValidationAlg implements Algorithm {
        public static final String PAJEK_MIME_TYPE = "file:application/pajek";
        
        private String inNetFileName;
		
        
        public JungPajekNetValidationAlg(Data[] data,
        								 Dictionary parameters,
        								 CIShellContext context) {
        	inNetFileName = (String) data[0].getData();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {	        	
        	File pajekNetFile = new File(inNetFileName);
        	
        	try {
        		(new PajekNetReader()).load(new FileReader(pajekNetFile));
        		return createOutData(inNetFileName, pajekNetFile);
			} catch (FileNotFoundException e) {
				String message =
					"Could find the Pajek .net file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (Exception e) {
				String message = "Unexpected error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);	
			}
        }

		private Data[] createOutData(String fileName, File inData) {
			Data[] dm =
				new Data[]{ new BasicData(inData, PAJEK_MIME_TYPE) };
			dm[0].getMetadata().put(
					DataProperty.LABEL, "Pajek .net file: " + fileName);
			dm[0].getMetadata().put(
					DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return dm;
		}
    }
}