/*
 * HTDraw.java
 *
 * www.bouthier.net
 * 2001
 */

package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.Graphics;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;


/**
 * The HTDraw class implements the drawing model for the HTView.
 */
public class HTDraw {

    private final static int NBR_FRAMES = 10; // number of intermediates animation frames

    //private HTModel    model    = null;  // the tree model
    private HTView     view     = null;  // the view using this drawing model
    private HTDrawNode drawRoot = null;  // the root of the drawing tree 

    private HTCoordS   sOrigin  = null;  // origin of the screen plane
    private HTCoordS   sMax     = null;  // max point in the screen plane 
      
    private double[]   ray      = null;
    
    private boolean    fastMode = false; // fast mode

    /** Maps {@link HTNode}s to {@link HTDrawNode}s. */
    private Map drawToHTNodeMap;


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to draw 
     * @param view     the view using this drawing model
     */
    HTDraw(HTModel model, HTView view) {

	// initialize mapping
	drawToHTNodeMap = new HashMap();

        this.view = view;
        //this.model = model;
        HTModelNode root = model.getRoot();
        sOrigin = new HTCoordS();
        sMax = new HTCoordS();

        ray = new double[4];
        ray[0] = model.getLength();

        for (int i = 1; i < ray.length; i++) {
            ray[i] = (ray[0] + ray[i - 1]) / (1 + (ray[0] * ray[i - 1]));
        }

        if (root.isLeaf()) {
            drawRoot = new HTDrawNode(null, root, this);
        } else {
            drawRoot = new HTDrawNodeComposite(null, (HTModelNodeComposite) root, this);
        }

	return;
    }


  /* --- Screen coordinates --- */

    /**
     * Refresh the screen coordinates of the drawing tree.
     */
    void refreshScreenCoordinates() {
        Insets insets = view.getInsets();
        sMax.x = (view.getWidth() - insets.left - insets.right) / 2;
        sMax.y = (view.getHeight() - insets.top - insets.bottom) / 2;
        sOrigin.x = sMax.x + insets.left;
        sOrigin.y = sMax.y + insets.top;
        drawRoot.refreshScreenCoordinates(sOrigin, sMax);
    }

    /**
     * Returns the origin of the screen plane.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the origin of the screen plane
     */
    HTCoordS getSOrigin() {
        return sOrigin;
    }

    /**
     * Return the point representing the up right corner
     * of the screen plane, thus giving x and y maxima.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the max point
     */
    HTCoordS getSMax() {
        return sMax;
    }


  /* --- Drawing --- */

    /**
     * Draws the branches of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {
        drawRoot.drawBranches(g);
    }

    /**
     * Draws the nodes of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        drawRoot.drawNodes(g);
    }


  /* --- Translation --- */

    /**
     * Translates the hyperbolic tree by the given vector.
     *
     * @param t    the translation vector
     */
    void translate(HTCoordE zs, HTCoordE ze) {
        HTCoordE zo = new HTCoordE(drawRoot.getOldCoordinates());
        zo.x = - zo.x;
        zo.y = - zo.y;
        HTCoordE zs2 = new HTCoordE(zs);
        zs2.translate(zo);

        HTCoordE t = new HTCoordE();
        double de = ze.d2();
        double ds = zs2.d2();
        double dd = 1.0 - de * ds;
        t.x = (ze.x * ( 1.0 - ds) - zs2.x * (1.0 - de)) / dd;
        t.y = (ze.y * ( 1.0 - ds) - zs2.y * (1.0 - de)) / dd;
        
        if (t.isValid()) {
            HTTransformation to = new HTTransformation();
            to.composition(zo, t);
        
            drawRoot.transform(to);
            view.repaint();
        }
    }

    /**
     * Signal that the translation ended.
     */
    void endTranslation() {
        drawRoot.endTranslation();
    }

    /**
     * Translate the hyperbolic tree so that the given node 
     * is put at the origin of the hyperbolic tree.
     *
     * @param node    the given HTDrawNode
     */
    void translateToOrigin(HTDrawNode node) {
        view.stopMouseListening();
        AnimThread t = new AnimThread(node);
        t.start();
    }

    /**
     * Restores the hyperbolic tree to its origin.
     */
    void restore() {
        drawRoot.restore();
        view.repaint();
    }

    /**
     * Sets the fast mode, where nodes are no more drawed.
     *
     * @param mode    setting on or off.
     */
    void fastMode(boolean mode) {
        if (mode != fastMode) {
            fastMode = mode;
            drawRoot.fastMode(mode);
            if (mode == false) {
                view.repaint();
            }
        }
    }
    

  /* --- Node searching --- */

    /**
     * Returns the node (if any) whose screen coordinates' zone
     * contains thoses given in parameters.
     *
     * @param zs    the given screen coordinate
     * @return      the searched HTDrawNode if found;
     *              <CODE>null</CODE> otherwise
     */
    HTDrawNode findNode(HTCoordS zs) {
        return drawRoot.findNode(zs);
    }


    /** Maps a {@link HTNode} to a {@link HTDrawNode}.
     * Used for backwards finding a {@link HTDrawNode} instance for a given
     * {@link HTNode}.
     * @param htNode the {@link HTNode}.
     * @param drawNode the {@link HTDrawNode} for the given {@link HTNode}.
     */
    protected void mapNode(HTNode htNode, HTDrawNode drawNode) {

	drawToHTNodeMap.put(htNode, drawNode);
	return;
    }

    /** Finds a {@link HTDrawNode} for a given {@link HTNode}.
     * @param htNode the {@link HTNode} for which we want to find the
     *     {@link HTDrawNode}.
     * @return the {@link HTDrawNode} for the given {@link HTNode}.
     */
    protected HTDrawNode findDrawNode(HTNode htNode) {

	HTDrawNode drawNode = (HTDrawNode) drawToHTNodeMap.get(htNode);
	return drawNode;
    }


  /* --- Inner animation thread --- */

    /**
     * The AnimThread class implements the thread that do the animation
     * when clicking on a node.
     */  
    class AnimThread
        extends Thread {

        private HTDrawNode node  = null; // node to put at the origin
        private Runnable   tTask = null; // translation task

        /**
         * Constructor.
         *
         * @param node    the node to put at the origin
         */
        AnimThread(HTDrawNode node) {
            this.node = node;
	    return;
        }

        /**
         * Do the animation.
         */
        public void run() {
            HTCoordE zn = node.getOldCoordinates();
            HTCoordE zf = new HTCoordE();

            int frames = NBR_FRAMES;
            //int nodes = model.getNumberOfNodes();
            
            double d = zn.d();
            for (int i = 0; i < ray.length; i++) {
            	if (d > ray[i]) {
                	frames += NBR_FRAMES / 2;
            	}
            }
                        
            double factorX = zn.x / frames;
            double factorY = zn.y / frames;
            
            for (int i = 1; i < frames; i++) {
                zf.x = zn.x - (i * factorX);
                zf.y = zn.y - (i * factorY);
                tTask = new TranslateThread(zn, zf);
                try {
                    SwingUtilities.invokeAndWait(tTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
       
            zf.x = 0.0;
            zf.y = 0.0;
            tTask = new LastTranslateThread(zn, zf);
            try {
                SwingUtilities.invokeAndWait(tTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
      /* --- Inner's inner --- */
      
        class TranslateThread
            implements Runnable {
            
            HTCoordE zStart = null; 
            HTCoordE zEnd   = null;
            
            TranslateThread(HTCoordE z1, HTCoordE z2) {
                zStart = z1;
                zEnd = z2;
            }
            
            public void run() {
                translate(zStart, zEnd);
			    view.repaint();
            }        
        }
        
        class LastTranslateThread
            implements Runnable {
            
            HTCoordE zStart = null; 
            HTCoordE zEnd   = null;
            
            LastTranslateThread(HTCoordE z1, HTCoordE z2) {
                zStart = z1;
                zEnd = z2;
            }
            
            public void run() {
                translate(zStart, zEnd);
                endTranslation();
			    view.repaint();
			    view.startMouseListening();
            }        
        }

              
    }

}

