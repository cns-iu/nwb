package edu.iu.sci2.visualization.imageviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Dictionary;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

//

public class ImageViewerAlgorithm implements Algorithm {
	// Constants.
	private static final String EXAMPLE_POST_SCRIPT_RESOURCE_FILE_NAME = "example.ps";
	
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    LogService logger;
    
    public ImageViewerAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	BufferedImage imageToBeDisplayed = (BufferedImage)data[0].getData();
    	
    	logger.log(LogService.LOG_INFO,
    			   "Your image may be scaled to fit the screen if it does not " +
    			   "already.  If you wish to view it in full resolution, you may " +
    			   "save it out from the Data Manager.");
    	
    	// Create the frame (window) used to display the image.
    	JFrame imageDisplayFrame = createImageDisplayFrame(imageToBeDisplayed);
    	
    	// Display the frame to the user
    	imageDisplayFrame.setVisible(true);
    	
    	return null;
    }
    
    // Create the window to display the image.
    private JFrame createImageDisplayFrame(BufferedImage bufferedImage) {
    	Dimension windowSize = determineDisplayFrameSize(bufferedImage);
    	
    	// Create and setup the panel that we'll blit the image to.
    	JPanel imagePanel = new ImageDisplayPanel(bufferedImage);
    	
    	imagePanel.setLayout(new GridLayout(1, 1));
    	
    	// Create and setup the frame.
    	JFrame displayFrame = new JFrame("Image Viewer");
    	
    	displayFrame.getContentPane().add(imagePanel);
    	displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	displayFrame.setSize(windowSize);
    	
    	return displayFrame;
    }
    
    private Dimension determineDisplayFrameSize(BufferedImage bufferedImage) {
    	final int bufferedImageWidth = bufferedImage.getWidth();
    	final int bufferedImageHeight = bufferedImage.getHeight();
    	final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	final int screenWidth = screenSize.width;
    	final int screenHeight = screenSize.height;
    	
    	final double xScale = ((double)screenWidth / (double)bufferedImageWidth);
    	final double yScale = ((double)screenHeight / (double)bufferedImageHeight);
    	final double scaleUsed = Math.min(xScale, yScale);
    	
    	final int determinedWidth = (int)(scaleUsed * bufferedImageWidth);
    	final int determinedHeight = (int)(scaleUsed * bufferedImageHeight);
    	
    	return new Dimension(determinedWidth, determinedHeight);
    }
    
    // We needed to extend JPanel to handle the proper (re)drawing/blitting of an
    // image to it.
    private class ImageDisplayPanel extends JPanel {
    	private BufferedImage displayImage;
    	
    	public ImageDisplayPanel(BufferedImage displayImage) {
    		this.displayImage = displayImage;
    	}
    	
    	public void paintComponent(Graphics graphics) {
    		// In case super does anything important.
    		super.paintComponent(graphics);
    		
    		// Classically, graphics was not necessarily a Graphics2D, but the
    		// current APIs have it so it is.  We just need to cast it.
    		Graphics2D graphics2D = (Graphics2D)graphics;
    		
    		// Draw our buffered image on to the passed-in graphics.
    		// graphics2D.drawImage(this.displayImage, 0, 0, null);
    		graphics2D.drawImage
    			(this.displayImage, 0, 0, getWidth(), getHeight(), this); 
    	}
    }
}