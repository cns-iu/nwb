package edu.iu.scipolicy.visualization.geomaps.legend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class Legend {
	public static final double DEFAULT_LOWER_LEFT_X_IN_POINTS = 0;
	public static final double DEFAULT_LOWER_LEFT_Y_IN_POINTS = 72 * 2.2;
	public static final double DEFAULT_WIDTH_IN_POINTS = 72 * 8.5;

	private double lowerLeftX, lowerLeftY, width;
	private List<LegendComponent> components;
	private BufferedReader postScriptDefinitionsReader;

	public Legend() {
		this(DEFAULT_LOWER_LEFT_X_IN_POINTS, DEFAULT_LOWER_LEFT_Y_IN_POINTS, DEFAULT_WIDTH_IN_POINTS);
	}

	public Legend(double lowerLeftX, double lowerLeftY,	double width) {
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		this.width = width;

		components = new ArrayList<LegendComponent>();
		
		final ClassLoader loader = getClass().getClassLoader();
		this.postScriptDefinitionsReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("/edu/iu/scipolicy/visualization/geomaps/printing/legendPostScriptDefinitions.ps")));
	}

	public String toPostScript() throws AlgorithmExecutionException {
		String s = "";

		String line;
		try {
			while((line = postScriptDefinitionsReader.readLine()) != null) {
				s += line + "\n";
			}
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}
		s += "\n";

		for ( LegendComponent legendComponent : components ) {
			s += legendComponent.toPostScript();
			s += "\n";
		}

		return s;
	}

	public double getLowerLeftX() {
		return lowerLeftX;
	}

	public double getLowerLeftY() {
		return lowerLeftY;
	}

	public double getWidth() {
		return width;
	}

	public void add(LegendComponent legendComponent) {
		components.add(legendComponent);
	}
	
	public static String prettyPrintDouble(double value) {
		if ( value == Math.floor(value) ) {
			return String.valueOf((int) value);
		}
		else {
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			return decimalFormat.format(value);
		}
	}
}
