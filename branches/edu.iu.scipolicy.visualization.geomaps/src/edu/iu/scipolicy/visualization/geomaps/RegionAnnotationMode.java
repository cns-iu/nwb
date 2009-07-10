package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.opengis.referencing.crs.ProjectedCRS;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.LinearScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.PrefuseDoubleReader;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class RegionAnnotationMode implements AnnotationMode {
	public static final String FEATURE_NAME_ID = "featureName";
	public static final String FEATURE_COLOR_QUANTITY_ID = "featureColorQuantity";
	public static final String FEATURE_COLOR_SCALING_ID = "featureColorScaling";
	public static final String FEATURE_COLOR_RANGE_ID = "featureColorRange";
	
	public static final Map<String, Range<Color>> COLOR_RANGES;
	static {
		Map<String, Range<Color>> t = new HashMap<String, Range<Color>>();
		t.put("Cyan to burgundy", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}

	@SuppressWarnings("unchecked") // TODO
	public void printPS(Table inTable, Dictionary parameters, File temporaryPostScriptFile, ProjectedCRS projectedCRS, URL shapefileURL) throws AlgorithmExecutionException, IOException {
		
		
		String featureNameAttribute = (String) parameters.get(FEATURE_NAME_ID);
		String featureColorQuantityAttribute = (String) parameters.get(FEATURE_COLOR_QUANTITY_ID);	
		System.out.println("Value of " + FEATURE_COLOR_QUANTITY_ID + " is " + featureColorQuantityAttribute);
		Map<String, Double> featureColorQuantityMap = getFeatureColorQuantityMap(inTable, featureNameAttribute, featureColorQuantityAttribute);
		String featureColorScaling = (String) parameters.get(FEATURE_COLOR_SCALING_ID);
		DoubleScaler featureColorQuantityScaler = ScalerFactory.createScaler(featureColorScaling);
		String featureColorRangeKey = (String) parameters.get(FEATURE_COLOR_RANGE_ID);
		Range<Color> featureColorRange = COLOR_RANGES.get(featureColorRangeKey);
		
		// TODO Extremely temporary ////////////////////
		Map<Coordinate, Double> circleAreaMap = new HashMap<Coordinate, Double>();
		String circleAreaAttribute = "";		
		DoubleScaler circleAreaScaler = new LinearScaler();
		Map<Coordinate, Double> circleColorQuantityMap = new HashMap<Coordinate, Double>();
		String circleColorQuantityAttribute = "";
		DoubleScaler circleColorQuantityScaler = new LinearScaler();
		Range<Color> circleColorRange = new Range<Color>(Color.WHITE, Color.WHITE);
		////////////////////////////////////////////////
		
		ShapefileToPostScript shapefileToPostScript = new ShapefileToPostScript(shapefileURL, projectedCRS);
		shapefileToPostScript.printPostScript(temporaryPostScriptFile, featureColorQuantityMap, featureColorQuantityAttribute, featureColorQuantityScaler, featureColorRange, circleAreaMap, circleAreaAttribute, circleAreaScaler, circleColorQuantityMap, circleColorQuantityAttribute, circleColorQuantityScaler, circleColorRange);
	}

	private Map<String, Double> getFeatureColorQuantityMap(Table inTable, String featureNameAttribute, String featureColorQuantityAttribute) throws AlgorithmExecutionException {
		Map<String, Double> featureColorQuantityMap = new HashMap<String, Double>();
		
		int duplicateFeatureNameKeys = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());

			boolean featureNameSpecified = row.canGetString(featureNameAttribute);
			boolean featureColorQuantitySpecified = PrefuseDoubleReader.specified(row, featureColorQuantityAttribute);
			if ( featureNameSpecified && featureColorQuantitySpecified ) {					
				String featureName = row.getString(featureNameAttribute);
				// TODO: GeoCode featureName here!
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
