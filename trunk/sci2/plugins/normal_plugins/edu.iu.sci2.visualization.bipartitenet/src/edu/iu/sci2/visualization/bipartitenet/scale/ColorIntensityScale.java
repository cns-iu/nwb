package edu.iu.sci2.visualization.bipartitenet.scale;

import java.awt.Color;

import com.google.common.collect.ImmutableList;

public class ColorIntensityScale implements Scale<Double,Color> {
	private static final double MAXIMUM_INTENSITY = 1;
	private static final double MINIMUM_INTENSITY = 0.1;
	private static final float DEFAULT_HUE = 0; 
	private static final float DEFAULT_SATURATION = 0;

	private final BasicZeroAnchoredScale intensityScale; 
	private final float hue;
	private final float saturation; 

	private ColorIntensityScale(float hue, float saturation) {
		this.intensityScale = new BasicZeroAnchoredScale(MINIMUM_INTENSITY, MAXIMUM_INTENSITY);
		this.hue = hue;
		this.saturation = saturation;
	}
	
	public static ColorIntensityScale createWithHS(float hue, float saturation) {
		return new ColorIntensityScale(hue, saturation);
	}
	
	public static ColorIntensityScale createWithDefaultColor() {
		return new ColorIntensityScale(DEFAULT_HUE, DEFAULT_SATURATION);
	}
	
	@Override
	public Color apply(Double value) {
		// To get the Brightness(B) value, we take (1 - intensity).
		// This is because brightness==1 draws white, 0 draws black.  dark marks seem more intense.
		return Color.getHSBColor(hue, saturation, 1 - intensityScale.apply(value).floatValue());
	}
	
	public void train(Iterable<Double> trainingData) {
		intensityScale.train(trainingData);
	}
	
	public void doneTraining() {
		intensityScale.doneTraining();
	}
	
	public ImmutableList<Double> getExtrema() {
		return intensityScale.getExtrema();
	}
}
