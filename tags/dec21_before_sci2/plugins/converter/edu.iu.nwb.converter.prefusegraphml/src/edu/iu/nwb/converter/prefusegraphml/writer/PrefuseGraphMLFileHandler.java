package edu.iu.nwb.converter.prefusegraphml.writer;

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
public class PrefuseGraphMLFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLFileHandlerAlg(data);
    }
    
    public class PrefuseGraphMLFileHandlerAlg implements Algorithm {
		public static final String XML_FILE_EXT = "file-ext:xml";
		public static final String GRAPHML_MIME_TYPE = "file:text/graphml+xml";
		
		private Object inData;
		private String inFormat;
        
		
        public PrefuseGraphMLFileHandlerAlg(Data[] data) {
			inData = data[0].getData();
			inFormat = data[0].getFormat();
        }

        
        public Data[] execute() throws AlgorithmExecutionException{
        	if (inData instanceof File) {
        		if (inFormat.equals(GRAPHML_MIME_TYPE)) {
        			return new Data[]{new BasicData(inData, XML_FILE_EXT)};
        		} else {
        			throw new AlgorithmExecutionException(
        					"Expected file:text/graphml+xml, "
        					+ "but the input format is " + inFormat);
        		}
        	} else {
        		throw new AlgorithmExecutionException(
    					"Expected a File, but the input data is "
    					+ inData.getClass().getName());
        	}    	
        }
    }
}