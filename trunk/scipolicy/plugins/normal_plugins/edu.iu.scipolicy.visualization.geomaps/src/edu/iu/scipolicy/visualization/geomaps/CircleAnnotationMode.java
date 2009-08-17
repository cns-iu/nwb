package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Dictionary;
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
import edu.iu.scipolicy.visualization.geomaps.interpolation.InterpolatorInversionException;
import edu.iu.scipolicy.visualization.geomaps.interpolation.LinearInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ListInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.ZeroLengthInterpolatorInputRangeException;
import edu.iu.scipolicy.visualization.geomaps.legend.CircleAreaLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.ColorLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.legend.LegendComponent;
import edu.iu.scipolicy.visualization.geomaps.legend.NullLegendComponent;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.scaling.ListScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.Averager;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
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
	
	public static final String SUBTITLE = "with circle annotations";

	// ids provides a common index into areas and colorQuantities
	private MultiMap ids;
	private List<Double> areas;
	private List<Double> colorQuantities;

	/* 1: Read the relevant parameters
     * 2: Read the area and color data from inTable
	 * 3: Areas
	 *    	3a: Scale the area data
	 * 		3b: Interpolate the area data
     * 		3c: Make circle area LegendComponent
	 * 4: Colors
	 *    	4a: Scale the color data
	 * 		4b: Interpolate the color data
     * 		4c: Make circle color LegendComponent
	 * 5: Zip the area and color data together to make the List<Circle>
	 * 6: Apply this annotation (the List<Circle> and the LegendComponents) to postScriptWriter
	 */
	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(ShapefileToPostScriptWriter postScriptWriter, Table inTable, Dictionary parameters) throws AlgorithmExecutionException {
		String areaAttribute = (String) parameters.get(CIRCLE_AREA_ID);
		String colorQuantityAttribute = (String) parameters.get(CIRCLE_COLOR_QUANTITY_ID);
	
		String colorRangeKey = (String) parameters.get(CIRCLE_COLOR_RANGE_ID);
		Range<Color> colorRange = Constants.COLOR_RANGES.get(colorRangeKey);

		String areaScaling = (String) parameters.get(CIRCLE_AREA_SCALING_ID);
		Scaler areaScaler = ScalerFactory.createScaler(areaScaling);	
		String colorScaling = (String) parameters.get(CIRCLE_COLOR_SCALING_ID);
		Scaler colorQuantityScaler = ScalerFactory.createScaler(colorScaling);
		
		String latitudeAttribute = (String) parameters.get(LATITUDE_ID);
		String longitudeAttribute = (String) parameters.get(LONGITUDE_ID);
		setCircleData(inTable, latitudeAttribute, longitudeAttribute, areaAttribute, areaScaler, colorQuantityAttribute, colorQuantityScaler);
		
		if ( ids.isEmpty() ) {
			throw new AlgorithmExecutionException("No appropriate data found for circle annotations.");
		} else {
			// Scale and interpolate circle areas
			ListScaler areaListScaler = new ListScaler(areaScaler);
			List<Double> scaledAreas = areaListScaler.scale(areas);
			Range<Double> areaScalableRange = areaListScaler.getScalableRange();			
			Range<Double> interpolatedAreaRange = new Range<Double>(
					CirclePrinter.DEFAULT_CIRCLE_AREA_MINIMUM, 
					CirclePrinter.DEFAULT_CIRCLE_AREA_MAXIMUM);
			
			List<Double> interpolatedAreas = new ArrayList<Double>();
			LegendComponent circleAreaLegend = new NullLegendComponent();
			try {
				Interpolator<Double> areaInterpolator = new LinearInterpolator(scaledAreas, interpolatedAreaRange);
				ListInterpolator<Double> areaListInterpolator = new ListInterpolator<Double>(areaInterpolator);
				interpolatedAreas = areaListInterpolator.getInterpolatedList(scaledAreas);
				
				Range<Double> actualInterpolatedAreaRange = Range.calculateRange(interpolatedAreas);
				double areaMidrange = Averager.mean(actualInterpolatedAreaRange.getMin(), actualInterpolatedAreaRange.getMax());
				double areaMidrangePreimage = areaInterpolator.invert(areaMidrange);
				double rawMidArea = areaScaler.invert(areaMidrangePreimage);
				
				// Add circle area legend
				double areaLegendLowerLeftX = Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS;
				double areaLegendLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
				String areaTypeLabel = "Circle Area";
				circleAreaLegend = new CircleAreaLegend(areaScalableRange, areaScaling, rawMidArea, areaMidrange, interpolatedAreaRange, areaTypeLabel, areaAttribute, areaLegendLowerLeftX, areaLegendLowerLeftY);
			} catch (ZeroLengthInterpolatorInputRangeException e) {
				GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					"Warning: Couldn't interpolate circle areas: " + e.getMessage(),
					e);
			} catch (InterpolatorInversionException e) {
				GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					"Warning: Couldn't reverse interpolation "
						+ "to make circle area legend: " + e.getMessage(),
					e);
			}	

			// Scale and interpolate circle colors
			ListScaler colorQuantityListScaler = new ListScaler(colorQuantityScaler);
			List<Double> scaledColorQuantities = colorQuantityListScaler.scale(colorQuantities);
			Range<Double> colorQuantityScalableRange = colorQuantityListScaler.getScalableRange();
			
			List<Color> interpolatedColors = new ArrayList<Color>();
			LegendComponent colorGradient = new NullLegendComponent();
			try {
				Interpolator<Color> colorQuantityInterpolator = new ColorInterpolator(scaledColorQuantities, colorRange);
				ListInterpolator<Color> colorQuantityListInterpolator = new ListInterpolator<Color>(colorQuantityInterpolator);
				interpolatedColors = colorQuantityListInterpolator.getInterpolatedList(scaledColorQuantities);
	
				Color colorMidrange = Averager.mean(colorRange.getMin(), colorRange.getMax());
				double colorMidrangePreimage = colorQuantityInterpolator.invert(colorMidrange);
				double rawMidColorQuantity = colorQuantityScaler.invert(colorMidrangePreimage);
				
				// Add circle color legend
				double colorGradientLowerLeftX = Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS + (Legend.DEFAULT_WIDTH_IN_POINTS / 2.0);
				double colorGradientLowerLeftY = Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
				double colorGradientWidth =  0.8 * (Legend.DEFAULT_WIDTH_IN_POINTS / 2.0);
				double colorGradientHeight = 10;
				String colorTypeLabel = "Circle Color";
				colorGradient = new ColorLegend(colorQuantityScalableRange, colorScaling, rawMidColorQuantity, colorRange, colorTypeLabel, colorQuantityAttribute, colorGradientLowerLeftX, colorGradientLowerLeftY, colorGradientWidth, colorGradientHeight);
			} catch (ZeroLengthInterpolatorInputRangeException e) {
				GeoMapsAlgorithm.logger.log(
						LogService.LOG_WARNING,
						"Warning: Couldn't interpolate circle colors: " + e.getMessage(),
						e);
				} catch (InterpolatorInversionException e) {
					GeoMapsAlgorithm.logger.log(
						LogService.LOG_WARNING,
						"Warning: Couldn't reverse interpolation "
							+ "to make circle color legend: " + e.getMessage(),
						e);
				}
			
			/* Construct the list of Circles from the specified areas and colors.
			 * Note we expect that every Coordinate with an area specified has a color specified
			 * and that every Coordinate with a color specified has an area specified.
			 */
			List<Circle> circles = new ArrayList<Circle>();
			for ( Object circleID : ids.entrySet() ) {
				Coordinate coordinate = ((Map.Entry<Coordinate, List<Integer>>) circleID).getKey();
				List<Integer> ids = ((Map.Entry<Coordinate, List<Integer>>) circleID).getValue();
				
				for ( int id : ids ) {
					double area = interpolatedAreas.get(id);
					Color color = interpolatedColors.get(id);

					circles.add(new Circle(coordinate, area, color));
				}
			}
			
			postScriptWriter.setCircleAnnotations(SUBTITLE, circles, circleAreaLegend, colorGradient);
		}
	}
	
	private void setCircleData(Table inTable, String latitudeAttribute, String longitudeAttribute, String circleAreaAttribute, Scaler circleAreaScaler, String circleColorQuantityAttribute, Scaler circleColorQuantityScaler) throws AlgorithmExecutionException {
		areas = new ArrayList<Double>();
		colorQuantities = new ArrayList<Double>();
		ids = new MultiHashMap();
		
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
						
						areas.add(area);
						colorQuantities.add(colorQuantity);
						ids.put(coordinate, id);
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
TODO: Rewrite this class in the following way:

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