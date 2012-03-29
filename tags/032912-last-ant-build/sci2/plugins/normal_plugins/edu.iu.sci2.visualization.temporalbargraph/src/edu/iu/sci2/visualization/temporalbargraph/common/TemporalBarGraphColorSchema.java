package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;

import org.cishell.utilities.color.ColorSchema;

/**
 * This is the default colors for use by TemporalBarGraph.  Michael came up with
 * the colors.  He used hex code, so I found it easiest to just keep the hex.
 *
 */
public class TemporalBarGraphColorSchema extends ColorSchema {

	/**
	 * The default color should be black.
	 */
	public static final Color DEFAULT_COLOR = Color.BLACK;

	/**
	 * Some default colors as given by Michael.
	 */
	public static final Color[] COLOR_ARRAY = new Color[] {
		new Color(0x004949),
		new Color(0x009292),
		new Color(0xFF6DB6),
		new Color(0xFEB6DB),
		new Color(0x29548E),
		new Color(0xB66DFF),
		new Color(0x6FB6FF),
		new Color(0xDC6D02),
		new Color(0x620000),
		new Color(0xFFFF6D)};

	/**
	 * A default color schema to use for TemporalBarGraph.
	 */
	public static final ColorSchema DEFAULT_COLOR_SCHEMA = new ColorSchema(
			COLOR_ARRAY, DEFAULT_COLOR);

	/**
	 * Get a default color schema.
	 */
	public TemporalBarGraphColorSchema() {
		this(COLOR_ARRAY, DEFAULT_COLOR);
	}

	/**
	 * Give a default color[] with the colors you'd like to use and a
	 * default color.
	 * @param colors The colors to use
	 * @param defaultColor A default color.
	 */
	public TemporalBarGraphColorSchema(final Color[] colors,
			final Color defaultColor) {
		super(colors, defaultColor);
	}

}
