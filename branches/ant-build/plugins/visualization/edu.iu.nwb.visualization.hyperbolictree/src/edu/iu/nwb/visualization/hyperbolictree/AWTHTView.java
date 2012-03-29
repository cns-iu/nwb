/*
 * HTView.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The AWTHTView class implements a view of the HyperTree for use in an
 * AWT based application.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 *         Roman Kennke [roman@ontographics.com]
 * @version 1.0
 */
public class AWTHTView

    extends Panel implements HTView {


    /**
	 * 
	 */
	private static final long serialVersionUID = -8913546160205283088L;
	//private HTModel    model  = null; // the tree model represented
    private HTDraw     draw   = null; // the drawing model
    private HTAction   action = null; // action manager
    //private boolean    fastMode = false;
    //private boolean    longNameMode = false;
    //private boolean    circleMode = false;
    //private boolean    transNotCorrected = false;
    //private boolean    quadMode = true;


    private Image image = null;

    // double buffer magic
    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;

    // tooltip magic
    private TooltipManager tooltipManager;
    private String tooltipText;
    private int mouseX;
    private int mouseY;
    private long lastMoved;

    /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to view
     */
    public AWTHTView(HTModel model) {
        super(new BorderLayout());
        setSize(new Dimension(250, 250));
        setBackground(Color.white);

        //this.model = model; 
        draw = new HTDraw(model, this);
        action = new HTAction(draw);
        startMouseListening();

	tooltipManager = new TooltipManager();
	mouseX = 0;
	mouseY = 0;
	tooltipText = null;
	lastMoved = 0L;
    }


  /* --- Node finding --- */

    /**
     * Returns the node containing the mouse event.
     * <P>
     * This will be a HTNode.
     *
     * @param event    the mouse event on a node
     * @return         the node containing this event;
     *                 could be <CODE>null</CODE> if no node was found
     */
    public HTNode getNodeUnderTheMouse(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        HTDrawNode node = draw.findNode(new HTCoordS(x, y));
        if (node != null) {
            return node.getHTModelNode().getNode();
        } else {
            return null;
        }
    }
    
  /* --- Tooltip --- */

    /**
     * Returns the tooltip to be displayed.
     *
     * @param event    the event triggering the tooltip
     * @return         the String to be displayed
     */
    public String getToolTipText(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        HTDrawNode node = draw.findNode(new HTCoordS(x, y));
        if (node != null) {
            return node.getName();
        } else {
            return null;
        }
    }

  /* --- Paint --- */

    /**
     * Paint the component.
     *
     * @param g    the graphic context
     */
    //PDA
    //public void paintComponent(Graphics g) {
    //    super.paintComponent(g);
    public void paint(Graphics g) {
       super.paint(g);

	//    checks the buffersize with the current panelsize
        //    or initialises the image with the first paint
        if(bufferWidth!=getSize().width || 
	   bufferHeight!=getSize().height || 
	   bufferImage==null || bufferGraphics==null)
            resetBuffer();

	if(bufferGraphics!=null){
            //this clears the offscreen image, not the onscreen one
            bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);
	    
            //calls the paintbuffer method with 
            //the offscreen graphics as a param
            paintBuffer(bufferGraphics);
	    
            //we finaly paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage,0,0,this);
        }

    }

    public void update(Graphics g) {
	paint(g);
    }


    //PDA

    private void paintBuffer(Graphics g) {

	if (image != null) {
	    g.drawImage(image, 0, 0, getWidth(), this.getHeight(), this);
	}

        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint
                (RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON);
        }
        draw.refreshScreenCoordinates();
        draw.drawBranches(g);
        draw.drawNodes(g);

	// draw tooltip
	drawTooltip(g);
    }

    /** Draws the tooltip under the mouse.
     */
    private void drawTooltip(Graphics g) {

	if ((tooltipText != null)
	    && ((System.currentTimeMillis() - lastMoved) > 500)) {

	    char[] chars = tooltipText.toCharArray();
	    FontMetrics fontMetrics = g.getFontMetrics();
	    int width = fontMetrics.charsWidth(chars, 0, chars.length);
	    int height = fontMetrics.getHeight();

	    Color fillColor = new Color(239, 239, 182);
	    g.setColor(fillColor);
	    g.fillRect(mouseX, mouseY - height, width + 4, height + 2);

	    g.setColor(Color.black);
	    g.drawRect(mouseX, mouseY - height, width + 4, height + 2);

	    g.setColor(Color.black);
	    g.drawString(tooltipText, mouseX + 2, mouseY - 2);
	}
	return;
    }


    private void resetBuffer(){
        // always keep track of the image size
        bufferWidth=getSize().width;
        bufferHeight=getSize().height;
	
        //    clean up the previous image
        if(bufferGraphics!=null){
            bufferGraphics.dispose();
            bufferGraphics=null;
        }
        if(bufferImage!=null){
            bufferImage.flush();
            bufferImage=null;
        }
        System.gc();
	
        //    create the new image with the size of the panel
        bufferImage=createImage(bufferWidth,bufferHeight);
        bufferGraphics=bufferImage.getGraphics();
    }


  /* --- Thread-safe locking --- */
  
    /**
     * Stops the listening of mouse events.
     */
    public void stopMouseListening() {
        this.removeMouseListener(action);
        this.removeMouseMotionListener(action);
	this.removeMouseMotionListener(tooltipManager);
    }
    
    /**
     * Starts the listening of mouse events.
     */
    public void startMouseListening() {
        this.addMouseListener(action);
        this.addMouseMotionListener(action);

	this.addMouseMotionListener(tooltipManager);
	return;
    }


    public void translateToOrigin(HTNode node) {
        draw.translateToOrigin(draw.findDrawNode(node));
        return;
    }

    public void setImage(Image image) {
        this.image = image;
        return;
    }

    class TooltipManager implements MouseMotionListener {

	private Timer timer;
	private TimerTask task;

	public TooltipManager() {
	    timer = new Timer(true);
	    task = null;
	    return;
	}


	public void mouseDragged(MouseEvent e) {
	    mouseX = e.getX();
	    mouseY = e.getY();
	    tooltipText = getToolTipText(e);
	    lastMoved = System.currentTimeMillis();
	    return;
	}

	public void mouseMoved(MouseEvent e) {
	    mouseX = e.getX();
	    mouseY = e.getY();
	    tooltipText = getToolTipText(e);
	    lastMoved = System.currentTimeMillis();
	    repaint();
	    if (task != null) {
		task.cancel();
	    }
	    task = new TooltipTimerTask();
	    timer.schedule(task, 600);
	    return;
	}
    }

    class TooltipTimerTask extends TimerTask {

	public void run() {
	    repaint();
	    return;
	}
    }
}

