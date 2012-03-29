package edu.iu.sci2.converter.psraster.jpg.jpgreader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.ImageUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.sci2.converter.psraster.jpg.jpgreader.JPGReaderAlgorithm;
import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGReaderAlgorithmTest extends TestCase {
	private File temporaryInvalidJPGFileToBeRead;
	private File temporaryValidJPGFileToBeRead;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// Create the invalid jpg file that we will read later.
		this.temporaryInvalidJPGFileToBeRead = createTemporaryInvalidJPGFile();
		
		// Create the valid jpg file that we will read later.
	    this.temporaryValidJPGFileToBeRead = createTemporaryValidJPGFile();
	}

	@After
	public void tearDown() throws Exception {
		// Delete our temporary files that we created in setUp().
		this.temporaryInvalidJPGFileToBeRead.delete();
		this.temporaryValidJPGFileToBeRead.delete();
	}

	@Test
	public void testExecuteWithNonExistentFile() {
		// Create a JPGReader with invalid (file) in-data.
		JPGReaderAlgorithm jpgReader =
			createJPGReader("gobildy_gook" + File.separator + "file1234abcd.jpg");
		
		// This test succeeds if an exception is thrown when calling
		// jpgReader.execute().  This is to keep track of whether or not that happens.
		boolean testSucceeded = false;
		
		try {
			jpgReader.execute();
		}
		catch (AlgorithmExecutionException e) {
			// We caught an exception, so the test succeeded.
			testSucceeded = true;
		}
		
		assertTrue(testSucceeded);
	}
	
	@Test
	public void testExecuteWithInvalidJPGFile() {
		// Create a JPGReader with valid (file) in-data that isn't a valid JPG file.
		JPGReaderAlgorithm jpgReader = createJPGReader(this.temporaryInvalidJPGFileToBeRead);
		
		// This test succeeds if an exception is NOT thrown when calling
		// jpgReader.execute().  This is to keep track of whether or not that
		// happens.
		boolean testFailed = false;
		
		try {
			jpgReader.execute();
		}
		catch (AlgorithmExecutionException e) {
			// We caught an exception, so the test failed.
			testFailed = true;
		}
		
		assertFalse(testFailed);
	}
	
	@Test
	public void testExecuteWithValidJPGFile() {
		// Create a JPGReader with valid (file) in-data.
		JPGReaderAlgorithm jpgReader = createJPGReader(this.temporaryValidJPGFileToBeRead);
		
		// This test succeeds if an exception is NOT thrown when calling
		// jpgReader.execute().  This is to keep track of whether or not that
		// happens.
		boolean testFailed = false;
		
		try {
			jpgReader.execute();
		}
		catch (AlgorithmExecutionException e) {
			// We caught an exception, so the test failed.
			testFailed = true;
		}
		
		assertFalse(testFailed);
	}
	
	// Create a JPGReader given a file to be used as in-data.
	private JPGReaderAlgorithm createJPGReader(File inDataFile) {
		// Create in-data containing the file.
		Data fileData = new BasicData(inDataFile, PSRasterProperties.JPG_MIME_TYPE);
		
		// Create a JPGReader with the file in-data.
		JPGReaderAlgorithm jpgReader = new JPGReaderAlgorithm(new Data[] { fileData }, null, null);
		
		return jpgReader;
	}
	
	// Create a JPGReader given the file name of a file to be used as in-data.
	private JPGReaderAlgorithm createJPGReader(String inDataFileName) {
		// Create the file to be used as in-data.
		File inDataFile = new File(inDataFileName);
		
		return createJPGReader(inDataFile);
	}
	
	private File createTemporaryInvalidJPGFile() throws Exception {
		// Get the system-wide temporary directory path.
	    String temporaryDirectoryPath = System.getProperty("java.io.tmpdir");
	    
	    File temporaryImageFile =
	    	FileUtilities.createTemporaryFileInTemporaryDirectory
	    		(temporaryDirectoryPath, "nwb-temp", "jpg");
	    
	    // Write some text out to the temporary "image" file.
	    FileWriter temporaryImageFileWriter = new FileWriter(temporaryImageFile);
	    
	    temporaryImageFileWriter.write
	    	("This is a test.  This file is not a valid JPG file.");
	    
	    return temporaryImageFile;
	}
	
	private File createTemporaryValidJPGFile() throws Exception {
		BufferedImage imageForTemporaryFileToBeRead =
			ImageUtilities.createBufferedImageFilledWithColor(Color.RED);
		
	    // Attempt to create the temporary directory if necessary and our temporary
	    // file inside of it.
	    File temporaryValidJPGFileToBeRead =
	    	FileUtilities.writeBufferedImageIntoTemporaryDirectory
	    		(imageForTemporaryFileToBeRead, "jpg");
	    
	    return temporaryValidJPGFileToBeRead;
	}
}
