package edu.iu.epic.converter.spemshellmodel;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class SPEMShellValidatorAlgorithm implements Algorithm {
    public static final String MODEL_FILE_MIME_TYPE = "file:text/mdl";
	private String inMDLFilePath;
    
	
	public static class Factory implements AlgorithmFactory {
		@SuppressWarnings("unchecked") // Raw Dictionary
		public Algorithm createAlgorithm(
				Data[] data,
				Dictionary parameters,
				CIShellContext ciShellContext) {
	        return new SPEMShellValidatorAlgorithm(data);
	    }
	}
	
    public SPEMShellValidatorAlgorithm(Data[] data) {
    	this.inMDLFilePath = (String) data[0].getData();
    }

    
    /* TODO Note that this is currently completely trivial.  No validation
     * of any kind is performed.  This is just for testing SPEMShell integration
     * (we need to be able to load .mdl files into the data manager).
     */
    public Data[] execute() {
		return createOutData(new File(this.inMDLFilePath));
    }
    
    @SuppressWarnings("unchecked") // Dictionary conversion
	private Data[] createOutData(File inputData) {
		Data[] validationData =
			new Data[]{ new BasicData(inputData, MODEL_FILE_MIME_TYPE) };
		Dictionary<String, Object> metadata = validationData[0].getMetadata();
		metadata.put(DataProperty.LABEL, "Model: " + this.inMDLFilePath);
		metadata.put(DataProperty.TYPE, DataProperty.OTHER_TYPE);
		return validationData;
	}
}