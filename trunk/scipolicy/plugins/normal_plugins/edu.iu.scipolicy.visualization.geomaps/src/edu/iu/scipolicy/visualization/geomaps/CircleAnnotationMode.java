package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleMapInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.LinearInterpolator;
import edu.iu.scipolicy.visualization.geomaps.legend.AreaLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleMapScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.PrefuseDoubleReader;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class CircleAnnotationMode implements AnnotationMode {	
	public static final String LATITUDE_ID = "latitude";
	public static final String LONGITUDE_ID = "longitude";
	public static final String CIRCLE_AREA_ID = "circleArea";
	public static final String CIRCLE_AREA_SCALING_ID = "circleAreaScaling";
	public static final String CIRCLE_COLOR_QUANTITY_ID = "circleColorQuantity";
	public static final String CIRCLE_COLOR_SCALING_ID = "circleColorScaling";
	public static final String CIRCLE_COLOR_RANGE_ID = "circleColorRange";
	public static final Map<String, Range<Color>> COLOR_RANGES;
	static {
		Map<String, Range<Color>> t = new HashMap<String, Range<Color>>();
		t.put("Cyan to burgundy", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}

	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(Table inTable, Dictionary parameters, ShapefileToPostScript shapefileToPostScript) throws AlgorithmExecutionException {
		String latitudeAttribute = (String) parameters.get(LATITUDE_ID);
		String longitudeAttribute = (String) parameters.get(LONGITUDE_ID);
		
		String circleAreaAttribute = (String) parameters.get(CIRCLE_AREA_ID);		
		Map<Coordinate, Double> circleAreaMap = getCircleAreaMap(inTable, latitudeAttribute, longitudeAttribute, circleAreaAttribute);
		String circleAreaScaling = (String) parameters.get(CIRCLE_AREA_SCALING_ID);
		DoubleScaler circleAreaScaler = ScalerFactory.createScaler(circleAreaScaling);
		
		String circleColorQuantityAttribute = (String) parameters.get(CIRCLE_COLOR_QUANTITY_ID);
		Map<Coordinate, Double> circleColorQuantityMap = getCircleColorQuantityMap(inTable, latitudeAttribute, longitudeAttribute, circleColorQuantityAttribute);
		String circleColorScaling = (String) parameters.get(CIRCLE_COLOR_SCALING_ID);
		DoubleScaler circleColorQuantityScaler = ScalerFactory.createScaler(circleColorScaling);
		String circleColorRangeKey = (String) parameters.get(CIRCLE_COLOR_RANGE_ID);
		Range<Color> circleColorRange = COLOR_RANGES.get(circleColorRangeKey);
		
		Map<Coordinate, Circle> interpolatedCircleMap = new HashMap<Coordinate, Circle>();		
		if ( circleAreaMap.keySet().equals(circleColorQuantityMap.keySet()) ) {
			if ( circleAreaMap.isEmpty() ) {
				// TODO Throw exception?
				GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, "No appropriate data found for circle annotations.");
			}
			else {
				// Scale and interpolate circle areas
				DoubleMapScaler<Coordinate> circleAreaDoubleMapScaler = new DoubleMapScaler<Coordinate>(circleAreaScaler);
				Map<Coordinate, Double> scaledCircleAreaMap = circleAreaDoubleMapScaler.scale(circleAreaMap);
				Range<Double> circleAreaScalableRange = circleAreaDoubleMapScaler.getScalableRange();
				Range<Double> interpolatedCircleAreaRange = new Range<Double>(CirclePrinter.DEFAULT_CIRCLE_AREA_MINIMUM, CirclePrinter.DEFAULT_CIRCLE_AREA_MAXIMUM);
				DoubleInterpolator<Double> circleAreaInterpolator = new LinearInterpolator(scaledCircleAreaMap.values(), interpolatedCircleAreaRange);
				Map<Coordinate, Double> interpolatedCircleAreaMap = (new DoubleMapInterpolator<Coordinate, Double>(circleAreaInterpolator)).getInterpolatedMap(scaledCircleAreaMap);
		
				// Add circle area legend
				double areaLegendLowerLeftX = .38 * Legend.DEFAULT_WIDTH_IN_POINTS;
				double areaLegendLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
				String areaTypeLabel = "Circle Size";
				AreaLegend circleAreaLegend = new AreaLegend(circleAreaScalableRange, interpolatedCircleAreaRange, areaTypeLabel, circleAreaAttribute, areaLegendLowerLeftX, areaLegendLowerLeftY);
				
				// Scale and interpolate circle colors
				DoubleMapScaler<Coordinate> circleColorQuantityMapScaler = new DoubleMapScaler<Coordinate>(circleColorQuantityScaler);
				Map<Coordinate, Double> scaledCircleColorQuantityMap = circleColorQuantityMapScaler.scale(circleColorQuantityMap);
				Range<Double> circleColorQuantityScalableRange = circleColorQuantityMapScaler.getScalableRange();
				DoubleInterpolator<Color> circleColorInterpolator = new ColorInterpolator(scaledCircleColorQuantityMap.values(), circleColorRange);
				Map<Coordinate, Color> interpolatedCircleColorMap = (new DoubleMapInterpolator<Coordinate, Color>(circleColorInterpolator)).getInterpolatedMap(scaledCircleColorQuantityMap);
	
				// Add circle color legend
				double circleColorGradientLowerLeftX = 2 * Legend.DEFAULT_WIDTH_IN_POINTS/3;
				double circleColorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
				double circleColorGradientWidth = Legend.DEFAULT_WIDTH_IN_POINTS/3 * .90;
				double circleColorGradientHeight = 15;
				String circleColorTypeLabel = "Circle Color";
				LabeledGradient circleColorGradient = new LabeledGradient(circleColorQuantityScalableRange, circleColorRange, circleColorTypeLabel, circleColorQuantityAttribute, circleColorGradientLowerLeftX, circleColorGradientLowerLeftY, circleColorGradientWidth, circleColorGradientHeight);
				
				/* Construct the Circle map from the specified areas and colors.
				 * Note we require that every Coordinate with an area specified has a color specified
				 * and that every Coordinate with a color specified has an area specified.
				 */
				interpolatedCircleMap = new HashMap<Coordinate, Circle>();
				assert( interpolatedCircleAreaMap.keySet().equals(interpolatedCircleColorMap.keySet()) ); // TODO
				for ( Entry<Coordinate, Double> circleAreaMapEntry : interpolatedCircleAreaMap.entrySet() ) {
					Coordinate coordinate = circleAreaMapEntry.getKey();
					double area = circleAreaMapEntry.getValue();
					Color color = interpolatedCircleColorMap.get(coordinate);
	
					interpolatedCircleMap.put(coordinate, new Circle(area, color));
				}
				
				shapefileToPostScript.setCircleAnnotations(interpolatedCircleMap, circleAreaLegend, circleColorGradient);
			}
		}
		else {
			throw new AlgorithmExecutionException("Every circle annotation must have both a size and a color specified.");
		}
	}
	
	private Map<Coordinate, Double> getCircleAreaMap(Table inTable, String latitudeAttribute, String longitudeAttribute, String circleAreaAttribute) throws AlgorithmExecutionException {
		Map<Coordinate, Double> circleAreaMap = new HashMap<Coordinate, Double>();
		
		int duplicateCoordinateKeys = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());
			
			boolean latitudeSpecified = PrefuseDoubleReader.isSpecified(row, latitudeAttribute);
			boolean longitudeSpecified = PrefuseDoubleReader.isSpecified(row, longitudeAttribute);
			boolean circleAreaSpecified = PrefuseDoubleReader.isSpecified(row, circleAreaAttribute);
			
			if ( latitudeSpecified && longitudeSpecified && circleAreaSpecified ) {
				double latitude = PrefuseDoubleReader.get(row, latitudeAttribute);
				double longitude = PrefuseDoubleReader.get(row, longitudeAttribute);
				double area = PrefuseDoubleReader.get(row, circleAreaAttribute);
				
				Coordinate coordinate = new Coordinate(longitude, latitude);
				
				if ( circleAreaMap.containsKey(coordinate) ) {
					duplicateCoordinateKeys++;
				}
				else {
					circleAreaMap.put(coordinate, area);
				}
			}
		}
		if ( duplicateCoordinateKeys > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, duplicateCoordinateKeys + " duplicate coordinate keys detected.  Only the first was read.");
		}
		
		return circleAreaMap;
	}
	
	private Map<Coordinate, Double> getCircleColorQuantityMap(Table inTable, String latitudeAttribute, String longitudeAttribute, String circleColorQuantityAttribute) throws AlgorithmExecutionException {
		Map<Coordinate, Double> circleColorQuantityMap = new HashMap<Coordinate, Double>();
		
		int duplicateCoordinateKeys = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());
			
			boolean latitudeSpecified = PrefuseDoubleReader.isSpecified(row, latitudeAttribute);
			boolean longitudeSpecified = PrefuseDoubleReader.isSpecified(row, longitudeAttribute);
			boolean circleColorQuantitySpecified = PrefuseDoubleReader.isSpecified(row, circleColorQuantityAttribute);
			
			if ( latitudeSpecified && longitudeSpecified && circleColorQuantitySpecified ) {		
				double latitude = PrefuseDoubleReader.get(row, latitudeAttribute);
				double longitude = PrefuseDoubleReader.get(row, longitudeAttribute);
				double colorQuantity = PrefuseDoubleReader.get(row, circleColorQuantityAttribute);
				
				Coordinate coordinate = new Coordinate(longitude, latitude);
				
				if ( circleColorQuantityMap.containsKey(coordinate) ) {
					duplicateCoordinateKeys++;
				}
				else {
					circleColorQuantityMap.put(coordinate, colorQuantity);
				}
			}
		}
		if ( duplicateCoordinateKeys > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, duplicateCoordinateKeys + " duplicate coordinate keys detected.  Only the first was read.");
		}
		
		return circleColorQuantityMap;
	}
}
