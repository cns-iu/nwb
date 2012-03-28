package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.Color;
import java.awt.color.ColorSpace;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;

public class ColorTuples {
	public static final ColorSpace COLOR_SPACE = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	public static final float DEFAULT_ALPHA = 1.0f;
	public static final Function<Float, Double> DOUBLE_VALUE_OF =
			new Function<Float, Double>() {
				@Override public Double apply(Float f) { return f.doubleValue(); }};
	public static final Function<Double, Float> FLOAT_VALUE_OF =
			new Function<Double, Float>() {
				@Override public Float apply(Double d) { return d.floatValue(); }};
	
	private ColorTuples() {}
	
	
	public static double[] asTuple(final Color color) {
		float[] colorComponents = color.getColorComponents(COLOR_SPACE, null);
		
		return Doubles.toArray(Lists.transform(Floats.asList(colorComponents), DOUBLE_VALUE_OF));
	}
	public static final Function<Color, double[]> AS_TUPLE =
			new Function<Color, double[]> () {
				@Override public double[] apply(Color c) { return asTuple(c); }};
	
	public static Color asColor(double[] tuple) {	
		return new Color(COLOR_SPACE, Floats.toArray(Lists.transform(Doubles.asList(tuple), FLOAT_VALUE_OF)), DEFAULT_ALPHA);
	}
	public static final Function<double[], Color> AS_COLOR =
			new Function<double[], Color> () {
				@Override public Color apply(double[] t) { return asColor(t); }};
}