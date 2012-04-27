package edu.iu.cns.visualization.utility.linewrap;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

/**
 * Basic implementations of and utilities for working with {@link LineConstraint}.
 * 
 * @see LineConstraint
 */
public final class LineConstraints {
	private LineConstraints() {}
	
	/**
	 * An immutable adapter to the LineConstraint interface for String predicates.
	 */
	public static LineConstraint forPredicate(final Predicate<String> fitsOnOneLine) {
		return new PredicateLineConstraint(fitsOnOneLine);
	}
	
	private static final class PredicateLineConstraint implements LineConstraint {
		private final Predicate<String> fitsOnOneLine;
	
		private PredicateLineConstraint(Predicate<String> fitsOnOneLine) {
			this.fitsOnOneLine = fitsOnOneLine;
		}
	
		@Override
		public boolean fitsOnOneLine(String text) {
			return fitsOnOneLine.apply(text);
		}
	}


	/**
	 * A constraint that the {@link String#length()} of a line should not be more than
	 * {@code targetedLength}.
	 */
	public static LineConstraint length(int targetedLength) {
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

		@Override
		public int hashCode() {
			return Objects.hashCode(targetedLength);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null) {
				return false;
			}
			if (!(o instanceof LengthLineConstraint)) {
				return false;
			}
			LengthLineConstraint that = (LengthLineConstraint) o;

			return Objects.equal(this.targetedLength, that.targetedLength);
		}
	}

	/**
	 * A constraint that the {@link FontMetrics#stringWidth(String)} of the line should not be more
	 * than {@code targetedWidth} when using the provided {@code font} and {@code graphics} context.
	 */
	public static LineConstraint width(double targetedWidth, Font font, Graphics graphics) {
		return new WidthLineConstraint(targetedWidth, font, graphics);
	}
	
	/**
	 * @deprecated Prefer {@link #width(double, Font, Graphics)} with an explicit Graphics
	 *             when possible. This method creates a phony Graphics context and may not behave as
	 *             expected.
	 */
	@Deprecated()
	public static LineConstraint width(double targetedWidth, Font font) {
		/* TODO Test whether the phony context works well enough and consider
		 * updating the deprecation.
		 */
		return width(targetedWidth, font, makePhonyGraphicsContext());
	}	
	private static Graphics makePhonyGraphicsContext() {
		return new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB).createGraphics();
	}
	
	private static final class WidthLineConstraint implements LineConstraint {
		private final double targetedWidth;
		private final Graphics context;
		
		private WidthLineConstraint(double maximumWidth, Font font, Graphics context) {
			this.targetedWidth = maximumWidth;
			this.context = context;
			this.context.setFont(font);
		}
	
		@Override
		public boolean fitsOnOneLine(String s) {
			return context.getFontMetrics().getStringBounds(s, context).getWidth() <= targetedWidth;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("targetedWidth", targetedWidth)
					.add("context", context) // TODO Include context?
					.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(targetedWidth, context); // TODO Include context?
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null) {
				return false;
			}
			if (!(o instanceof WidthLineConstraint)) {
				return false;
			}
			WidthLineConstraint that = (WidthLineConstraint) o;

			return Objects.equal(this.targetedWidth, that.targetedWidth)
					&& Objects.equal(this.context, that.context); // TODO Include context?
		}
	}
}
