package edu.iu.sci2.converter.psraster.postscript.postscriptrenderer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.freehep.postscript.PSDevice;

// FreeHEP interprets and renders PostScript inside its class Processor, which
// accepts a PostScript device (PSDevice) upon construction.
// The PostScript device is what the PostScript actually gets rendered to.
// FreeHEP contains a class called VirtualDevice that is meant for rendering
// PostScript to an image (BufferedImage), but it has a bug in its only constructor,
// so we extended PSDevice with VirtualDeviceFix in almost exactly the same way as
// VirtualDevice (by copying VirtualDevice's code), minus the bug.
public class VirtualDeviceFix extends PSDevice {
	    private Graphics2D imageGraphics = null;
	    private Graphics2D graphics;
	    private Dimension dimension;
	    private AffineTransform device = new AffineTransform();

	    public VirtualDeviceFix(Graphics2D graphics, Dimension dimension) {
	        this.graphics = graphics;
	        this.dimension = dimension;
	        fireComponentResizedEvent(new ComponentEvent(new Container(), ComponentEvent.COMPONENT_RESIZED));
	    }

	    public double getWidth() {
	        return dimension.width;
	    }
	    
	    public double getHeight() {
	        return dimension.height;
	    }

	    public BufferedImage convertToImage(int width, int height) {
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        imageGraphics = (Graphics2D)image.getGraphics();
	        return image;
	    }

	    public Graphics2D getGraphics() {
	        if (imageGraphics != null) return imageGraphics;
	        return super.getGraphics();
	    }

	    public AffineTransform getDeviceTransform() {
	        return device;
	    }

	    public Graphics getDeviceGraphics() {
	        return graphics;
	    }

	    public void refresh() {
	        // ignored
	    }
	}