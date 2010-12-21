package edu.iu.nwb.converter.prefusexgmml.writer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLFileHandler implements AlgorithmFactory { 
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
        return new PrefuseXGMMLFileHandlerAlgorithm(data, parameters, context);
    }

    public class PrefuseXGMMLFileHandlerAlgorithm implements Algorithm {
        public static final String XML_FILE_EXT = "file-ext:xml";
		public static final String XGMML_MIME_TYPE = "file:text/xgmml+xml";
		
		private Object inData;
		private String inFormat;
		
        
        public PrefuseXGMMLFileHandlerAlgorithm(Data[] data,
        								  Dictionary parameters,
        								  CIShellContext context) {
			this.inData = data[0].getData();
			this.inFormat = data[0].getFormat();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	if (inData instanceof File) {
        		if (inFormat.equals(XGMML_MIME_TYPE)) {
        			return new Data[]{new BasicData(inData, XML_FILE_EXT)};   
        		} else {
        			throw new AlgorithmExecutionException(
        				"Expect file:text/xgmml+xml, but the input format is "
    					+ inFormat);
        		}
        	} else {
        		throw new AlgorithmExecutionException(
        				"Expect a File, but the input data is "
    					+ inData.getClass().getName());
        	}
        }
    }
}