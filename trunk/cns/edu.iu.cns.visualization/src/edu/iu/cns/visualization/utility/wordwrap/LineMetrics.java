package edu.iu.cns.visualization.utility.wordwrap;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.google.common.base.Objects;

/**
 * Common implementations of {@link LineMetric}.
 */
public final class LineMetrics {
	private LineMetrics() {}
	
	/**
	 * The {@link String#length()} of a line.
	 */
	public static LineMetric length() {
		return new LengthLineMetric();
	}
	
	private static final class LengthLineMetric implements LineMetric {
		@Override
		public int sizeOf(String line) {
			return line.length();
		}	
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).toString();
		}
	}
	
	/**
	 * The {@link FontMetrics#stringWidth(String)} of a line in the provided {@code font} and
	 * {@code graphics} context.
	 */
	public static LineMetric widthInContext(Font font, Graphics graphics) {
		return new WidthLineMetric(font, graphics);
	}
	
	/**
	 * The {@link FontMetrics#stringWidth(String)} of a line in the provided {@code font} within a
	 * fictional Graphics context.
	 * 
	 * <p/>
	 * Prefer {@link #widthInContext(Font, Graphics)} with an explicit Graphics when possible.
	 */
	public static LineMetric widthInFont(Font font) {
		return widthInContext(font, FictionalGraphicsContext.getInstance());
	}
	
	/**
	 * A fictional Graphics context for callers who do not have one. This seems
	 * to work for current purposes but has not been extensively tested.
	 */
	protected static class FictionalGraphicsContext {
		// from http://en.wikipedia.org/wiki/Singleton_pattern

		private static Graphics _instance = null;
		
		private FictionalGraphicsContext() {
			// Singleton, do not instantiate.
		}

		/**
		 * Get a fictional Graphics context for callers who do not have one.
		 * 
		 * @return A fictional {@link Graphics} context.
		 */
		public static synchronized Graphics getInstance() {
			if (_instance == null) {
				_instance = new BufferedImage(5, 5,
						BufferedImage.TYPE_INT_RGB).createGraphics();
			}
			
			return _instance;
		}

	}
			
	private static final class WidthLineMetric implements LineMetric {
		private final FontMetrics fontMetrics;
		
		private WidthLineMetric(Font font, Graphics context) {
			this.fontMetrics = context.getFontMetrics(font);
		}
	
		@Override
		public int sizeOf(String line) {
			return fontMetrics.stringWidth(line);
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("fontMetrics", fontMetrics).toString();
		}
	}
}