package edu.iu.epic.converter.spemshellmodel;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class SPEMShellValidatorAlgorithm implements Algorithm {
    private String inMDLFilePath;
    
    public SPEMShellValidatorAlgorithm(
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext ciShellContext) {
    	this.inMDLFilePath = (String) data[0].getData();
    }

    /* TODO Note that this is currently completely trivial.  No validation
     * of any kind is performed.  This is just for testing SPEMShell integration
     * (we need to be able to load .mdl files into the data manager).
     */
    public Data[] execute() throws AlgorithmExecutionException {
    	File inputData = new File(inMDLFilePath);

		return createOutData(inputData);
    }
    
    @SuppressWarnings("unchecked") // TODO
	private Data[] createOutData(File inputData) {
		Data[] validationData = new Data[]{ new BasicData(inputData, "file:text/mdl") };
		Dictionary<String, Object> metadata = validationData[0].getMetadata();
		metadata.put(DataProperty.LABEL, "Model: " + inMDLFilePath);
		metadata.put(DataProperty.TYPE, DataProperty.OTHER_TYPE);
		return validationData;
	}
}