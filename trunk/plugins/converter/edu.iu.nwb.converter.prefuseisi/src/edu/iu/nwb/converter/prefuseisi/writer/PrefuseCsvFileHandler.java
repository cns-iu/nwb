package edu.iu.nwb.converter.prefuseisi.writer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseCsvFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLFileHandlerAlg(data);
    }
    
    public class PrefuseGraphMLFileHandlerAlg implements Algorithm {
		public static final String CSV_MIME_TYPE = "file:text/csv";
		public static final String CSV_FILE_EXT = "file-ext:csv";
		
		private Object inData;
		private String inFormat;
        
		
        public PrefuseGraphMLFileHandlerAlg(Data[] data) {
			this.inData = data[0].getData();
			this.inFormat = data[0].getFormat();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	if (inData instanceof File) {
        		if (inFormat.equals(CSV_MIME_TYPE)) {
        			return new Data[]{ new BasicData(inData, CSV_FILE_EXT) };          		
        		} else {
        			throw new AlgorithmExecutionException(
        				"Expected " + CSV_MIME_TYPE
        				+ ", but the input format is " + inFormat);
        		}
        	} else {
        		throw new AlgorithmExecutionException(
        				"Expected a file, but the input data is of type "
        				+ inData.getClass().getName());
        	}
        }
    }
}