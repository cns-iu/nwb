package edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy;

import java.awt.Color;

/* toPostScript produces the PostScript code necessary to visualize the current
 * path with this.color.  For example, subclasses can produce PostScript code
 * to stroke or fill the path with this.color.
 */
public abstract class ColorStrategy {
	public static final String INDENT = "    ";
	
	protected Color color;
	
	public abstract String toPostScript();

	public Color getColor() {
		return color;
	}
}
