package edu.iu.scipolicy.converter.psraster.postscript.pshandler;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

public class PSHandlerAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public PSHandlerAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // Convert the in-data from mime type file:text/ps to file-ext:ps. 
    public Data[] execute() throws AlgorithmExecutionException {
    	File postScriptFile = (File)data[0].getData();
    	String postScriptFileName = postScriptFile.getName();
    		
    	// "Validate" the file by making sure it exists.
        if (!postScriptFile.exists()) {
        	throw new AlgorithmExecutionException("Unable to find the file \'" +
        		postScriptFileName + "\' file for validation.");
        }

        // Wrap the File object in a Data object. 
        Data postScriptFileData =
        	new BasicData(postScriptFile, PSRasterProperties.PS_FILE_TYPE);
        	
        return new Data[] { postScriptFileData };
    }
}