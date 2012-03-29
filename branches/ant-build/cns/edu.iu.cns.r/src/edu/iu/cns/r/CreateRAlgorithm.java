package edu.iu.cns.r;

import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.cns.r.utility.RInstance;

public class CreateRAlgorithm implements Algorithm {
    private String rHome;
    private LogService logger;
    
    public CreateRAlgorithm(String rHome, LogService logger) {
        this.rHome = rHome;
        this.logger = logger;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	RInstance rInstance = new RInstance(rHome, this.logger);

        return wrapRInstanceAsOutputData(rInstance);
    }

    private static Data[] wrapRInstanceAsOutputData(RInstance rInstance) {
    	Data rDatum = new BasicData(rInstance, rInstance.getClass().getName());
    	Dictionary<String, Object> metadata = rDatum.getMetadata();
    	metadata.put(DataProperty.LABEL, "R Instance");
    	metadata.put(DataProperty.TYPE, DataProperty.R_INSTANCE_TYPE);

    	return new Data[] { rDatum };
    }
}