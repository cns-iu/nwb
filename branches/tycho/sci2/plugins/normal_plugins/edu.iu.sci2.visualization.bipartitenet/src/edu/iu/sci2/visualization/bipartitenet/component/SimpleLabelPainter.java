package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;

import math.geom2d.Point2D;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public class SimpleLabelPainter {
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

	private final XAlignment xAlign;
	private final YAlignment yAlign;
	private final Font font;
	private final Color color;
	private final Truncator trunc;

	private SimpleLabelPainter(XAlignment xAlign, YAlignment yAlign, Font font, Color color, Truncator trunc) {
		Preconditions.checkNotNull(xAlign);
		Preconditions.checkNotNull(yAlign);
		Preconditions.checkNotNull(trunc);
		
		this.xAlign = xAlign;
		this.yAlign = yAlign;
		this.font = font;
		this.color = color;
		this.trunc = trunc;
	}

	public Paintable getPaintable(final Point2D position, final String text) {
		return new Paintable() {
			@Override
			public void paint(Graphics2D g) {
				paintLabel(position, text, g);
			}
		};
	}
	
	public void paintLabel(Point2D position, String text, Graphics2D g) {
		setFontIfNeeded(g);
		setColorIfNeeded(g);
		
		Font theFont = g.getFont();
		
		String truncated = trunc.truncate(text, g);
		
		TextLayout tl = new TextLayout(truncated, theFont, g.getFontRenderContext());
		double xPos = position.getX() + xAlign.apply(tl);
		
		LineMetrics lm = theFont.getLineMetrics("Asdfj", g.getFontRenderContext());
		double yPos = position.getY() + yAlign.apply(lm);
		
		g.drawString(truncated, (float) xPos, (float) yPos);
	}

	private void setColorIfNeeded(Graphics2D g) {
		if (this.color != null) {
			g.setColor(this.color);
		}		
	}

	private void setFontIfNeeded(Graphics2D g) {
		if (this.font != null) {
			g.setFont(this.font);
		}
	}

	public static Builder alignedBy(XAlignment xAlign, YAlignment yAlign) {
		return new Builder(xAlign, yAlign);
	}
	
	public static class Builder {
		private final XAlignment xAlign;
		private final YAlignment yAlign;
		private Font font;
		private Color color;
		private Truncator trunc = Truncator.none();
		
		public Builder(XAlignment xAlign, YAlignment yAlign) {
			this.xAlign = xAlign;
			this.yAlign = yAlign;
		}
		
		public Builder withFont(Font f) {
			this.font = f;
			return this;
		}
		
		public Builder withColor(Color c) {
			this.color = c;
			return this;
		}
		
		public Builder truncatedTo(double width) {
			trunc = Truncator.atWidth(width);
			return this;
		}
		
		public SimpleLabelPainter build() {
			return new SimpleLabelPainter(xAlign, yAlign, font, color, trunc);
		}
		
		public Paintable makeLabel(Point2D position, String text) {
			return build().getPaintable(position, text);
		}
	}
	
}