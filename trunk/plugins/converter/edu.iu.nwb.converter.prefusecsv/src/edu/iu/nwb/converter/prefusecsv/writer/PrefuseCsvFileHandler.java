package edu.iu.nwb.converter.prefusecsv.writer;

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
public class PrefuseCsvFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseCSVFileHandlerAlgorithm(data, parameters, context);
    }
    
    public class PrefuseCSVFileHandlerAlgorithm implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseCSVFileHandlerAlgorithm(Data[] inputData, Dictionary parameters, CIShellContext ciShellContext) {
            this.data = inputData;
            this.parameters = parameters;
            this.context = ciShellContext;
            logger=(LogService)ciShellContext.getService(LogService.class.getName());
        }

        public Data[] execute() throws AlgorithmExecutionException {
        	Object inputData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inputData instanceof File && format.equals("file:text/csv")){
        		return new Data[]{new BasicData(inputData, "file-ext:csv")};          		
        	}
        	else {
        		if (!(inputData instanceof File))        				
        			throw new AlgorithmExecutionException("Expect a File, but the input data is "+inputData.getClass().getName());
        		else if (!format.equals("file:text/csv"))
        			throw new AlgorithmExecutionException("Expect file:text/csv, but the input format is "+format);
        		throw new AlgorithmExecutionException("");
        	}     	

        }
    }
}