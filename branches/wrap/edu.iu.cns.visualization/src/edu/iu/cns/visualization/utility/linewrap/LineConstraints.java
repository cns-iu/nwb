package edu.iu.cns.visualization.utility.linewrap;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.google.common.base.Objects;

/**
 * Basic implementations of and utilities for working with {@link LineConstraint}.
 * 
 * @see LineConstraint
 */
public final class LineConstraints {
	private LineConstraints() {}
	
	/**
	 * A constraint that the {@link String#length()} of a line should not be more than
	 * {@code targetedLength}.
	 */
	public static LineConstraint byLength(int targetedLength) {
		return new LengthLineConstraint(targetedLength);
	}
	
	private static final class LengthLineConstraint implements LineConstraint {
		private final int targetedLength;
		
		private LengthLineConstraint(int maximumLength) {
			this.targetedLength = maximumLength;
		}
		
		@Override
		public boolean fitsOnOneLine(String s) {
			return s.length() <= targetedLength;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("targetedLength", this.targetedLength)
					.toString();
		}
	}

	/**
	 * A constraint that the {@link FontMetrics#stringWidth(String)} of the line should not be more
	 * than {@code targetedWidth} when using the provided {@code font} and {@code graphics} context.
	 */
	public static LineConstraint byWidth(double targetedWidth, Font font, Graphics graphics) {
		return new WidthLineConstraint(targetedWidth, font, graphics);
	}
	
	/**
	 * A constraint that the {@link FontMetrics#stringWidth(String)} of the line should not be more
	 * than {@code targetedWidth} when using the provided {@code font} within a fictional Graphics
	 * context.
	 * 
	 * Prefer {@link #byWidth(double, Font, Graphics)} with an explicit Graphics when possible. This
	 * method creates a phony Graphics context and may not behave as expected.
	 */
	public static LineConstraint byWidth(double targetedWidth, Font font) {
		return byWidth(targetedWidth, font, PHONY_GRAPHICS);
	}
	/**
	 * A fictional Graphics context for byWidth callers who do not have one.
	 * This seems to work for current purposes but has not been extensively tested.
	 */
	private static final Graphics PHONY_GRAPHICS =
			new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB).createGraphics();
	private static final class WidthLineConstraint implements LineConstraint {
		private final double targetedWidth;
		private final FontMetrics fontMetrics;
		
		private WidthLineConstraint(double maximumWidth, Font font, Graphics context) {
			this.targetedWidth = maximumWidth;
			this.fontMetrics = context.getFontMetrics(font);
		}
	
		@Override
		public boolean fitsOnOneLine(String s) {
			return fontMetrics.stringWidth(s) <= targetedWidth;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("targetedWidth", targetedWidth)
					.add("fontMetrics", fontMetrics)
					.toString();
		}
	}
}
