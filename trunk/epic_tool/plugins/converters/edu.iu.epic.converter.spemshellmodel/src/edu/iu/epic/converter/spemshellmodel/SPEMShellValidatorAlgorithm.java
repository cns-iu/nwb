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
    		Dictionary parameters,
    		CIShellContext ciShellContext) {
    	this.inMDLFilePath = (String) data[0].getData();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	File inputData = new File(inMDLFilePath);

		return createOutData(inputData);
    }
    
    private Data[] createOutData(File inputData) {
		Data[] validationData = new Data[]{ new BasicData(inputData, "file:text/mdl") };
		validationData[0].getMetadata().put(DataProperty.LABEL, "SPEMShell model: " + inMDLFilePath);
		validationData[0].getMetadata().put(DataProperty.TYPE, DataProperty.OTHER_TYPE);
		return validationData;
	}
}