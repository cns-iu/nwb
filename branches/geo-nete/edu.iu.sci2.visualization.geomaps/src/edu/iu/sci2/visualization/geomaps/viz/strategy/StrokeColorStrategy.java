package edu.iu.sci2.visualization.geomaps.viz.strategy;

import java.awt.Color;

import edu.iu.sci2.visualization.geomaps.viz.ps.PSUtility;

public class StrokeColorStrategy extends ColorStrategy {
	public static final Color DEFAULT_COLOR = Color.BLACK;
	
	private StrokeColorStrategy(Color color) {
		this.color = color;
	}
	public static StrokeColorStrategy forColor(Color color) {
		return (color == null) ? theDefault() : new StrokeColorStrategy(color);
	}
	public static StrokeColorStrategy theDefault() {
		return forColor(DEFAULT_COLOR);
	}
	

	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("gsave" + "\n");
		builder.append(INDENT + PSUtility.makeSetRGBColorCommand(color));
		builder.append(INDENT + "stroke" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
