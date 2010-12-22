package edu.iu.sci2.converter.psraster.postscript.psvalidator;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;

public class PSValidatorAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public PSValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // Convert the in-data from mime type file-ext:ps to file:text/ps. 
    public Data[] execute() throws AlgorithmExecutionException {
    	// Get the name of the file that the user chose.
    	String postScriptFileName = (String)data[0].getData();
    	File postScriptFile = new File(postScriptFileName);
    	
    	// "Validate" the file by making sure it exists.
    	if (!postScriptFile.exists()) {
    		throw new AlgorithmExecutionException("Unable to find the file \'" +
    			postScriptFileName + "\' file for validation.");
    	}
    	
    	// Wrap the File object in a Data object, and assign labels to it
    	// appropriately. 
    	Data postScriptFileData =
    		new BasicData(postScriptFile, PSRasterProperties.PS_MIME_TYPE);
    	//TODO: rename variable
    	Dictionary postScriptFileDataMetadata = postScriptFileData.getMetadata();
    	
    	postScriptFileDataMetadata.put(DataProperty.LABEL,
    								   "PostScript file: " + postScriptFileName);
    	postScriptFileDataMetadata.put(DataProperty.TYPE,
    								   DataProperty.VECTOR_IMAGE_TYPE);
    	
        return new Data[] { postScriptFileData };
    }
}