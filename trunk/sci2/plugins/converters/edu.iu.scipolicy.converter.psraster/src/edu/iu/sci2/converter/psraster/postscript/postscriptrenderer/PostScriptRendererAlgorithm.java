package edu.iu.sci2.converter.psraster.postscript.postscriptrenderer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.freehep.postscript.PSDevice;
import org.freehep.postscript.PSInputFile;
import org.freehep.postscript.Processor;
import org.osgi.service.log.LogService;

import edu.iu.sci2.converter.psraster.postscript.postscriptrenderer.utility.DimensionExtractor;
import edu.iu.sci2.converter.psraster.psrasterproperties.PSRasterProperties;

public class PostScriptRendererAlgorithm implements Algorithm {
	private static Dimension DEFAULT_IMAGE_DIMENSIONS = new Dimension(800, 600);
	
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    LogService logger;
    
    public PostScriptRendererAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    // The in-data should be a PostScript file with mime type file:text/ps.
    // The out-data is that PostScript file (interpreted and) rendered to an
    // in-memory image with mime type java.awt.image.BufferedImage.
    public Data[] execute() throws AlgorithmExecutionException {
    	File postScriptFile = (File)data[0].getData();
    	String postScriptFileName = postScriptFile.getAbsolutePath();

    	// Inform the user that rendering the PostScript may take a little while.
    	logger.log(LogService.LOG_INFO,
    			   "Rendering PostScript.  May take a few moments...");
    	
    	// Determine the PostScript image's dimensions.
    	
    	Dimension imageDimensions = 
    		DimensionExtractor.determineImageDimensions(postScriptFile, logger);
    	
    	System.out.println("Using dimensions " + imageDimensions.width + ", " + imageDimensions.height);
    	
    	// Render the PostScript.
    	
    	BufferedImage bufferedImage =
    		renderPostScriptFileToBufferedImage(postScriptFileName,
    											imageDimensions.width,
    											imageDimensions.height);
    	
    	// Wrap the rendered image in a Data object.
        Data bufferedImageData =
        	new BasicData(bufferedImage,
        				  PSRasterProperties.IMAGE_OBJECT_MIME_TYPE);
        	
        return new Data[] { bufferedImageData };
    }
    
    // FreeHEP renders PostScript via its Processor class, which gets constructed
    // with a post script device (PSDevice).
    // The PostScript device is essentially a target to be rendered to that almost
    // certainly contains another object that can be rendered to using Swing/etc.,
    // i.e. a GUI window or an image.
    // In our case, we want the PostScript device to render to an image so we use
    // the result however we want.
    public BufferedImage renderPostScriptFileToBufferedImage
    		(String postScriptFileName,
    		 int width, int height) throws AlgorithmExecutionException {
		// Create the image to which we'll be rendering the PostScript.
    	BufferedImage bufferedImage =
			new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
    	// Get the graphics context from that image.
		Graphics2D bufferedImageGraphics = bufferedImage.createGraphics();
		
		try {
			// Create the device that the PostScript processor will render to.
			PSDevice postScriptDeviceToRenderTo =
				new VirtualDeviceFix(bufferedImageGraphics,
									 new Dimension(width, height));
			
			// Create the PostScript processor.
			Processor postScriptProcessor = new Processor(postScriptDeviceToRenderTo, false);
			
			// Setup the PostScript processor with the (input) PostScript data.
			postScriptProcessor.setData(new PSInputFile(postScriptFileName));
			// Interpret the PostScript.  If it doesn't fail, it will be rendered
			// (to bufferedImage) after this step.
			postScriptProcessor.process();
		}
		catch (Exception e) {
			throw new AlgorithmExecutionException
				("Failed to render PostScript file \'" + postScriptFileName +
						"\' for the following reason:\r\n" + e.getMessage(),
				 e);
		}
		
		// The image we rendered the PostScript to is also what we want out of this
		// method, so return it.
		return bufferedImage;
    }
 
}