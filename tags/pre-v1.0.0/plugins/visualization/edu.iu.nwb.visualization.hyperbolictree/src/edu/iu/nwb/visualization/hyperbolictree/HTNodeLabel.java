/*
 * HTNodeLabel.java
 *
 * www.bouthier.net
 * 2001
 */

package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.*;


/**
 * The HTNodeLabel class implements the drawed label 
 * representing a node.
 */
public class HTNodeLabel {

    private HTDrawNode node   = null;  // represented node
    private int        x      = 0;     // x up-left corner
    private int        y      = 0;     // y up-left corner
    private int        width  = 0;     // width of the label
    private int        height = 0;     // height of the label
    private boolean    active = false; // should be drawed ?


  /* ---  Constructor --- */
  
    /**
     * Constructor.
     * 
     * @param node    the represented node
     */
    HTNodeLabel(HTDrawNode node) {
        this.node = node;
    }
    
    
  /* --- Draw --- */
  
    /**
     * Draw this label, if there is enought space.
     *
     * @param g    the graphic context
     */
    void draw(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int fh = fm.getHeight();
        int space = node.getSpace();
        if (space >= fh) {

            active = true;
            HTCoordS zs = node.getScreenCoordinates();          
            String name = node.getName();
            Color color = node.getColor();
            Image icon = node.getImage();
            char[] nameC = name.toCharArray();            
            int nameLength = nameC.length;
            int nameWidth = fm.charsWidth(nameC, 0, nameLength);

            while((nameWidth >= space) && (nameLength > 0)) {
                nameLength--;               
                nameWidth = fm.charsWidth(nameC, 0, nameLength);
            }
            
            height = fh + 2*node.getSize();
            width = nameWidth + 10 + 2 * node.getSize();
            x = zs.x - (width / 2) - node.getSize();
            y = zs.y - (fh / 2) - node.getSize();  
                      
            g.setColor(color);
            g.fillRect(x, y, width, height);
            // draw image
            if (icon != null) {
            	g.drawImage(icon, x, y, null);
            }
            g.setColor(Color.black);

	    if (g instanceof Graphics2D) {
		// only in a Java2D capable environment
		((Graphics2D) g).setStroke
		    (new BasicStroke((long) node.getBorderSize()));
	    }

            g.drawRect(x, y, width, height);

	    if (g instanceof Graphics2D) {
		// only in a Java2D capable environment
		((Graphics2D) g).setStroke
		    (new BasicStroke((long) 1.0));
	    }

            int sx = zs.x - (nameWidth / 2) - node.getSize();            
            int sy = y + fm.getAscent() + (fm.getLeading() / 2) +
                node.getSize();;       
            
            g.drawString(new String(nameC, 0, nameLength), sx, sy);           
        } else {
            active = false;
        }
    }


  /* --- Zone containing --- */

    /**
     * Is the given HTCoordS within this label ?
     *
     * @return    <CODE>true</CODE> if it is,
     *            <CODE>false</CODE> otherwise
     */
    boolean contains(HTCoordS zs) {
        if (active) {
            if ((zs.x >= x) && (zs.x <= (x + width)) &&
                (zs.y >= y) && (zs.y <= (y + height)) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return node.getScreenCoordinates().contains(zs);
        }
    }
    
        
  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = "label of " + node.getName() + 
                        "\n\tx = " + x + " : y = " + y +
                        "\n\tw = " + width + " : h = " + height; 
        return result;
    }

}
