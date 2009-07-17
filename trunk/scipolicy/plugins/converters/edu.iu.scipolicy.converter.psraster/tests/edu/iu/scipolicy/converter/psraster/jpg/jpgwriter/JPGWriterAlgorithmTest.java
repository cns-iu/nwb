package edu.iu.scipolicy.converter.psraster.jpg.jpgwriter;

import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

public class JPGWriterAlgorithmTest {
	// Constants.
	private static final int TEMPORARY_IMAGE_WIDTH = 1024;
	private static final int TEMPORARY_IMAGE_HEIGHT = 768;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testExecute() {
		// Create a BufferedImage to use when creating a JPGWriter to test.
		BufferedImage bufferedImage =
			createBufferedImageFilledWithColor(Color.GREEN);
		
		// Create a JPGWriter with (image) in-data to test writing out to.
		JPGWriterAlgorithm jpgWriter = createJPGWriter(bufferedImage);
		
		// This test succeeds if an exception is NOT thrown when calling
		// jpgReader.execute().  This is to keep track of whether or not that
		// happens.
		boolean testFailed = false;
		
		// The out-data data from the JPGWriter.  (If the test succeeds, the
		// out-data should contain a valid file, so we need to delete it.)
		Data[] jpgWriterExecuteOutData = null;
		
		try {
			jpgWriterExecuteOutData = jpgWriter.execute();
		}
		catch (AlgorithmExecutionException e) {
			// We caught an exception, so the test failed.
			testFailed = true;
		}
		finally {
			// Delete the out-data file, if necessary.
			if ((jpgWriterExecuteOutData != null) &&
				(jpgWriterExecuteOutData[0] != null) &&
				(jpgWriterExecuteOutData[0].getData() instanceof File)) {
				File outDataFile = (File)jpgWriterExecuteOutData[0].getData();
				
				outDataFile.delete();
			}
		}
		
		assertFalse(testFailed);
	}
	
	// Create a JPGWriter given an image to be used as in-data.
	private JPGWriterAlgorithm createJPGWriter(BufferedImage inDataBufferedImage) {
		// Create in-data containing the file.
		Data bufferedImageData = new BasicData(inDataBufferedImage,
										PSRasterProperties.IMAGE_OBJECT_MIME_TYPE);
		
		// Create a JPGWriter with the file in-data.
		JPGWriterAlgorithm jpgWriter =
			new JPGWriterAlgorithm(new Data[] { bufferedImageData }, null, null);
		
		return jpgWriter;
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
}
