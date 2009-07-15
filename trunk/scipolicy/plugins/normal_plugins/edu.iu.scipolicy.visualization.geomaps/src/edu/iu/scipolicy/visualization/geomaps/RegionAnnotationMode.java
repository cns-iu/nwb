package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.Interpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ListInterpolator;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.scaling.ListScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
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
	private Map<String, Integer> featureIDs;
	private List<Double> featureColorQuantities;
	static {
		Map<String, Range<Color>> t = new HashMap<String, Range<Color>>();
		t.put("Blue to red", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));
		t.put("Yellow to red", new Range<Color>(new Color(254, 204, 92), new Color(177, 4, 39)));
		t.put("Yellow to blue", new Range<Color>(new Color(255, 255, 158), new Color(37, 52, 148)));
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}

	/* 1: Grab the relevant parameters
     * 2: Read the color data from inTable
	 * 3: Scale the color data
	 * 4: Interpolate the color data
     * 5: Make region color LegendComponent
	 * 6: Apply this annotation (the List<Circle> and the LegendComponents) to postScriptWriter
	 */
	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(ShapefileToPostScriptWriter shapefileToPostScript, Table inTable, Dictionary parameters) throws AlgorithmExecutionException {
		String featureNameAttribute = (String) parameters.get(FEATURE_NAME_ID);
		String featureColorQuantityAttribute = (String) parameters.get(FEATURE_COLOR_QUANTITY_ID);
		String featureColorScaling = (String) parameters.get(FEATURE_COLOR_SCALING_ID);
		Scaler featureColorQuantityScaler = ScalerFactory.createScaler(featureColorScaling);
		String featureColorRangeKey = (String) parameters.get(FEATURE_COLOR_RANGE_ID);
		Range<Color> featureColorRange = COLOR_RANGES.get(featureColorRangeKey);
		
		getFeatureData(inTable, featureNameAttribute, featureColorQuantityAttribute, featureColorQuantityScaler);
		
		if ( featureColorQuantities.isEmpty() ) {
			throw new AlgorithmExecutionException("No data found for region color annotations.");
		}
		else {
			// Scale and interpolate feature colors
			ListScaler featureColorQuantityListScaler = new ListScaler(featureColorQuantityScaler);
			List<Double> scaledFeatureColorQuantities = featureColorQuantityListScaler.scale(featureColorQuantities);
			Range<Double> featureColorQuantityScalableRange = featureColorQuantityListScaler.getScalableRange();
			Interpolator<Color> featureColorQuantityInterpolator = new ColorInterpolator(scaledFeatureColorQuantities, featureColorRange);
			List<Color> interpolatedFeatureColors = (new ListInterpolator<Color>(featureColorQuantityInterpolator)).getInterpolatedList(scaledFeatureColorQuantities );
			
			Map<String, Color> interpolatedFeatureColorMap = new HashMap<String, Color>();
			for ( Map.Entry<String, Integer> featureID : featureIDs.entrySet() ) {
				String featureName = featureID.getKey();
				int id = featureID.getValue();
				Color color = interpolatedFeatureColors.get(id);
				
				interpolatedFeatureColorMap.put(featureName, color);				
			}
			
			
			// Add feature color legend
			double featureColorGradientLowerLeftX = .05 * Legend.DEFAULT_WIDTH_IN_POINTS;
			double featureColorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
			double featureColorGradientWidth = (Legend.DEFAULT_WIDTH_IN_POINTS / 3) * 0.90;
			double featureColorGradientHeight = 15;
			String featureColorTypeLabel = "Region Color";

			
			LabeledGradient featureColorGradient = new LabeledGradient(featureColorQuantityScalableRange, featureColorRange, featureColorTypeLabel, featureColorQuantityAttribute, featureColorGradientLowerLeftX, featureColorGradientLowerLeftY, featureColorGradientWidth, featureColorGradientHeight);
			shapefileToPostScript.setFeatureColorAnnotations(interpolatedFeatureColorMap, featureColorGradient);
		}
	}

	private void getFeatureData(Table inTable, String featureNameAttribute, String featureColorQuantityAttribute, Scaler featureColorQuantityScaler) throws AlgorithmExecutionException {
		featureIDs = new HashMap<String, Integer>();
		featureColorQuantities = new ArrayList<Double>();
				
		int duplicateFeatureNameKeys = 0;
		int id = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());

			boolean featureNameSpecified = row.canGetString(featureNameAttribute);
			boolean featureColorQuantitySpecified = PrefuseDoubleReader.isSpecified(row, featureColorQuantityAttribute);
			if ( featureNameSpecified && featureColorQuantitySpecified ) {					
				String featureName = row.getString(featureNameAttribute);
				double featureColorQuantity = PrefuseDoubleReader.get(row, featureColorQuantityAttribute);//(Double) row.get(featureColorQuantityAttribute);
				
				if ( featureIDs.containsKey(featureName) ) {
					duplicateFeatureNameKeys++;
				}
				else {					
					featureColorQuantities.add(featureColorQuantity);
					featureIDs.put(featureName, id);
					id++;
				}
			}
		}
		if ( duplicateFeatureNameKeys > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, duplicateFeatureNameKeys + " duplicate feature name keys detected.  Only the first was read.");
		}
	}
}
