package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;

import org.cishell.utilities.color.ColorSchema;

public class TemporalBarGraphColorSchema extends ColorSchema {

	public static final Color DEFAULT_COLOR = Color.BLACK;
	public static final Color[] COLOR_ARRAY = new Color[]{ 
													new Color(166, 206, 227),
													new Color(31, 120, 180),
													new Color(51, 160, 44),
													new Color(178, 223, 138),
													new Color(251, 154, 153),
													new Color(227, 26, 28),
													new Color(253, 191, 111),
													new Color(255, 127, 0),
													new Color(202, 178, 214),
													new Color(106, 61, 154),
													new Color(255, 255, 153)			
												};
	
	public static final ColorSchema DEFAULT_COLOR_SCHEMA = new ColorSchema(COLOR_ARRAY, DEFAULT_COLOR);
	
	public TemporalBarGraphColorSchema() {
		this(COLOR_ARRAY, DEFAULT_COLOR);
	}
	
	public TemporalBarGraphColorSchema(Color[] colors, Color defaultColor){
		super(colors, defaultColor);
	}

}
