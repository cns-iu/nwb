package edu.iu.sci2.visualization.bipartitenet.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;

public enum NodeDestination {
	LEFT(Color.decode("#3399FF")) {
		/* TODO Extract bits that don't vary per node.. per side even? */
		
		@Override
		public void paintLabel(NodeView nv, Graphics2D g, double maxHeight, Font defaultFont) {
			SimpleLabelPainter painter =
					new SimpleLabelPainter(
							nv.getNodeCenter().translate(- nv.getCenterToTextDistance(), 0),
							XAlignment.RIGHT,
							YAlignment.STRIKE_HEIGHT,
							nv.getLabel(),
							fitFontWithinHeight(g.getFontRenderContext(), maxHeight, defaultFont), null);
			
			painter.paint(g);
		}


	},
	RIGHT(Color.decode("#FF9900")) {
		@Override
		public void paintLabel(NodeView nv, Graphics2D g, double maxHeight, Font defaultFont) {
			SimpleLabelPainter painter =
					new SimpleLabelPainter(
							nv.getNodeCenter().translate(nv.getCenterToTextDistance(), 0),
							XAlignment.LEFT,
							YAlignment.STRIKE_HEIGHT,
							nv.getLabel(),
							fitFontWithinHeight(g.getFontRenderContext(), maxHeight, defaultFont), null);
			
			painter.paint(g);
		}

	};

	private static final int SPACING_BETWEEN_LABELS = 2;
	private static final int MINIMUM_FONT_SIZE = 2;
	private final Color fillColor;
	
	private NodeDestination(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	public abstract void paintLabel(NodeView nv, Graphics2D g, double maxHeight, Font defaultFont);
	
	private static Font fitFontWithinHeight(FontRenderContext frc, double maxHeight, Font defaultFont) {
		Font currentFont = defaultFont;
		for (int fontSize = defaultFont.getSize(); fontSize >= MINIMUM_FONT_SIZE; fontSize--) {
			currentFont = defaultFont.deriveFont(defaultFont.getStyle(), fontSize);
			TextLayout tl = new TextLayout("Alg", currentFont, frc); // "Alg" is a good height test with its risers and descenders
			Rectangle2D textBounds = tl.getBounds();
			// "+1" to leave a bit of a margin
			if (textBounds.getHeight() + SPACING_BETWEEN_LABELS < maxHeight) {
				break;
			}
		}
		
		return currentFont;
	}
	public	Color getFillColor() {
		return fillColor;
	}
}
