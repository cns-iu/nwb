package edu.iu.sci2.visualization.scimaps.tempvis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Stack;

// TODO: Think about/implement the VisualElementDisplayer stuff.
public class GraphicsState {
	public static final float POINTS_PER_INCH = 72.0f;

	public Graphics2D current;
	
	private Stack<Graphics2D> graphicsHistory = new Stack<Graphics2D>();
	
	public GraphicsState(Graphics2D graphics) {
		current = graphics;
		graphicsHistory = new Stack<Graphics2D>();
	}

	public void save() {
		graphicsHistory.push(current);
		// TODO Carry over stroke?  And restore?
		current = (Graphics2D) current.create();
	}

	public void restore() {
		if (!graphicsHistory.isEmpty()) {
			current.dispose();
			current = graphicsHistory.pop();
		}
	}

	public int stringWidth(String s) {
		return current.getFontMetrics().stringWidth(s);
	}
	
	public void scaleFont(double scale) {
		setFontSize(current.getFont().getSize() * scale);
	}
	
	public void setFontSize(double fontSize) {
		current.setFont(current.getFont().deriveFont((float) fontSize));
	}
	
	public void setFont(String fontName) {
		current.setFont(Font.getFont(fontName));
	}

	public void setFont(String fontName, int fontSize) {
		current.setFont(new Font(fontName, Font.PLAIN, fontSize));
	}

	public void setGray(double gray) {
		current.setColor(new Color((float) gray, (float) gray, (float) gray));
	}
	
	/* In the style of PostScript's arc: (centerX, centerY) is the center of the circle
	 * implied by the arc and we accept a radius rather than a bounding width and height.
	 */
	public void drawArc(int centerX, int centerY, int radius, int startAngle, int arcAngle) {
		int upperLeftX = centerX - radius;
		int upperLeftY = centerY - radius;
		int width = 2 * radius;
		int height = 2 * radius;
		
		current.drawArc(upperLeftX, upperLeftY, width, height, startAngle, arcAngle);
	}
	
	public static float inch(float points) {
		return POINTS_PER_INCH * points;
	}
}