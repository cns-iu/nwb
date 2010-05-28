package edu.iu.nwb.composite.isiloadandclean;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;

public class ISILoadAndCleanAlgorithm implements Algorithm, ProgressTrackable {
    private Dictionary<String, Object> parameters;
    private CIShellContext ciShellContext;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

    private AlgorithmFactory fileLoader;
    private AlgorithmFactory isiToPrefuseConverter;
    private AlgorithmFactory isiDupRemover;

    
    @SuppressWarnings("unchecked")	// Dictionary<String, Object>
    public ISILoadAndCleanAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext context,
    		AlgorithmFactory fileLoader,
    		AlgorithmFactory isiToPrefuseConverter,
    		AlgorithmFactory isiDupRemover) {
        this.parameters = parameters;
        this.ciShellContext = context;

        this.fileLoader = fileLoader;
        this.isiToPrefuseConverter = isiToPrefuseConverter;
        this.isiDupRemover = isiDupRemover;
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	if (progressMonitor != null) {
    		this.progressMonitor = progressMonitor;
    	}
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Data[] loadedData = AlgorithmUtilities.executeAlgorithm(
    		this.fileLoader, this.progressMonitor, null, this.parameters, this.ciShellContext); 
    	
    	if ((loadedData == null) || (loadedData.length == 0)) {
    		return new Data[0];
    	}

    	Data[] convertedToPrefuseData = AlgorithmUtilities.executeAlgorithm(
    		this.isiToPrefuseConverter,
    		this.progressMonitor,
    		loadedData,
    		this.parameters,
    		this.ciShellContext);
    	Data[] duplicatesRemovedData = AlgorithmUtilities.executeAlgorithm(
    		this.isiDupRemover,
    		this.progressMonitor,
    		convertedToPrefuseData,
    		this.parameters,
    		this.ciShellContext);
    	Data[] originalAndDuplicatesRemovedData = new Data[] {
    		convertedToPrefuseData[0], duplicatesRemovedData[0]
		};
    	
    	return originalAndDuplicatesRemovedData;
    }
}