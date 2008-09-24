package edu.iu.nwb.converter.prefusegraphml.writer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLFileHandlerAlg(data, parameters, context);
    }
    
    public class PrefuseGraphMLFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseGraphMLFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
            logger=(LogService)context.getService(LogService.class.getName());
        }

        public Data[] execute() throws AlgorithmExecutionException{
        	Object inData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inData instanceof File && format.equals("file:text/graphml+xml")){
        		return new Data[]{new BasicData(inData, "file-ext:xml")};          		
        	}
        	else {
        		if (!(inData instanceof File))        				
        			throw new AlgorithmExecutionException("Expected a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals("file:text/graphml+xml"))
        			throw new AlgorithmExecutionException("Expected file:text/graphml+xml, but the input format is "+format);
       			throw new AlgorithmExecutionException("Either not a file or format is not graphml");
        	}     	
        }
    }
}