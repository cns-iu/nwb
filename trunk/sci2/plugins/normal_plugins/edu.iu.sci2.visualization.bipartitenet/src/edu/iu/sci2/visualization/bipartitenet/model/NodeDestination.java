package edu.iu.sci2.visualization.bipartitenet.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;

public enum NodeDestination {
	LEFT {
		/* TODO Extract bits that don't vary per node.. per side even? */
		
		@Override
		public void paintLabel(NodeView nv, Graphics2D g, double maxHeight) {
			TextLayout layout = fitTextLayout(nv.getLabel(), g, maxHeight);
			Rectangle2D textBounds = layout.getBounds();
			double x = nv.getNodeCenter().getX()
					- nv.getCenterToTextDistance() - textBounds.getWidth();

			layout.draw(g, (float) x, (float) (nv.getNodeCenter().getY()
					- getFontCenterHeight(g)));
		}

		@Override
		public
		Color getFillColor() {
			return Color.pink;
		}

	},
	RIGHT {
		@Override
		public void paintLabel(NodeView nv, Graphics2D g, double maxHeight) {
			TextLayout layout = fitTextLayout(nv.getLabel(), g, maxHeight);
			double x = nv.getNodeCenter().getX() + nv.getCenterToTextDistance();
			
			layout.draw(g, (float) x, (float) (nv.getNodeCenter().getY()
					- getFontCenterHeight(g)));
		}

		@Override
		public
		Color getFillColor() {
			return Color.orange;
		}
	};

	private static final int MINIMUM_FONT_SIZE = 2;
	private static final Font LABEL_FONT = PageDirector.BASIC_FONT;
	public abstract void paintLabel(NodeView nv, Graphics2D g, double maxHeight);
	public abstract Color getFillColor();
	
	
	private static TextLayout fitTextLayout(String label, Graphics2D g, double maxHeight) {
		FontRenderContext frc = g.getFontRenderContext();
		Font currentFont = fitFontWithinHeight(frc, maxHeight);
		
		return new TextLayout(label, currentFont, frc);
	}
	
	private static Font fitFontWithinHeight(FontRenderContext frc, double maxHeight) {
		Font currentFont = LABEL_FONT;
		for (int fontSize = LABEL_FONT.getSize() ; fontSize >= MINIMUM_FONT_SIZE; fontSize--) {
			currentFont = LABEL_FONT.deriveFont(LABEL_FONT.getStyle(), fontSize);
			TextLayout tl = new TextLayout("Alg", currentFont, frc); // "Alg" is a good height test with its risers and descenders
			Rectangle2D textBounds = tl.getBounds();
			// "+1" to leave a bit of a margin
			if (textBounds.getHeight() + 1 < maxHeight) {
				break;
			}
		}
		
		return currentFont;
	}
	
	private static float getFontCenterHeight(Graphics2D g) {
		 LineMetrics lm = g.getFont().getLineMetrics("Asdf", g.getFontRenderContext());
		 return lm.getStrikethroughOffset();
	}
}
