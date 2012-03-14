package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;

import math.geom2d.Point2D;

import com.google.common.base.Function;

public final class SimpleLabelPainter implements Paintable {
	public static enum XAlignment implements Function<TextLayout, Double> {
		LEFT {
			@Override
			public Double apply(TextLayout tl) {
				return 0.0;
			}
		}, RIGHT {
			@Override
			public Double apply(TextLayout tl) {
				return - tl.getBounds().getWidth();
			}
		}, CENTER {
			@Override
			public Double apply(TextLayout tl) {
				return - (tl.getBounds().getWidth() / 2);
			}
		};

		@Override
		public abstract Double apply(TextLayout tl);
	}
	
	/**
	 * These are functions that will tell you, given a LineMetrics object, what value you
	 * should *add* to your Y-value, in order to align the text to a point at the given height.
	 */
	public static enum YAlignment implements Function<LineMetrics, Double> {
		BASELINE {
			@Override
			public Double apply(LineMetrics lm) {
				return 0.0;
			}
		}, STRIKE_HEIGHT {
			@Override
			public Double apply(LineMetrics lm) {
				/*
				 * Returns a positive number.  The strikethrough offset is negative because
				 * it's an offset rather than a distance, so we negate it again.
				 */
				return (double) - lm.getStrikethroughOffset();
			}
		}, ASCENT {
			@Override
			public Double apply(LineMetrics lm) {
				/*
				 * Returns a positive number.  "Ascent" is a *distance* rather than an offset,
				 * so we don't have to negate it.
				 */
				return (double) lm.getAscent();
			}
		}, DESCENT {
			@Override
			public Double apply(LineMetrics lm) {
				/*
				 * The descent is a distance, and we want to move the text up on the page
				 * by that amount, so we negate it.
				 */
				return (double) - lm.getDescent();
			}
		};
		@Override
		public abstract Double apply(LineMetrics lm);
	}
		
	private final Point2D position;
	private final String text;
	private final Font font;
	private final XAlignment xAlign;
	private final YAlignment yAlign;
	private final Color color;
	private final Truncator trunc;

	public SimpleLabelPainter(Point2D position, XAlignment xAlign, YAlignment yAlign,
			String text, Font font, Color color, Truncator trunc) {
		this.position = position;
		this.xAlign = xAlign;
		this.yAlign = yAlign;
		this.text = text;
		this.font = font;
		this.color = color;
		this.trunc = trunc;
	}

	@Override
	public void paint(Graphics2D g) {
		Font theFont;
		if (font == null) {
			theFont = g.getFont();
		} else {
			theFont = this.font;
		}
		g.setFont(theFont);
		
		String truncatedLabel = trunc.truncate(text, g);

		if (color != null) {
			g.setColor(color);
		}
		
		TextLayout tl = new TextLayout(truncatedLabel, theFont, g.getFontRenderContext());
		double xPos = position.getX() + xAlign.apply(tl);
		
		LineMetrics lm = theFont.getLineMetrics("Asdfj", g.getFontRenderContext());
		double yPos = position.getY() + yAlign.apply(lm);
		
		g.drawString(truncatedLabel, (float) xPos, (float) yPos);
	}
	
}