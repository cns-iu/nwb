package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Graphics2D;

/**
 * Interface for objects which can be painted on a Graphics2D.
 *
 */
public interface Paintable {
	/**
	 * Draws this object on the given {@link Graphics2D}.
	 * 
	 * @param g
	 *            a new Graphics2D object - you don't need to worry about
	 *            cleaning up the state of the Graphics object, this one will be
	 *            discarded after you're done with it.
	 */
	public void paint(Graphics2D g);
}
