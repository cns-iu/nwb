package edu.iu.sci2.converter.psraster.jpg.jpgwriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;


public class JPGWriterAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public JPGWriterAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    // The in-data should be a BufferedImage object with mime type
    // java.awt.image.BufferedImage.
    // Write the BufferedImage in-data to a temporary file on disk.
    // That temporary file on disk is the out-data, with mime type file:text/jpg.
    public Data[] execute() throws AlgorithmExecutionException {
    	// The in-data is a BufferedImage, so get it.
    	BufferedImage imageToWriteToDisk = (BufferedImage)data[0].getData();
    	
    	File temporaryJPGFile = null;
    	
    	try {
    		temporaryJPGFile =
    			FileUtilities.writeBufferedImageIntoTemporaryDirectory
    				(imageToWriteToDisk, "jpg");
    	}
    	catch (IOException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	catch (Exception e) {
    		throw new AlgorithmExecutionException(e);
    	}
   		
   		// It is now safe to create the Data that we're returning...
   		Data jpgFileData =
    		new BasicData(temporaryJPGFile, PSRasterProperties.JPG_MIME_TYPE);
   		
    	return new Data[] { jpgFileData };
    }
}