package edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy;

import java.awt.Color;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScriptWriter;

public class FillColorStrategy extends ColorStrategy {
	public FillColorStrategy(Color color) {
		this.color = color;
	}
	
	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("gsave" + "\n");	
		builder.append(INDENT + ShapefileToPostScriptWriter.makeSetRGBColorCommand(color));
		builder.append(INDENT + "fill" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}