package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import math.geom2d.Point2D;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;

public class ComplexLabelPainter implements Paintable {
	
	private final Point2D position;
	private final List<String> lines;
	private final List<Font> fonts;
	private final List<Color> colors;
	private final double lineSpacing;

	public static class Builder {
		// Set in constructor
		private final Point2D position;
		private final Font defaultFont;
		private final Color defaultColor;
		
		// Mutated by calls
		private final List<String> lines = Lists.newArrayList();
		private final List<Font> fonts = Lists.newArrayList();
		private final List<Color> colors = Lists.newArrayList();
		private double lineSpacing = 1;
		
		public Builder(Point2D topLeft, Font defaultFont, Color defaultColor) {
			Preconditions.checkNotNull(topLeft);
			Preconditions.checkNotNull(defaultFont);
			Preconditions.checkNotNull(defaultColor);
			this.position = topLeft;
			this.defaultFont = defaultFont;
			this.defaultColor = defaultColor;
		}
		
		public Builder addLine(String line) {
			Preconditions.checkNotNull(line);
			lines.add(line);
			fonts.add(defaultFont);
			colors.add(defaultColor);
			return this;
		}
		
		public Builder addLine(String line, Font font) {
			Preconditions.checkNotNull(line);
			Preconditions.checkNotNull(font);
			lines.add(line);
			fonts.add(font);
			colors.add(defaultColor);
			return this;
		}
		
		public Builder addLine(String line, Color color) {
			Preconditions.checkNotNull(line);
			Preconditions.checkNotNull(color);
			lines.add(line);
			fonts.add(defaultFont);
			colors.add(color);
			return this;
		}
		
		public Builder addLine(String line, Font font, Color color) {
			Preconditions.checkNotNull(line);
			Preconditions.checkNotNull(font);
			Preconditions.checkNotNull(color);
			lines.add(line);
			fonts.add(font);
			colors.add(color);
			return this;
		}
		
		public Builder withLineSpacing(double lineSpacing) {
			this.lineSpacing = lineSpacing;
			return this;
		}
		
		public ComplexLabelPainter build() {
			return new ComplexLabelPainter(position, lineSpacing, lines, 
					fonts, 
					colors);
		}
	}

	private ComplexLabelPainter(Point2D position, double lineSpacing, List<String> lines,
			List<Font> fonts, List<Color> colors) {
		this.position = position;
		this.lineSpacing = lineSpacing;
		this.lines = lines;
		this.fonts = fonts;
		this.colors = colors;
	}

	public float estimateHeight() {
		float sum = 0;
		for (Font f : fonts) {
			sum += f.getSize2D() * lineSpacing;
		}
		return sum;
	}

	@Override
	public void paint(Graphics2D g) {
		Point2D currentPosition = position;
		
		for (int i = 0; i < lines.size(); i++) {
			Font thisFont = fonts.get(i);
			
			SimpleLabelPainter p = new SimpleLabelPainter(currentPosition, 
					XAlignment.LEFT, YAlignment.ASCENT,
					lines.get(i), thisFont, colors.get(i), Truncator.none());
			Graphics2D newGraphics = (Graphics2D) g.create();
			p.paint(newGraphics);
			newGraphics.dispose();
			
			currentPosition = currentPosition.translate(0, thisFont.getSize2D() * lineSpacing);
		}
	}

}
