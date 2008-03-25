
package edu.iu.nwb.visualization.hyperbolictree;

import java.awt.Color;
import java.awt.Image;

/** An abstract implementation of {@link HTNode}.
 * 
 * @author Roman Kennke [roman@ontographics.com]
 */
public abstract class AbstractHTNode implements HTNode {

    
    /**
     * Returns the color of the node.
     * Used in the drawing of the node label.
     *
     * @return    the color of the node
     */
    public Color getColor() {
        return Color.white;
    }

    /** Returns an image (icon) which should be displayed inside
     * the node. <code>null</code> is interpreted as no image.
     * @return an image (icon).
     */ 
    public Image getImage() {
    	return null;
    }

    /** Returns the preferred size of the node. Reasonable
     * values are from 0 to 10.
     * @return the size of the node.
     */
    public int getSize() {
        return 0;
    }

    /** Returns the preferred width of the border of the node.
     * Reasonable values are from 1 to 4.
     * @return the border width.
     */
    public int getBorderSize() {
        return 1;
    }
}
