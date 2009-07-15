package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.Interpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ListInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.LinearInterpolator;
import edu.iu.scipolicy.visualization.geomaps.legend.AreaLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.scaling.ListScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
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
		t.put("Saffron to crimson", new Range<Color>(new Color(254, 204, 92), new Color(177, 4, 39)));
		t.put("Lemon chiffon to egyptian blue", new Range<Color>(new Color(255, 255, 158), new Color(37, 52, 148)));
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}
	
	private MultiMap circleIDs;
	private List<Double> circleAreas;
	private List<Double> circleColorQuantities;

	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(Table inTable, Dictionary parameters, ShapefileToPostScriptWriter shapefileToPostScript) throws AlgorithmExecutionException {
		// STEPS:
		// Scale
		// Interpolate
		// Make a legend component
		// ALSO: creates shape file to post script writer
		String latitudeAttribute = (String) parameters.get(LATITUDE_ID);
		String longitudeAttribute = (String) parameters.get(LONGITUDE_ID);		
		String areaAttribute = (String) parameters.get(CIRCLE_AREA_ID);			
		String colorQuantityAttribute = (String) parameters.get(CIRCLE_COLOR_QUANTITY_ID);		
		String areaScaling = (String) parameters.get(CIRCLE_AREA_SCALING_ID);
		Scaler areaScaler = ScalerFactory.createScaler(areaScaling);	
		String colorScaling = (String) parameters.get(CIRCLE_COLOR_SCALING_ID);
		Scaler colorQuantityScaler = ScalerFactory.createScaler(colorScaling);
		String colorRangeKey = (String) parameters.get(CIRCLE_COLOR_RANGE_ID);
		Range<Color> colorRange = COLOR_RANGES.get(colorRangeKey);
		
		setCircleData(inTable, latitudeAttribute, longitudeAttribute, areaAttribute, areaScaler, colorQuantityAttribute, colorQuantityScaler);
		
		if ( circleIDs.isEmpty() ) {
			// TODO Throw exception?
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, "No appropriate data found for circle annotations.");
		}
		else {
			// Scale and interpolate circle areas
			// TODO: Refactor these variable names.
			ListScaler circleAreaListScaler = new ListScaler(areaScaler);
			List<Double> scaledCircleAreas = circleAreaListScaler.scale(circleAreas);
			Range<Double> circleAreaScalableRange = circleAreaListScaler.getScalableRange();			
			Range<Double> interpolatedCircleAreaRange = new Range<Double>(CirclePrinter.DEFAULT_CIRCLE_AREA_MINIMUM, CirclePrinter.DEFAULT_CIRCLE_AREA_MAXIMUM);
			Interpolator<Double> circleAreaInterpolator = new LinearInterpolator(scaledCircleAreas, interpolatedCircleAreaRange);
			List<Double> interpolatedCircleAreas = (new ListInterpolator<Double>(circleAreaInterpolator)).getInterpolatedList(scaledCircleAreas);

			// Add circle area legend
			double areaLegendLowerLeftX = .38 * Legend.DEFAULT_WIDTH_IN_POINTS;
			double areaLegendLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
			String areaTypeLabel = "Circle Size";
			AreaLegend circleAreaLegend = new AreaLegend(circleAreaScalableRange, interpolatedCircleAreaRange, areaTypeLabel, areaAttribute, areaLegendLowerLeftX, areaLegendLowerLeftY);
			
			// Scale and interpolate circle colors
			ListScaler circleColorQuantityListScaler = new ListScaler(colorQuantityScaler);
			List<Double> scaledCircleColorQuantities = circleColorQuantityListScaler.scale(circleColorQuantities);
			Range<Double> circleColorQuantityScalableRange = circleColorQuantityListScaler.getScalableRange();
			Interpolator<Color> circleColorInterpolator = new ColorInterpolator(scaledCircleColorQuantities, colorRange);
			List<Color> interpolatedCircleColors = (new ListInterpolator<Color>(circleColorInterpolator)).getInterpolatedList(scaledCircleColorQuantities);

			// Add circle color legend
			double circleColorGradientLowerLeftX = 2 * Legend.DEFAULT_WIDTH_IN_POINTS/3;
			double circleColorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
			double circleColorGradientWidth = Legend.DEFAULT_WIDTH_IN_POINTS/3 * .90;
			double circleColorGradientHeight = 15;
			String circleColorTypeLabel = "Circle Color";
			LabeledGradient circleColorGradient = new LabeledGradient(circleColorQuantityScalableRange, colorRange, circleColorTypeLabel, colorQuantityAttribute, circleColorGradientLowerLeftX, circleColorGradientLowerLeftY, circleColorGradientWidth, circleColorGradientHeight);
			
			/* Construct the list of Circles from the specified areas and colors.
			 * Note we require that every Coordinate with an area specified has a color specified
			 * and that every Coordinate with a color specified has an area specified.
			 */
			List<Circle> circles = new ArrayList<Circle>();
			assert( // TODO
				circleIDs.size() == interpolatedCircleAreas.size()
				&& interpolatedCircleAreas.size() == interpolatedCircleColors.size()			
			); 
			for ( Object circleID : circleIDs.entrySet() ) {
				Coordinate coordinate = ((Map.Entry<Coordinate, List<Integer>>) circleID).getKey();
				List<Integer> ids = ((Map.Entry<Coordinate, List<Integer>>) circleID).getValue();
				
				for ( int id : ids ) {
					double area = interpolatedCircleAreas.get(id);
					Color color = interpolatedCircleColors.get(id);

					circles.add(new Circle(coordinate, area, color));
				}
			}
			
			shapefileToPostScript.setCircleAnnotations(circles, circleAreaLegend, circleColorGradient);
		}
	}
	
	private void setCircleData(Table inTable, String latitudeAttribute, String longitudeAttribute, String circleAreaAttribute, Scaler circleAreaScaler, String circleColorQuantityAttribute, Scaler circleColorQuantityScaler) throws AlgorithmExecutionException {
		circleAreas = new ArrayList<Double>();
		circleColorQuantities = new ArrayList<Double>();
		circleIDs = new MultiHashMap();
		
		int incompleteSpecificationCount = 0;
		int unscalableCount = 0;
		
		int id = 0;
		for( TableIterator tableIterator = inTable.iterator(); tableIterator.hasNext(); ) {
			Tuple row = inTable.getTuple(tableIterator.nextInt());
			
			boolean latitudeSpecified = PrefuseDoubleReader.isSpecified(row, latitudeAttribute);
			boolean longitudeSpecified = PrefuseDoubleReader.isSpecified(row, longitudeAttribute);
			
			if ( latitudeSpecified && longitudeSpecified ) {
				boolean circleAreaSpecified = PrefuseDoubleReader.isSpecified(row, circleAreaAttribute);
				boolean circleColorQuantitySpecified = PrefuseDoubleReader.isSpecified(row, circleColorQuantityAttribute);
				
				if ( circleAreaSpecified && circleColorQuantitySpecified ) {
					double latitude = PrefuseDoubleReader.get(row, latitudeAttribute);
					double longitude = PrefuseDoubleReader.get(row, longitudeAttribute);
					double area = PrefuseDoubleReader.get(row, circleAreaAttribute);
					double colorQuantity = PrefuseDoubleReader.get(row, circleColorQuantityAttribute);
					
					if ( circleAreaScaler.canScale(area) && circleColorQuantityScaler.canScale(colorQuantity) ) {
						Coordinate coordinate = new Coordinate(longitude, latitude);
						
						circleAreas.add(area);
						circleColorQuantities.add(colorQuantity);
						circleIDs.put(coordinate, id);
						id++;
					}
					else {
						unscalableCount++;
					}
				}
				else {
					incompleteSpecificationCount++;
				}
			}
		}
		
		if ( incompleteSpecificationCount > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, incompleteSpecificationCount + " rows specified latitude and longitude but not both area and color.. rows skipped.");
		}
		
		if ( unscalableCount > 0 ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, unscalableCount + " rows contained an unscalable value for either or both of area and color.. rows skipped.");
		}
	}
}

/*
 *

class CircleSpecification {
	double area;
	double colorQuantity;
	double lat, lng;
	
	CircleSpecification(area, colorQuantity, lat, lng, scaler) {
		this.area = area
		...
		
		if (scaler..area < 0 ||
				this.colorQuantiy < 0) {
			isValid = false;
		}
}

List<CircleSpecification> circleSpecs;

// TODO: new name
annotation(container) {
	// Get dataContainerOutOfParameters
	// Create circleSpecs
	Range minMax = new Range();

	unScaledAreaIterator = new CircleAreaIterator(container);
	// Side effects minMax
	scaledCircleAreas = scale(unScaledAreaIterator, minMax);
	scaledAreaIterator = new CircleAreaIterator(scaledCircleAreas)
	interpolatedCircleAreas = doInterpolation(scaledAreaIterator, minMax);
	sizeComponent = makeAreaLegend(interpolatedCircleAreas);
	
	unscaledColorIterator = new CircleColorIterator(container);
	// Side effects minMax
	scaledCircleColors = scale(unscaledColorIterator, minMax);
	scaledColorIterator = new CircleColorIterator(scaledCircleColors)
	interpolatedCircleColors = doInterpolation(scaledColorIterator, minMax);
	colorComponent = makeColorLegend(interpolatedCircleColors);
}

class CircleAreaIterator implements Iterator {
	List<CircleSpecification> circleSpecs;
	int currentCircleSpec = 0;
	
	getNext() {
		nextCircleSpec = circleSpecs[currentCircleSpec]
		currentCircleSpec++
		
		return nextCircleSpec.area;
	}
}

class CircleColorIterator implements Iterator {
	List<CircleSpecification> circleSpecs;
	int currentCircleSpec = 0;
	
	getNext() {
		nextCircleSpec = circleSpecs[currentCircleSpec]
		currentCircleSpec++
		
		return nextCircleSpec.colorQuantity;
	}
}
*/