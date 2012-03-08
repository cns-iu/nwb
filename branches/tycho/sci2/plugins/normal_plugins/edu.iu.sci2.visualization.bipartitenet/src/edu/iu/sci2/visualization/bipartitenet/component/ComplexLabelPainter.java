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

	public static class Builder {
		private final List<String> lines = Lists.newArrayList();
		private final List<Font> fonts = Lists.newArrayList();
		private final List<Color> colors = Lists.newArrayList();
		private final Point2D position;
		private final Font defaultFont;
		private final Color defaultColor;
		
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
		
		public Builder addLine(String line, Font font, Color color) {
			Preconditions.checkNotNull(line);
			Preconditions.checkNotNull(font);
			Preconditions.checkNotNull(color);
			lines.add(line);
			fonts.add(font);
			colors.add(color);
			return this;
		}
		
		public ComplexLabelPainter build() {
			return new ComplexLabelPainter(position, lines, 
					fonts, 
					colors);
		}
	}

	private ComplexLabelPainter(Point2D position, List<String> lines,
			List<Font> fonts, List<Color> colors) {
		this.position = position;
		this.lines = lines;
		this.fonts = fonts;
		this.colors = colors;
	}


	@Override
	public void paint(Graphics2D g) {
		Point2D currentPosition = position;
		
		for (int i = 0; i < lines.size(); i++) {
			Font thisFont = fonts.get(i);
			
			SimpleLabelPainter p = new SimpleLabelPainter(currentPosition, 
					XAlignment.LEFT, YAlignment.ASCENT,
					lines.get(i), thisFont, colors.get(i));
			p.paint((Graphics2D) g.create());
			
			currentPosition = currentPosition.translate(0, thisFont.getSize2D());
		}
	}

}
