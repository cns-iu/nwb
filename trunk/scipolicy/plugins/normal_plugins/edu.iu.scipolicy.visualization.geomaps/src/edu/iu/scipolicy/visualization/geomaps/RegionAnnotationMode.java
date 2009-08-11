package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.ArrayList;
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
import edu.iu.scipolicy.visualization.geomaps.interpolation.InterpolatorInversionException;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ListInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ZeroLengthInterpolatorInputRangeException;
import edu.iu.scipolicy.visualization.geomaps.legend.ColorLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.legend.LegendComponent;
import edu.iu.scipolicy.visualization.geomaps.legend.NullLegendComponent;
import edu.iu.scipolicy.visualization.geomaps.scaling.ListScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.PrefuseDoubleReader;
import edu.iu.scipolicy.visualization.geomaps.utility.RGBAverager;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class RegionAnnotationMode implements AnnotationMode {
	public static final String FEATURE_NAME_ID = "featureName";
	public static final String FEATURE_COLOR_QUANTITY_ID = "featureColorQuantity";
	public static final String FEATURE_COLOR_SCALING_ID = "featureColorScaling";
	public static final String FEATURE_COLOR_RANGE_ID = "featureColorRange";
	public static final String DEFAULT_FEATURE_NAME_ATTRIBUTE_KEY = "NAME";
	
	public static final String SUBTITLE = "with colored region annotations";
	
	private Map<String, Integer> featureIDs;
	private List<Double> featureColorQuantities;

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
		Range<Color> featureColorRange = Constants.COLOR_RANGES.get(featureColorRangeKey);
		
		getFeatureData(inTable, featureNameAttribute, featureColorQuantityAttribute, featureColorQuantityScaler);
		
		if ( featureColorQuantities.isEmpty() ) {
			throw new AlgorithmExecutionException("No data found for region color annotations.");
		}
		else {
			// Scale and interpolate feature colors
			ListScaler featureColorQuantityListScaler = new ListScaler(featureColorQuantityScaler);
			List<Double> scaledFeatureColorQuantities = featureColorQuantityListScaler.scale(featureColorQuantities);
			Range<Double> featureColorQuantityScalableRange = featureColorQuantityListScaler.getScalableRange();
			
			Map<String, Color> interpolatedFeatureColorMap = new HashMap<String, Color>();
			LegendComponent featureColorGradient = new NullLegendComponent();
			try {
				Interpolator<Color> featureColorQuantityInterpolator = new ColorInterpolator(scaledFeatureColorQuantities, featureColorRange);
				List<Color> interpolatedFeatureColors = (new ListInterpolator<Color>(featureColorQuantityInterpolator)).getInterpolatedList(scaledFeatureColorQuantities );
				
				interpolatedFeatureColorMap = new HashMap<String, Color>();
				for ( Map.Entry<String, Integer> featureID : featureIDs.entrySet() ) {
					String featureName = featureID.getKey();
					int id = featureID.getValue();
					Color color = interpolatedFeatureColors.get(id);
					
					interpolatedFeatureColorMap.put(featureName, color);				
				}
				
				Color colorMidrange = RGBAverager.mean(featureColorRange.getMin(), featureColorRange.getMax());
				double colorMidrangePreInterpolation = featureColorQuantityInterpolator.invert(colorMidrange);
				double rawMidColorQuantity = featureColorQuantityScaler.invert(colorMidrangePreInterpolation);
				
				// Add feature color legend
				double featureColorGradientLowerLeftX = Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS;
				double featureColorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
				double featureColorGradientWidth = 0.90 * Legend.DEFAULT_WIDTH_IN_POINTS;
				double featureColorGradientHeight = 15;
				String featureColorTypeLabel = "Region Color";	
				
				featureColorGradient = new ColorLegend(featureColorQuantityScalableRange, featureColorScaling, rawMidColorQuantity, featureColorRange, featureColorTypeLabel, featureColorQuantityAttribute, featureColorGradientLowerLeftX, featureColorGradientLowerLeftY, featureColorGradientWidth, featureColorGradientHeight);
			} catch (ZeroLengthInterpolatorInputRangeException e) {
				GeoMapsAlgorithm.logger.log(
						LogService.LOG_WARNING,
						"Warning: Couldn't interpolate region colors: " + e.getMessage(),
						e);
				} catch (InterpolatorInversionException e) {
					GeoMapsAlgorithm.logger.log(
						LogService.LOG_WARNING,
						"Warning: Couldn't reverse interpolation "
							+ "to make region color legend: " + e.getMessage(),
						e);
				}
			
			shapefileToPostScript.setFeatureColorAnnotations(SUBTITLE, interpolatedFeatureColorMap, featureColorGradient);
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
