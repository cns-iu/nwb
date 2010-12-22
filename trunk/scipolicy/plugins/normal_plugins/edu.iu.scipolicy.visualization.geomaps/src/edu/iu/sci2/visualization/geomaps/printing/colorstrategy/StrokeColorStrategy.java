package edu.iu.sci2.visualization.geomaps.printing.colorstrategy;

import java.awt.Color;

import edu.iu.sci2.visualization.geomaps.ShapefileToPostScriptWriter;

public class StrokeColorStrategy extends ColorStrategy {
	public static final Color DEFAULT_COLOR = Color.BLACK;
	
	
	public StrokeColorStrategy() {
		this(DEFAULT_COLOR);
	}
	
	public StrokeColorStrategy(Color color) {
		this.color = color;
	}
	

	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("gsave" + "\n");	
		builder.append(INDENT + ShapefileToPostScriptWriter.makeSetRGBColorCommand(color));
		builder.append(INDENT + "stroke" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
