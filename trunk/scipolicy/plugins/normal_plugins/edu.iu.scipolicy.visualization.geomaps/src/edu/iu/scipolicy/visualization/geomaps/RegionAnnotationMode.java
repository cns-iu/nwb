package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleMapInterpolator;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleMapScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.PrefuseDoubleReader;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class RegionAnnotationMode implements AnnotationMode {
	public static final String FEATURE_NAME_ID = "featureName";
	public static final String FEATURE_COLOR_QUANTITY_ID = "featureColorQuantity";
	public static final String FEATURE_COLOR_SCALING_ID = "featureColorScaling";
	public static final String FEATURE_COLOR_RANGE_ID = "featureColorRange";
	public static final String DEFAULT_FEATURE_NAME_ATTRIBUTE_KEY = "NAME";
	
	public static final Map<String, Range<Color>> COLOR_RANGES;
	static {
		Map<String, Range<Color>> t = new HashMap<String, Range<Color>>();
		t.put("Cyan to burgundy", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}

	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(Table inTable, Dictionary parameters, ShapefileToPostScript shapefileToPostScript) throws AlgorithmExecutionException {
		String featureNameAttribute = (String) parameters.get(FEATURE_NAME_ID);
		String featureColorQuantityAttribute = (String) parameters.get(FEATURE_COLOR_QUANTITY_ID);
		Map<String, Double> featureColorQuantityMap = getFeatureColorQuantityMap(inTable, featureNameAttribute, featureColorQuantityAttribute);
		String featureColorScaling = (String) parameters.get(FEATURE_COLOR_SCALING_ID);
		DoubleScaler featureColorQuantityScaler = ScalerFactory.createScaler(featureColorScaling);
		String featureColorRangeKey = (String) parameters.get(FEATURE_COLOR_RANGE_ID);
		Range<Color> featureColorRange = COLOR_RANGES.get(featureColorRangeKey);
		
		if ( featureColorQuantityMap.isEmpty() ) {
			// TODO Throw exception?
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, "No data found for region color annotations.");
		}
		else {
			Map<String, Color> interpolatedFeatureColorMap = new HashMap<String, Color>();
			
			// Scale and interpolate feature colors
			DoubleMapScaler<String> featureColorQuantityMapScaler = new DoubleMapScaler<String>(featureColorQuantityScaler);
			Map<String, Double> scaledFeatureColorQuantityMap = featureColorQuantityMapScaler.scale(featureColorQuantityMap);
			Range<Double> featureColorQuantityScalableRange = featureColorQuantityMapScaler.getScalableRange();
			DoubleInterpolator<Color> featureColorQuantityInterpolator = new ColorInterpolator(scaledFeatureColorQuantityMap.values(), featureColorRange);
			interpolatedFeatureColorMap = (new DoubleMapInterpolator<String, Color>(featureColorQuantityInterpolator)).getInterpolatedMap(scaledFeatureColorQuantityMap );
			
			// Add feature color legend
			double featureColorGradientLowerLeftX = .05 * Legend.DEFAULT_WIDTH_IN_POINTS;
			double featureColorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
			double featureColorGradientWidth = Legend.DEFAULT_WIDTH_IN_POINTS/3 * .90;
			double featureColorGradientHeight = 15;
			String featureColorTypeLabel = "Feature Color";

			LabeledGradient featureColorGradient = new LabeledGradient(featureColorQuantityScalableRange, featureColorRange, featureColorTypeLabel, featureColorQuantityAttribute, featureColorGradientLowerLeftX, featureColorGradientLowerLeftY, featureColorGradientWidth, featureColorGradientHeight);
			shapefileToPostScript.setFeatureColorAnnotations(interpolatedFeatureColorMap, featureColorGradient);
		}
	}

	private Map<String, Double> getFeatureColorQuantityMap(Table inTable, String featureNameAttribute, String featureColorQuantityAttribute) throws AlgorithmExecutionException {
		Map<String, Double> featureColorQuantityMap = new HashMap<String, Double>();
		
		int duplicateFeatureNameKeys = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());

			boolean featureNameSpecified = row.canGetString(featureNameAttribute);
			boolean featureColorQuantitySpecified = PrefuseDoubleReader.isSpecified(row, featureColorQuantityAttribute);
			if ( featureNameSpecified && featureColorQuantitySpecified ) {					
				String featureName = row.getString(featureNameAttribute);
				double featureColorQuantity = PrefuseDoubleReader.get(row, featureColorQuantityAttribute);//(Double) row.get(featureColorQuantityAttribute);
				
				if ( featureColorQuantityMap.containsKey(featureName) ) {
					duplicateFeatureNameKeys++;
				}
				else {					
					featureColorQuantityMap.put(featureName, featureColorQuantity);
				}
			}
		}
		if ( duplicateFeatureNameKeys > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, duplicateFeatureNameKeys + " duplicate feature name keys detected.  Only the first was read.");
		}
		
		return featureColorQuantityMap;
	}
}
