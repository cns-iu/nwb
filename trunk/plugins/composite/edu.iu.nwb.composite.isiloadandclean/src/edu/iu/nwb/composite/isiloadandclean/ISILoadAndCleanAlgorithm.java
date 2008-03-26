package edu.iu.nwb.composite.isiloadandclean;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.composite.isiloadandclean.loading.SWTISILoader;
import edu.iu.nwb.composite.isiloadandclean.loading.SpecialISILoader;

public class ISILoadAndCleanAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService log;
    
    private SpecialISILoader isiLoader;
    
    private AlgorithmFactory isiValidator;
    private AlgorithmFactory isiToPrefuseConverter;
    private AlgorithmFactory isiDupRemover;
   
    public ISILoadAndCleanAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
    		AlgorithmFactory isiValidator, AlgorithmFactory isiToPrefuseConverter,
    		AlgorithmFactory isiDupRemover) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.log = (LogService)context.getService(LogService.class.getName());      
        
        //Change this if you want to do it with a web gui
        this.isiLoader = new SWTISILoader();
        
        this.isiValidator = isiValidator;
        this.isiToPrefuseConverter = isiToPrefuseConverter;
        this.isiDupRemover = isiDupRemover;
        
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	Data[] loadedISIData = isiLoader.getISIDataFromUser();
    	
    	if (loadedISIData == null || loadedISIData.length == 0) {
    		throw new AlgorithmExecutionException("input data was either null or empty");
    	}
    
    	Data[] validatedISIData = executeAlgorithm(isiValidator, loadedISIData);
    	
    	Data[] convertedToPrefuseISIData = executeAlgorithm(isiToPrefuseConverter, validatedISIData);
    	
    	Data[] dupRemovedISIData = executeAlgorithm(isiDupRemover, convertedToPrefuseISIData);
    	
    	Data[] origAndDupRemovedISIData = new Data[]{convertedToPrefuseISIData[0], dupRemovedISIData[0]};
    	
    	return origAndDupRemovedISIData;
    }
    
    private Data[] executeAlgorithm(AlgorithmFactory algFactory, Data[] data) throws AlgorithmExecutionException{
    	Algorithm alg = algFactory.createAlgorithm(data, parameters, context);
    	Data[] result = alg.execute();
    	return result;
    }
}