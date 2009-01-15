package edu.iu.scipolicy.converter.psraster.fileutilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileUtilities {
	// Return a File pointing to the directory specified in temporaryDirectoryPath,
    // creating the directory if it doesn't already exist.
    public static File createTemporaryDirectory(String temporaryDirectoryPath) {
    	File temporaryDirectory =
    		new File(temporaryDirectoryPath + File.separator + "temp");
    	
    	if (!temporaryDirectory.exists())
    		temporaryDirectory.mkdir();
    	
    	return temporaryDirectory;
    }
    
    // Attempt to create a temporary file on disk whose name is passed in.
    public static File createTemporaryFile(File temporaryDirectory,
    								 String temporaryDirectoryPath,
    								 String temporaryFileName,
    								 String temporaryFileExtension) {
    	File temporaryFile;
    	
    	try {
    		temporaryFile = File.createTempFile("NWB-Session-" + temporaryFileName,
    											temporaryFileExtension,
    											temporaryDirectory);
    	}
    	catch (IOException e) {
    		// We couldn't make the temporary file in the temporary directory
	    	// using the standard Java File temporary file scheme (?), so we're
    		// coming up with our own temporary file (that we hope doesn't already
    		// exist).
	    	temporaryFile = new File (temporaryDirectoryPath + File.separator +
	    		temporaryFileName + File.separator + "temp." +
	    		temporaryFileExtension);
    	}
    	
    	return temporaryFile;
    }
    
    // Attempt to create a temporary file on disk in a temporary directory (that may
    // also be created, if necessary).
    public static File createTemporaryFileInTemporaryDirectory
    		(String temporaryDirectoryPath,
    		 String temporaryFileName,
    	 	 String temporaryFileExtension) throws IOException {
    	// Get/create the temporary directory.
    	File temporaryDirectory = createTemporaryDirectory(temporaryDirectoryPath);
    	
    	// Attempt to create the temporary file now.
    	File temporaryFile = createTemporaryFile(temporaryDirectory,
    											 temporaryDirectoryPath,
    											 temporaryFileName,
    											 temporaryFileExtension);
    	
    	// If the creation of the temporary file failed, throw an exception.
    	if (temporaryFile == null) {
    		throw new IOException
    			("Failed to generate a file in the temporary directory.");
    	}
    	
    	return temporaryFile;
    }
    
    public static File writeBufferedImageToTemporaryFileInTemporaryDirectory
    		(BufferedImage bufferedImage,
    		 String imageType) throws IOException, Exception {
    	// Get the system-wide temporary directory path.
	    String temporaryDirectoryPath = System.getProperty("java.io.tmpdir");
	    File temporaryImageFile =
	    	createTemporaryFileInTemporaryDirectory(temporaryDirectoryPath,
	    											"nwb-temp",
	    											"jpg");

	    // Attempt to write the image to the temporary file on disk.
   		if (!ImageIO.write(bufferedImage, imageType, temporaryImageFile)) {
   			throw new Exception
   				("No valid image writer was found for the image type " + imageType);
   		}
   		
   		return temporaryImageFile;
    }
}