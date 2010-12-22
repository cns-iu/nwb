package edu.iu.sci2.visualization.imageviewer;
//Re-enable me when PSRasterProperties isn't part of a converter plugin
//(this causes dependency problems in the build system)

//package edu.iu.sci2.visualization.imageviewer;
//
//import java.awt.Color;
//import java.awt.image.BufferedImage;
//
//import junit.framework.TestCase;
//
//import org.cishell.framework.data.BasicData;
//import org.cishell.framework.data.Data;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;
//import edu.iu.sci2.utilities.ImageUtilities;
//
//public class ImageViewerAlgorithmTest extends TestCase {
//
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void testExecute() {
//		// Create an image to view.
//		BufferedImage bufferedImageToView =
//			ImageUtilities.createBufferedImageFilledWithColor(Color.BLUE);
//		
//		// Wrap the image for the image viewer.
//		Data bufferedImageToViewData = new BasicData(bufferedImageToView,
//										PSRasterProperties.IMAGE_OBJECT_MIME_TYPE);
//		
//		// Create the image viewer.
//		ImageViewerAlgorithm imageViewer =
//			new ImageViewerAlgorithm(new Data[] { bufferedImageToViewData }, null, null);
//		
//		try {
//			if (imageViewer.execute() != null)
//				fail();
//		}
//		catch (Exception e) {
//			fail();
//		}
//	}
//
//}
