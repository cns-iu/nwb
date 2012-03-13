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
		new Color(0x3399ff),
		new Color(0xff9900),
		new Color(0x99cc00),
		new Color(0x003366),
		new Color(0xcc0000),
		new Color(0xcc00cc),
		new Color(0x009999),
		new Color(0x7a7900),
		new Color(0xff00b4),
		new Color(0x520000)};

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
