package edu.iu.scipolicy.converter.psraster.jpg.jpgwriter;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import javax.imageio.ImageIO;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;


public class JPGWriter implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    LogService logger;
    
    public JPGWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    // The in-data should be a BufferedImage object with mime type
    // java.awt.image.BufferedImage.
    // Write the BufferedImage in-data to a temporary file on disk.
    // That temporary file on disk is the out-data, with mime type file:text/jpg.
    public Data[] execute() throws AlgorithmExecutionException {
    	// The in-data is a BufferedImage, so get it.
    	BufferedImage imageToWriteToDisk = (BufferedImage)data[0].getData();
    	
    	// If necessary, create the temporary directory in which our file will
    	// placed.
	    String temporaryDirectoryPath = System.getProperty("java.io.tmpdir");
	    File temporaryDirectory = createTemporaryDirectory(temporaryDirectoryPath);
	    
	    // Try creating the temporary (jpg) file in the temporary directory.
	    File temporaryJPGFile = createTemporaryFile(temporaryDirectory,
	    											temporaryDirectoryPath,
	    											"jpg");
	    
	    // Throw an exception if we failed to create the temporary file.
	    if (temporaryJPGFile == null) {
	    	throw new AlgorithmExecutionException
	    		("Failed to generate a file in the temporary directory.");
	    }

	    // Attempt to write the image to the temporary file on disk.
   		try {
   			ImageIO.write(imageToWriteToDisk, "jpg", temporaryJPGFile);
   		}
   		catch (IOException ioe) {
   			throw new AlgorithmExecutionException(ioe);
   		}
   		
   		// It is now safe to create the Data that we're returning...
   		Data jpgFileData =
    		new BasicData(temporaryJPGFile, PSRasterProperties.JPG_MIME_TYPE);
   		
    	return new Data[] { jpgFileData };
    }
    
    // Return a File pointing to the directory specified in temporaryDirectoryPath,
    // creating the directory if it doesn't already exist.
    private File createTemporaryDirectory(String temporaryDirectoryPath) {
    	File temporaryDirectory =
    		new File(temporaryDirectoryPath + File.separator + "temp");
    	
    	if (!temporaryDirectory.exists())
    		temporaryDirectory.mkdir();
    	
    	return temporaryDirectory;
    }
    
    // Attempt to create a temporary file on disk whose name is passed in.
    private File createTemporaryFile(File temporaryDirectory,
    								 String temporaryDirectoryPath,
    								 String temporaryFileExtension) {
    	File temporaryFile;
    	
    	try {
    		temporaryFile = File.createTempFile("NWB-Session-",
    											temporaryFileExtension,
    											temporaryDirectory);
    	}
    	catch (IOException e) {
    		// We couldn't make the temporary jpg file in the temporary directory
	    	// using the standard Java File temporary file scheme (?), so we're
    		// coming up with our own temporary file (that we hope doesn't already
    		// exist).
	    	temporaryFile = new File (temporaryDirectoryPath + File.separator +
	    		"nwbTemp" + File.separator + "temp." + temporaryFileExtension);
    	}
    	
    	return temporaryFile;
    }
}