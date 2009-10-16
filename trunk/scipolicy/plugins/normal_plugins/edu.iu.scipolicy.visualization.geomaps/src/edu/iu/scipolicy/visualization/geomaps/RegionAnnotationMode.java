package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.Interpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.InterpolatorInversionException;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ZeroLengthInterpolatorInputRangeException;
import edu.iu.scipolicy.visualization.geomaps.legend.ColorLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.legend.LegendComponent;
import edu.iu.scipolicy.visualization.geomaps.legend.NullLegendComponent;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.FillColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.Averager;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class RegionAnnotationMode extends AnnotationMode {
	public static final double COLOR_GRADIENT_LOWER_LEFT_X =
		Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS
		+ (0.5 * Legend.DEFAULT_WIDTH_IN_POINTS);
	public static final double COLOR_GRADIENT_WIDTH =
		Constants.PAGE_MARGIN_SIZE_IN_POINTS
		+ Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS
		- COLOR_GRADIENT_LOWER_LEFT_X;
	public static final int COLOR_GRADIENT_HEIGHT = 10;
	public static final String FEATURE_NAME_ID = "featureName";
	public static final String FEATURE_COLOR_QUANTITY_ID = "featureColorQuantity";
	public static final String FEATURE_COLOR_SCALING_ID = "featureColorScaling";
	public static final String FEATURE_COLOR_RANGE_ID = "featureColorRange";
	public static final String DEFAULT_FEATURE_NAME_ATTRIBUTE_KEY = "NAME";
	
	public static final String SUBTITLE = "Colored-Region Annotation Style";
	public static final Color DEFAULT_FEATURE_COLOR = null;
	
	/* 1: Grab the relevant parameters
     * 2: Read the color data from inTable
	 * 3: Scale the color data
	 * 4: Interpolate the color data
     * 5: Make region color LegendComponent
	 * 6: Apply this annotation (the List<Circle> and the LegendComponents) to postScriptWriter
	 */
	@Override
	public void applyAnnotations(
			ShapefileToPostScriptWriter shapefileToPostScript,
			Table inTable,
			Dictionary<String, Object> parameters)
				throws AlgorithmExecutionException {
		String nameAttribute = (String) parameters.get(FEATURE_NAME_ID);
		String colorValueAttribute = (String) parameters.get(FEATURE_COLOR_QUANTITY_ID);
		String colorValueScaling = (String) parameters.get(FEATURE_COLOR_SCALING_ID);
		Scaler colorValueScaler = ScalerFactory.createScaler(colorValueScaling);
		String colorRangeKey = (String) parameters.get(FEATURE_COLOR_RANGE_ID);
		Range<Color> colorRange = Constants.COLOR_RANGES.get(colorRangeKey);
		
		
		Range<Double> colorValueScalableRange =
			AnnotationMode.calculateScalableRangeOverColumn(
					inTable,
					colorValueAttribute,
					colorValueScaler);
		Interpolator<Color> colorInterpolator;
		try {
			colorInterpolator =
				new ColorInterpolator(
						colorValueScaler.scale(colorValueScalableRange),
						colorRange);
		} catch (ZeroLengthInterpolatorInputRangeException e) {
			throw new AlgorithmExecutionException(
					"Cannot interpolate feature colors due to: " + e.getMessage(), e);
		}
		
		
		int duplicateFeatureNames = 0;
		int unreadableTableValues = 0;
		Map<String, ColorStrategy> featureColors =
			new HashMap<String, ColorStrategy>();
		for (TableIterator tableIt = inTable.iterator(); tableIt.hasNext();) {
			Tuple row = inTable.getTuple(tableIt.nextInt());
			
			if (row.canGetString(nameAttribute)) {
				String rawFeatureName = row.getString(nameAttribute);
				
				if (rawFeatureName == null) {
					GeoMapsAlgorithm.logger.log(
							LogService.LOG_WARNING,
							"Skipping row with no region name value.");
					continue;
				}
				
				String normalFeatureName = rawFeatureName.toLowerCase();

				if (featureColors.containsKey(normalFeatureName)) {
					GeoMapsAlgorithm.logger.log(
							LogService.LOG_WARNING,
							"Duplicate region name: \"" + normalFeatureName + "\"");
					duplicateFeatureNames++;
				} else {
					try {
						double featureColorValue =
							NumberUtilities.interpretObjectAsDouble(
									row.get(colorValueAttribute));
						
						if (colorValueScaler.canScale(featureColorValue)) {				
							Color featureColor =
								colorInterpolator.interpolate(
									colorValueScaler.scale(featureColorValue));
							
							featureColors.put(
									normalFeatureName,
									new FillColorStrategy(featureColor));
						}
					} catch (NumberFormatException e) {
						unreadableTableValues++;
					} catch (NullPointerException e) {
						unreadableTableValues++;
					}
				}
			}
		}
		
		if (duplicateFeatureNames > 0) {
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					duplicateFeatureNames
					+ " duplicate feature name keys detected.  "
					+ "Only the first was read in each case.");
		}
		
		if (unreadableTableValues > 0) {
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					unreadableTableValues
					+ " rows in the table had values that could not be read "
					+ "as numbers.  Those rows were ignored.");
		}

		LegendComponent legend =
			createLegend(
				colorValueAttribute,
				colorValueScaling,
				colorValueScaler,
				colorValueScalableRange,
				colorInterpolator,
				colorRange);
		
		shapefileToPostScript.setFeatureColorAnnotations(SUBTITLE, featureColors, legend);
	}
	
	private LegendComponent createLegend(
			String attribute,
			String scaling,
			Scaler scaler,
			Range<Double> scalableRange,
			Interpolator<Color> colorInterpolator,
			Range<Color> colorRange)
				throws AlgorithmExecutionException {
		LegendComponent featureColorGradient = new NullLegendComponent();
		try {
			Color colorMidrange =
				Averager.mean(
						colorRange.getMin(),
						colorRange.getMax());
			double colorMidrangePreInterpolation =
				colorInterpolator.invert(colorMidrange);
			double rawMidColorQuantity =
				scaler.invert(colorMidrangePreInterpolation);
			
			featureColorGradient =
				new ColorLegend(
						scalableRange,
						scaling,
						rawMidColorQuantity,
						colorRange,
						"Region Color",
						attribute,
						COLOR_GRADIENT_LOWER_LEFT_X,
						Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS,
						COLOR_GRADIENT_WIDTH,
						COLOR_GRADIENT_HEIGHT);
		} catch (InterpolatorInversionException e) {
			throw new AlgorithmExecutionException(
					"Couldn't create region color legend: "
					+ e.getMessage(),
					e);
		}
		
		return featureColorGradient;
	}
}
