package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Truncator {
	
	public abstract String truncate(String string, Graphics2D g);
	
	public static Truncator none() {
		return new Truncator() {
			@Override
			public String truncate(String string, Graphics2D g) {
				return string;
			}
		};
	}
	
	public static Truncator atWidth(final double width) {
		return new Truncator() {
			@Override
			public String truncate(String string, Graphics2D g) {
				return shortenIfNeeded(string, g, width);
			}
		};
	}
	
	/*
	 * Thanks, David Coe.
	 * We should make a library version of this, it's very useful.
	 */
	private static String shortenIfNeeded(String string, Graphics2D g2d,
			double limit) {
		Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(string,
				g2d);
		if (stringBounds.getWidth() <= limit) {
			return string;
		}

		String truncatationIndicator = "...";
		Rectangle2D truncationIndicatorBounds = g2d.getFontMetrics()
				.getStringBounds(truncatationIndicator, g2d);
		int charWidth = g2d.getFontMetrics().getMaxAdvance();

		if (truncationIndicatorBounds.getWidth() + charWidth >= limit) {
			// This will not render well, but there was no way to handle
			// this gracefully.
			return truncatationIndicator;
		}

		int numberToRemove = (int) ((stringBounds.getWidth() - limit) / charWidth);

		if (numberToRemove >= string.length()) {
			// This will not render well, but there was no way to handle
			// this gracefully.
			return truncatationIndicator;
		}

		String truncatedString = string.substring(0, string.length()
				- numberToRemove)
				+ truncatationIndicator;

		while (g2d.getFontMetrics().getStringBounds(truncatedString, g2d)
				.getWidth() > limit
				&& numberToRemove < string.length()) {
			numberToRemove++;
			truncatedString = string.substring(0, string.length()
					- numberToRemove)
					+ truncatationIndicator;
		}

		return truncatedString;
	}
}
