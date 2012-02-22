package edu.iu.sci2.visualization.geomaps.viz.strategy;

import java.awt.Color;

import edu.iu.sci2.visualization.geomaps.viz.ps.PSUtility;

public class FillColorStrategy extends ColorStrategy {
	private FillColorStrategy(Color color) {
		this.color = color;
	}
	public static ColorStrategy forColor(Color color) {
		return (color == null) ? (new NullColorStrategy()) : (new FillColorStrategy(color));
	}
	
	
	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("gsave" + "\n");
		builder.append(INDENT + PSUtility.makeSetRGBColorCommand(color));
		builder.append(INDENT + "fill" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}