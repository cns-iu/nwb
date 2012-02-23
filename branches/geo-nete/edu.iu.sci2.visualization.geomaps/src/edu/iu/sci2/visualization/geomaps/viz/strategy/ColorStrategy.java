package edu.iu.sci2.visualization.geomaps.viz.strategy;

import java.awt.Color;

/* toPostScript produces the PostScript code necessary to visualize the current
 * path with this.color.  For example, subclasses can produce PostScript code
 * to stroke or fill the path with this.color.
 */
public abstract class ColorStrategy implements Strategy {
	protected Color color;

	public Color getColor() {
		return color;
	}

	@Override
	public abstract String toPostScript();
}
