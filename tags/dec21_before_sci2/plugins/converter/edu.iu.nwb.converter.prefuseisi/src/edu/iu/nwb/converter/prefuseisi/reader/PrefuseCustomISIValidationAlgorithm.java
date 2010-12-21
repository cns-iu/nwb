package edu.iu.nwb.converter.prefuseisi.reader;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;

/**
 * The algorithm will create two Prefuse dataset. The first dataset 
 * contains the original ISI records. The second data contains the 
 * unique records of the origin.
 * @author kongch
 */
public class PrefuseCustomISIValidationAlgorithm implements Algorithm {

	private Dictionary<String, Object> parameters;
    private CIShellContext ciShellContext;
    private AlgorithmFactory isiPrefuseValidator;
    private AlgorithmFactory isiToPrefuseConverter;
    private AlgorithmFactory isiDupRemover;
    private Data[] data;

    
	public PrefuseCustomISIValidationAlgorithm(
    		Data[] data, 
    		Dictionary<String, Object> parameters,	
    		CIShellContext context,
    		AlgorithmFactory isiPrefuseValidator,
    		AlgorithmFactory isiToPrefuseConverter,
    		AlgorithmFactory isiDupRemover) {
    	this.data = data;
        this.parameters = parameters;
        this.ciShellContext = context;
        this.isiPrefuseValidator = isiPrefuseValidator;
        this.isiToPrefuseConverter = isiToPrefuseConverter;
        this.isiDupRemover = isiDupRemover;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	if ((this.data == null) || (this.data.length == 0)) {
    		return new Data[0];
    	}

    	/* validate with Prefuse validator */
    	Data[] validatorData = AlgorithmUtilities.executeAlgorithm(
        		this.isiPrefuseValidator,
        		this.data,
        		this.parameters,
        		this.ciShellContext);
    	
    	/* Generate data using Prefuse Data Reader */
    	Data[] convertedToPrefuseData = AlgorithmUtilities.executeAlgorithm(
    		this.isiToPrefuseConverter,
    		validatorData,
    		this.parameters,
    		this.ciShellContext);
    	
    	/* Remove duplication records */
    	Data[] duplicatesRemovedData = AlgorithmUtilities.executeAlgorithm(
    		this.isiDupRemover,
    		convertedToPrefuseData,
    		this.parameters,
    		this.ciShellContext);
    	
    	/* Create the returned data array */ 
    	Data[] originalAndDuplicatesRemovedData = new Data[] {
    		convertedToPrefuseData[0], duplicatesRemovedData[0]
		};
    	
    	return originalAndDuplicatesRemovedData;
    }
}
