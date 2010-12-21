package edu.iu.scipolicy.converter.psraster.jpg.jpgvalidator;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGValidatorAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public JPGValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // The in-data should be a file name (string) of a jpg or jpeg file.
    // Attempt to create and output a File object of the given file name.
    public Data[] execute() throws AlgorithmExecutionException {
    	// Get the name of the file that the user chose.
    	String jpgFileName = (String)data[0].getData();
    	File jpgFile = new File(jpgFileName);
    	
    	// "Validate" the file by making sure it exists.
    	if (!jpgFile.exists()) {
    		throw new AlgorithmExecutionException("Unable to find the file \'" +
    			jpgFileName + "\' file for validation.");
    	}

    	// Wrap the File object in a Data object, and add metadata to it
    	// appropriately. 
    	Data jpgFileData =
    		new BasicData(jpgFile, PSRasterProperties.JPG_MIME_TYPE);
    	Dictionary jpgFileMetadata = jpgFileData.getMetadata();
    	
    	jpgFileMetadata.put(DataProperty.LABEL,
    								   "JPG file: " + jpgFileName);
    	jpgFileMetadata.put(DataProperty.TYPE,
    								   DataProperty.RASTER_IMAGE_TYPE);
    	
        return new Data[] { jpgFileData };
    }
}