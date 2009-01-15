package edu.iu.scipolicy.converter.psraster.jpg.jpgreader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.scipolicy.converter.psraster.fileutilities.FileUtilities;
import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGReaderTest extends TestCase {
	// Constants.
	private static final int TEMPORARY_IMAGE_WIDTH = 640;
	private static final int TEMPORARY_IMAGE_HEIGHT = 480;
	
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
	
	// TODO: Test for invalid input image file (not just one that doesn't exist)?

	@Test
	public void testExecuteWithNonExistentFile() {
		// Create a JPGReader with invalid (file) in-data.
		JPGReader jpgReader =
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
		JPGReader jpgReader = createJPGReader(this.temporaryInvalidJPGFileToBeRead);
		
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
		JPGReader jpgReader = createJPGReader(this.temporaryValidJPGFileToBeRead);
		
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
	private JPGReader createJPGReader(File inDataFile) {
		// Create in-data containing the file.
		Data fileData = new BasicData(inDataFile, PSRasterProperties.JPG_MIME_TYPE);
		
		// Create a JPGReader with the file in-data.
		JPGReader jpgReader = new JPGReader(new Data[] { fileData }, null, null);
		
		return jpgReader;
	}
	
	// Create a JPGReader given the file name of a file to be used as in-data.
	private JPGReader createJPGReader(String inDataFileName) {
		// Create the file to be used as in-data.
		File inDataFile = new File(inDataFileName);
		
		return createJPGReader(inDataFile);
	}
	
	private BufferedImage createBufferedImageFilledWithColor(Color fillColor) {
		// Create the image (that we will fill and output).
		BufferedImage bufferedImage = new BufferedImage(TEMPORARY_IMAGE_WIDTH,
														TEMPORARY_IMAGE_HEIGHT,
														BufferedImage.TYPE_INT_RGB);
		
		// Get the image's graphics context so we can paint to/fill the image.
		Graphics2D bufferedImageGraphics2D = bufferedImage.createGraphics();
		
		// Fill the image with the color passed in.
		bufferedImageGraphics2D.setBackground(fillColor);
		
		return bufferedImage;
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
			createBufferedImageFilledWithColor(Color.RED);
		
	    // Attempt to create the temporary directory if necessary and our temporary
	    // file inside of it.
	    File temporaryValidJPGFileToBeRead =
	    	FileUtilities.writeBufferedImageToTemporaryFileInTemporaryDirectory
	    		(imageForTemporaryFileToBeRead, "jpg");
	    
	    return temporaryValidJPGFileToBeRead;
	}
}
