package edu.iu.sci2.visualization.geomaps.utility;
//package edu.iu.sci2.visualization.geomaps.data.utility;
//
//import java.awt.Color;
//
//import com.google.common.base.Function;
//import com.google.common.base.Preconditions;
//import com.google.common.collect.Lists;
//import com.google.common.primitives.Doubles;
//import com.google.common.primitives.Floats;
//
///**
// * Bridge methods between a {@link Color} and its integer RGB coordinates as a double[3].
// * <p/>
// * Warning: Behavior depends on {@link Enum#ordinal()}. */ 
//public enum ColorDimension {
//	RED(
//		new Function<Color, Integer>() { public Integer apply(Color c) { return asTuple; }}),
//	GREEN(
//		new Function<Color, Integer>() { public Integer apply(Color c) { return c.getGreen(); }}),
//	BLUE(
//		new Function<Color, Integer>() { public Integer apply(Color c) { return c.getBlue(); }});
//	
//	private final Function<Color, Integer> getter;
//	private ColorDimension(Function<Color, Integer> getter) {
//		this.getter = getter;
//	}
//
//	public static double[] asTuple(final Color color) {
//		float[] colorComponents = new float[ColorDimension.values().length];
//		color.getColorComponents(colorComponents);
//		
//		return Doubles.toArray(Lists.transform(Floats.asList(colorComponents),
//				new Function<Float, Double>() {
//					public Double apply(Float f) {
//						return new Double(f);
//					}					
//				}));
//	}
//	
//	public static Color asColor(double[] tuple) {
//		Preconditions.checkArgument(
//				tuple.length == ColorDimension.values().length,
//				"Argument dimensionality must match that of ColorIndex.");
//		
//		return new Color(
//				(float) tuple[RED.ordinal()],
//				(float) tuple[GREEN.ordinal()],
//				(float) tuple[BLUE.ordinal()]);
//	}
//}