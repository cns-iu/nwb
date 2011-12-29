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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


/**
 * The SwingHTView class implements a view of the HyperTree for use in
 * a Swing based application.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 *         Roman Kennke [roman@ontographics.com]
 * @version 1.0
 */
public class SwingHTView

    extends JPanel implements HTView {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7527027016171424904L;
	//private HTModel    model  = null; // the tree model represented
    private HTDraw     draw   = null; // the drawing model
    private HTAction   action = null; // action manager
    //private boolean    fastMode = false;
    //private boolean    longNameMode = false;
    //private boolean    circleMode = false;
    //private boolean    transNotCorrected = false;
    //private boolean    quadMode = true;


    private Image image = null;

  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to view
     */
    public SwingHTView(HTModel model) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(250, 250));

        setBackground(Color.white);

        //this.model = model; 
        draw = new HTDraw(model, this);
        action = new HTAction(draw);
        startMouseListening();

        //ToolTipManager.sharedInstance().registerComponent(this);

        return;
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    }


  /* --- Thread-safe locking --- */
  
    /**
     * Stops the listening of mouse events.
     */
    public void stopMouseListening() {
        this.removeMouseListener(action);
        this.removeMouseMotionListener(action);
    }
    
    /**
     * Starts the listening of mouse events.
     */
    public void startMouseListening() {
        this.addMouseListener(action);
        this.addMouseMotionListener(action);
    }


    public void translateToOrigin(HTNode node) {
	HTDrawNode drawNode = draw.findDrawNode(node);
        draw.translateToOrigin(drawNode);
        return;
    }

    public void setImage(Image image) {
        this.image = image;
        return;
    }


}

