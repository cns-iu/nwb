package edu.iu.sci2.converter.psraster.jpg.jpghandler;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGHandlerAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public JPGHandlerAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // Convert the in-data from mime type file:text/jpg to file-ext:jpg. 
    public Data[] execute() throws AlgorithmExecutionException {
    	File jpgFile = (File) this.data[0].getData();
    	String jpgFileName = jpgFile.getName();
    		
    	// "Validate" the file by making sure it exists.
        if (!jpgFile.exists()) {
        	throw new AlgorithmExecutionException("Unable to find the file \'" +
        		jpgFileName + "\' file for validation.");
        }

        Data jpgFileData =
        	new BasicData(jpgFile, PSRasterProperties.JPG_FILE_TYPE);
        	
        return new Data[] { jpgFileData };
    }
}