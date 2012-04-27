package edu.iu.cns.visualization.utility.linewrap;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.google.common.base.Objects;

/**
 * Common {@link LineConstraint}s.
 */
public final class LineConstraints {
	private LineConstraints() {}
	
	/**
	 * TODO Change text to fit after move
	 * A greedy line wrapper that tries to make the total {@link String#length()} of each line no
	 * longer than {@code length}.
	 */
	public static LineConstraint length(int targetedLength) {
		return new LengthLineConstraint(targetedLength);
	}
	
	private static final class LengthLineConstraint implements LineConstraint {
		private final int targetedLength;
		
		LengthLineConstraint(int maximumLength) {
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

	
	public static LineConstraint width(double targetedWidth, Font font, Graphics graphics) {
		return new WidthLineConstraint(targetedWidth, font, graphics);
	}
	
	/**
	 * @deprecated	Prefer {@link #width(double, Font, Graphics)} with an explicit Graphics when
	 * 				possible.  This method creates a phony Graphics and may not behave as expected.
	 */
	@Deprecated()
	public static LineConstraint width(double targetedWidth, Font font) {
		// TODO Test whether the phony graphics work well enough and consider updating the deprecation.
		return width(targetedWidth, font, makePhonyGraphics());
	}	
	private static Graphics makePhonyGraphics() {
		return new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB).createGraphics();
	}
	
	private static final class WidthLineConstraint implements LineConstraint {
		private final double targetedWidth;
		private final Graphics graphics;
		
		WidthLineConstraint(double maximumWidth, Font font, Graphics graphics) { // TODO Make font, graphics optional?
			this.targetedWidth = maximumWidth;
			this.graphics = graphics;
			this.graphics.setFont(font);
		}
	
		@Override
		public boolean fitsOnOneLine(String s) {
			return graphics.getFontMetrics().getStringBounds(s, graphics).getWidth() <= targetedWidth;
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("targetedWidth", targetedWidth)
					.add("graphics", graphics) // TODO Include graphics?
					.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(targetedWidth, graphics); // TODO Include graphics?
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
					&& Objects.equal(this.graphics, that.graphics); // TODO Include graphics?
		}
	}
}
