package edu.iu.sci2.visualization.scimaps.tempvis;

import java.awt.BasicStroke;
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
		this.current = graphics;
		this.graphicsHistory = new Stack<Graphics2D>();
	}
	
	public void save() {
		this.graphicsHistory.push(this.current);
		// TODO Carry over stroke? And restore?
		this.current = (Graphics2D) this.current.create();
	}

	public void restore() {
		if (!this.graphicsHistory.isEmpty()) {
			this.current.dispose();
			this.current = this.graphicsHistory.pop();
		}
	}

	public int stringWidth(String s) {
		return this.current.getFontMetrics().stringWidth(s);
	}

	public void scaleFont(double scale) {
		setFontSize(this.current.getFont().getSize() * scale);
	}

	public void setFontSize(double fontSize) {
		this.current.setFont(this.current.getFont()
				.deriveFont((float) fontSize));
	}

	public void setFont(String fontName) {
		this.current.setFont(Font.getFont(fontName));
	}

	public void setFont(String fontName, int style, int fontSize) {
		this.current.setFont(new Font(fontName, style, fontSize));
	}

	public void setFont(String fontName, int fontSize) {
		setFont(fontName, Font.PLAIN, fontSize);
	}

	public void setBoldFont(String fontName, int fontSize) {
		setFont(fontName, Font.BOLD, fontSize);
	}

	public void setItalicFont(String fontName, int fontSize) {
		setFont(fontName, Font.ITALIC, fontSize);
	}

	public void setGray(double gray) {
		this.current.setColor(new Color((float) gray, (float) gray,
				(float) gray));
	}

	/*
	 * In the style of PostScript's arc: (centerX, centerY) is the center of the
	 * circle implied by the arc and we accept a radius rather than a bounding
	 * width and height.
	 */
	public void drawArc(int centerX, int centerY, int radius, int startAngle,
			int arcAngle) {
		drawArc(centerX, centerY, radius, startAngle, arcAngle, 1);
	}

	public void drawStringAndTranslate(String string, float x, float y) {
		this.current.drawString(string, x, y);
		this.current.translate(0, this.current.getFontMetrics().getHeight());
	}

	public void drawStringAndTranslate(String string, int x, int y) {
		this.current.drawString(string, x, y);
		this.current.translate(0, this.current.getFontMetrics().getHeight());
	}

	public void drawStringAndTranslate(String string) {
		drawStringAndTranslate(string, 0, 0);
	}

	public static float inch(float points) {
		return POINTS_PER_INCH * points;
	}

	public void drawCircle(int centerX, int centerY, int radius) {
		drawArc(centerX, centerY, radius, 0, 360);
	}

	public void drawCircle(int centerX, int centerY, int radius, int weight) {
		drawArc(centerX, centerY, radius, 0, 360, weight);
	}

	private void drawArc(int centerX, int centerY, int radius, int startAngle,
			int arcAngle, float weight) {
		int upperLeftX = centerX - radius;
		int upperLeftY = centerY - radius;
		int width = 2 * radius;
		int height = 2 * radius;
		save();
		this.current.setStroke(new BasicStroke(weight));
		this.current.drawArc(upperLeftX, upperLeftY, width, height, startAngle,
				arcAngle);
		restore();

	}

}