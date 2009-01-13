package edu.iu.scipolicy.visualization.imageviewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Dictionary;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.scipolicy.converter.psraster.psrasterproperties.PSRasterProperties;

//

public class ImageViewer implements Algorithm {
	// Constants.
	private static final String EXAMPLE_POST_SCRIPT_RESOURCE_FILE_NAME = "example.ps";
	
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public ImageViewer(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	BufferedImage imageToBeDisplayed = (BufferedImage)data[0].getData();
    	// Create the frame (window) used to display the image.
    	JFrame imageDisplayFrame = createImageDisplayFrame(imageToBeDisplayed);
    	
    	// Display the frame to the user
    	imageDisplayFrame.setVisible(true);
    	
    	return null;
    }
    
    // Create the window to display the image.
    private JFrame createImageDisplayFrame(BufferedImage bufferedImage) {
    	// Create and setup the panel that we'll blit the image to.
    	JPanel imagePanel = new ImageDisplayPanel(bufferedImage);
    	
    	imagePanel.setLayout(new GridLayout(1, 1));
    	
    	// Create and setup the frame.
    	JFrame displayFrame = new JFrame("Image Viewer");
    	
    	displayFrame.getContentPane().add(imagePanel);
    	displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	displayFrame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
    	
    	return displayFrame;
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
    		graphics2D.drawImage(this.displayImage, 0, 0, null);
    	}
    }
}