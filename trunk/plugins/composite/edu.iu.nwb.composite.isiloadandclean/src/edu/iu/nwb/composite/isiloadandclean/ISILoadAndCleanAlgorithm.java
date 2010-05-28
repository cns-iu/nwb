package edu.iu.nwb.composite.isiloadandclean;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.service.log.LogService;

public class ISILoadAndCleanAlgorithm implements Algorithm {
	private LogService logger;
    private Dictionary parameters;
    private CIShellContext ciShellContext;

    private AlgorithmFactory fileLoader;
    private AlgorithmFactory isiToPrefuseConverter;
    private AlgorithmFactory isiDupRemover;

//    private SpecialISILoader isiLoader;
   
    public ISILoadAndCleanAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext context,
    		AlgorithmFactory fileLoader,
    		AlgorithmFactory isiToPrefuseConverter,
    		AlgorithmFactory isiDupRemover) {
        this.parameters = parameters;
        this.ciShellContext = context;

        this.logger = (LogService)context.getService(LogService.class.getName());      

        this.fileLoader = fileLoader;
        this.isiToPrefuseConverter = isiToPrefuseConverter;
        this.isiDupRemover = isiDupRemover;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Data[] loadedData = AlgorithmUtilities.executeAlgorithm(
    		this.fileLoader, this.parameters, this.ciShellContext, null); 
    	
    	if ((loadedData == null) || (loadedData.length == 0)) {
    		return new Data[0];
    	}

    	Data[] convertedToPrefuseData = AlgorithmUtilities.executeAlgorithm(
    		this.isiToPrefuseConverter, this.parameters, this.ciShellContext, loadedData);
    	Data[] duplicatesRemovedData = AlgorithmUtilities.executeAlgorithm(
    		this.isiDupRemover, this.parameters, this.ciShellContext, convertedToPrefuseData);
    	Data[] originalAndDuplicatesRemovedData = new Data[] {
    		convertedToPrefuseData[0], duplicatesRemovedData[0]
		};
    	
    	return originalAndDuplicatesRemovedData;
    }
}