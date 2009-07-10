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
	public void printPS(Table inTable, Dictionary parameters, File temporaryPostScriptFile, ProjectedCRS projectedCRS, URL shapefileURL)
			throws AlgorithmExecutionException, IOException {
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
		
		// TODO Extremely temporary //////////
		Map<String, Double> featureColorQuantityMap = new HashMap<String, Double>();
		String featureColorQuantityAttribute = "";
		DoubleScaler featureColorQuantityScaler = new LinearScaler();
		Range<Color> featureColorRange = new Range<Color>(Color.WHITE, Color.WHITE);
		//////////////////////////////////////
		
		ShapefileToPostScript shapefileToPostScript = new ShapefileToPostScript(shapefileURL, projectedCRS);
		
		shapefileToPostScript.printPostScript(temporaryPostScriptFile, featureColorQuantityMap, featureColorQuantityAttribute, featureColorQuantityScaler, featureColorRange, circleAreaMap, circleAreaAttribute, circleAreaScaler, circleColorQuantityMap, circleColorQuantityAttribute, circleColorQuantityScaler, circleColorRange);
	}
	
	private Map<Coordinate, Double> getCircleAreaMap(Table inTable, String latitudeAttribute, String longitudeAttribute, String circleAreaAttribute) throws AlgorithmExecutionException {
		Map<Coordinate, Double> circleAreaMap = new HashMap<Coordinate, Double>();
		
		int duplicateCoordinateKeys = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());
			
			boolean latitudeSpecified = PrefuseDoubleReader.specified(row, latitudeAttribute);
			boolean longitudeSpecified = PrefuseDoubleReader.specified(row, longitudeAttribute);
			boolean circleAreaSpecified = PrefuseDoubleReader.specified(row, circleAreaAttribute);
			
			if ( latitudeSpecified && longitudeSpecified && circleAreaSpecified ) {		
				double latitude = PrefuseDoubleReader.get(row, latitudeAttribute);
				double longitude = PrefuseDoubleReader.get(row, longitudeAttribute);
				double area = PrefuseDoubleReader.get(row, circleAreaAttribute);
				
				Coordinate coordinate = new Coordinate(longitude, latitude);
				
				if ( circleAreaMap.containsKey(coordinate) ) {
					duplicateCoordinateKeys++;
				}
				else {
					System.out.println("Adding " + coordinate + " to " + area);
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
			
			boolean latitudeSpecified = PrefuseDoubleReader.specified(row, latitudeAttribute);
			boolean longitudeSpecified = PrefuseDoubleReader.specified(row, longitudeAttribute);
			boolean circleColorQuantitySpecified = PrefuseDoubleReader.specified(row, circleColorQuantityAttribute);
			
			if ( latitudeSpecified && longitudeSpecified && circleColorQuantitySpecified ) {		
				double latitude = PrefuseDoubleReader.get(row, latitudeAttribute);
				double longitude = PrefuseDoubleReader.get(row, longitudeAttribute);
				double colorQuantity = PrefuseDoubleReader.get(row, circleColorQuantityAttribute);
				
				Coordinate coordinate = new Coordinate(longitude, latitude);
				
				if ( circleColorQuantityMap.containsKey(coordinate) ) {
					duplicateCoordinateKeys++;
				}
				else {
					System.out.println("Adding " + coordinate + " to " + colorQuantity);
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
