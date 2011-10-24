package edu.iu.nwb.converter.text;

import java.io.File;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

public class TextHandlerAlgorithm implements Algorithm {
    private File textFile;
    
    public TextHandlerAlgorithm(File textFile) {
        this.textFile = textFile;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	// TODO catch null, wrap in AEE
    	if (!textFile.exists()) {
    		throw new AlgorithmExecutionException("Unable to find the file '"
    				+ textFile.getName() + "' for validation.");
    	}
    	
    	
    	// TODO extract constant
        return new Data[] { new BasicData(textFile, "file-ext:txt") }; 
    }
}