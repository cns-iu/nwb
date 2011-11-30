package edu.iu.sci2.visualization.geomaps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.sci2.visualization.geomaps.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.interpolation.InterpolatorInversionException;
import edu.iu.sci2.visualization.geomaps.interpolation.LinearInterpolator;
import edu.iu.sci2.visualization.geomaps.legend.CircleAreaLegend;
import edu.iu.sci2.visualization.geomaps.legend.ColorLegend;
import edu.iu.sci2.visualization.geomaps.legend.Legend;
import edu.iu.sci2.visualization.geomaps.legend.LegendComponent;
import edu.iu.sci2.visualization.geomaps.legend.NullLegendComponent;
import edu.iu.sci2.visualization.geomaps.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.printing.Circle;
import edu.iu.sci2.visualization.geomaps.printing.CirclePrinter;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.FillColorStrategy;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.NullColorStrategy;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.StrokeColorStrategy;
import edu.iu.sci2.visualization.geomaps.scaling.Scaler;
import edu.iu.sci2.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.sci2.visualization.geomaps.utility.Averager;
import edu.iu.sci2.visualization.geomaps.utility.Constants;
import edu.iu.sci2.visualization.geomaps.utility.Range;

public class CircleAnnotationMode extends AnnotationMode {
	// Page layout sizes and dimensions
	public static final double AREA_LEGEND_LOWER_LEFT_X =
		Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS
		+ (2.0 * Legend.DEFAULT_WIDTH_IN_POINTS / 3.0);
	
	public static final double INNER_COLOR_LEGEND_LOWER_LEFT_X =
		Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS;
	public static final double INNER_COLOR_GRADIENT_WIDTH =
		0.8 * (Legend.DEFAULT_WIDTH_IN_POINTS / 3.0);
	public static final int INNER_COLOR_GRADIENT_HEIGHT = 10;
	
	public static final double OUTER_COLOR_LEGEND_LOWER_LEFT_X =
		Legend.DEFAULT_LOWER_LEFT_X_IN_POINTS
		+ (1.0 * Legend.DEFAULT_WIDTH_IN_POINTS / 3.0);
	public static final double OUTER_COLOR_GRADIENT_WIDTH = INNER_COLOR_GRADIENT_WIDTH;
	public static final int OUTER_COLOR_GRADIENT_HEIGHT = INNER_COLOR_GRADIENT_HEIGHT;
	
	// User parameter IDs
	// The *_ID values must match with the 'name' field in OSGI-INF/metatype/METADATA.XML
	public static final String LATITUDE_ID = "latitude";
	public static final String LONGITUDE_ID = "longitude";
	public static final String AREA_ID = "circleArea";
	public static final String AREA_SCALING_ID = "circleAreaScaling";
	public static final String INNER_COLOR_QUANTITY_ID = "innerColorQuantity";
	public static final String USE_NO_INNER_COLOR_TOKEN = "None (no inner color)";	
	public static final String INNER_COLOR_SCALING_ID = "innerColorScaling";
	public static final String INNER_COLOR_RANGE_ID = "innerColorRange";
	public static final String OUTER_COLOR_QUANTITY_ID = "outerColorQuantity";
	public static final String USE_NO_OUTER_COLOR_TOKEN = "None (no outer color)";
	public static final String OUTER_COLOR_SCALING_ID = "outerColorScaling";
	public static final String OUTER_COLOR_RANGE_ID = "outerColorRange";
	
	public static final String SUBTITLE = "Circle Annotation Style";	

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
	@Override
	public void applyAnnotations(
			ShapefileToPostScriptWriter postScriptWriter,
			Table inTable,
			Dictionary<String, Object> parameters)
				throws AlgorithmExecutionException {
		// Read parameters
		String latitudeAttribute = (String) parameters.get(LATITUDE_ID);
		String longitudeAttribute = (String) parameters.get(LONGITUDE_ID);
		
		String areaValueAttribute = (String) parameters.get(AREA_ID);
		String areaValueScaling = (String) parameters.get(AREA_SCALING_ID);
		Scaler areaValueScaler = ScalerFactory.createScaler(areaValueScaling);
		Range<Double> areaRange =
			new Range<Double>(
				CirclePrinter.DEFAULT_CIRCLE_AREA_MINIMUM, 
				CirclePrinter.DEFAULT_CIRCLE_AREA_MAXIMUM);
		
		
		String innerColorValueAttribute =
			(String) parameters.get(INNER_COLOR_QUANTITY_ID);
		boolean isUsingInnerColor = true;
		if (USE_NO_INNER_COLOR_TOKEN.equals(innerColorValueAttribute)) {
			isUsingInnerColor = false;
		}
		String innerColorScaling = (String) parameters.get(INNER_COLOR_SCALING_ID);
		Scaler innerColorValueScaler = ScalerFactory.createScaler(innerColorScaling);
		String innerColorRangeKey = (String) parameters.get(INNER_COLOR_RANGE_ID);
		Range<Color> innerColorRange = Constants.COLOR_RANGES.get(innerColorRangeKey);
		
		String outerColorValueAttribute =
			(String) parameters.get(OUTER_COLOR_QUANTITY_ID);
		boolean isUsingOuterColor = true;		
		if (USE_NO_OUTER_COLOR_TOKEN.equals(outerColorValueAttribute)) {
			isUsingOuterColor = false;
		}
		String outerColorScaling = (String) parameters.get(OUTER_COLOR_SCALING_ID);
		Scaler outerColorValueScaler = ScalerFactory.createScaler(outerColorScaling);
		String outerColorRangeKey = (String) parameters.get(OUTER_COLOR_RANGE_ID);
		Range<Color> outerColorRange = Constants.COLOR_RANGES.get(outerColorRangeKey);
		
		
		
		
		// Set up area interpolator
		Range<Double> areaValueScalableRange =
			AnnotationMode.calculateScalableRangeOverColumn(
					inTable, areaValueAttribute, areaValueScaler);
		Interpolator<Double> areaInterpolator =
			new LinearInterpolator(
					areaValueScaler.scale(areaValueScalableRange),
					areaRange);
		String areaNumberFormat = NumberFormatFactory.guessNumberFormat(
				areaValueAttribute, areaValueScalableRange);
		
		// Set up inner color interpolator
		Range<Double> innerColorValueScalableRange = null;
		Interpolator<Color> innerColorQuantityInterpolator = null;
		if (isUsingInnerColor) {
			innerColorValueScalableRange =
				AnnotationMode.calculateScalableRangeOverColumn(
						inTable, innerColorValueAttribute, innerColorValueScaler);
			innerColorQuantityInterpolator =
				new ColorInterpolator(
						innerColorValueScaler.scale(innerColorValueScalableRange),
						innerColorRange);
		}
		String innerColorNumberFormat = NumberFormatFactory.guessNumberFormat(
				innerColorValueAttribute, innerColorValueScalableRange);
		
		// Set up outer color interpolator
		Range<Double> outerColorValueScalableRange = null;
		Interpolator<Color> outerColorQuantityInterpolator = null;
		if (isUsingOuterColor) {
			outerColorValueScalableRange =
				AnnotationMode.calculateScalableRangeOverColumn(
						inTable, outerColorValueAttribute, outerColorValueScaler);
			
			outerColorQuantityInterpolator =
				new ColorInterpolator(
						outerColorValueScaler.scale(outerColorValueScalableRange),
						outerColorRange);
		}
		String outerColorNumberFormat = NumberFormatFactory.guessNumberFormat(
				outerColorValueAttribute, outerColorValueScalableRange);
		
		
		// Read, scale, and interpolate data from inTable
		int incompleteSpecificationCount = 0;
		int unscalableValueCount = 0;
		List<Circle> circles = new ArrayList<Circle>();
		for (TableIterator tableIt = inTable.iterator(); tableIt.hasNext();) {
			Tuple row = inTable.getTuple(tableIt.nextInt());
			
			try {
				double latitude =
					NumberUtilities.interpretObjectAsDouble(row.get(latitudeAttribute));
				double longitude =
					NumberUtilities.interpretObjectAsDouble(row.get(longitudeAttribute));

				double areaValue =
					NumberUtilities.interpretObjectAsDouble(row.get(areaValueAttribute));				
				double area = Double.NaN;
				if (areaValueScaler.canScale(areaValue)) {
					area = areaInterpolator.interpolate(
							areaValueScaler.scale(areaValue));
				} else {
					unscalableValueCount++;
					continue;
				}
				
				ColorStrategy innerColorStrategy = new NullColorStrategy();
				if (isUsingInnerColor
						&& (innerColorQuantityInterpolator != null)) {
					double innerColorValue =
						NumberUtilities.interpretObjectAsDouble(
								row.get(innerColorValueAttribute));
					
					if (innerColorValueScaler.canScale(innerColorValue)) {
						innerColorStrategy =
							new FillColorStrategy(
									innerColorQuantityInterpolator.interpolate(
											innerColorValueScaler.scale(
													innerColorValue)));
					} else {
						unscalableValueCount++;
						continue;
					}
				}
				
				ColorStrategy outerColorStrategy = new StrokeColorStrategy();
				if (isUsingOuterColor
						&& (outerColorQuantityInterpolator != null)) {
					double outerColorValue =
						NumberUtilities.interpretObjectAsDouble(
								row.get(outerColorValueAttribute));
					
					if (outerColorValueScaler.canScale(outerColorValue)) {
						outerColorStrategy =
							new StrokeColorStrategy(
									outerColorQuantityInterpolator.interpolate(
											outerColorValueScaler.scale(
													outerColorValue)));
					} else {
						unscalableValueCount++;
						continue;
					}
				}
				
				circles.add(
						new Circle(
								new Coordinate(longitude, latitude),
								area,
								innerColorStrategy,
								outerColorStrategy));
			} catch (NumberFormatException e) {
				incompleteSpecificationCount++;
			} catch (NullPointerException e) {
				incompleteSpecificationCount++;
			}
		}
		
		if (incompleteSpecificationCount > 0) {
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					incompleteSpecificationCount
					+ " rows in the table did not specify all values needed "
					+ "to make a circle; those rows were skipped.");
		}
		
		if (unscalableValueCount > 0) {
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					unscalableValueCount
					+ " rows in the table specified a value that could not be "
					+ "scaled; those rows were skipped.");
		}
		
		LegendComponent areaLegend =
			createAreaLegend(
				areaValueAttribute,
				areaValueScaling,
				areaValueScaler,
				areaValueScalableRange,
				areaInterpolator,
				areaRange,
				areaNumberFormat);		
		
		LegendComponent innerColorLegend =
			createInnerColorLegend(
				isUsingInnerColor,
				innerColorValueAttribute,
				innerColorScaling,
				innerColorValueScaler,
				innerColorValueScalableRange,
				innerColorQuantityInterpolator,
				innerColorRange,
				innerColorNumberFormat);
		
		LegendComponent outerColorLegend =
			createOuterColorLegend(
				isUsingOuterColor,
				outerColorValueAttribute,
				outerColorScaling,
				outerColorValueScaler,
				outerColorValueScalableRange,
				outerColorQuantityInterpolator,
				outerColorRange,
				outerColorNumberFormat);
		
		postScriptWriter.setCircleAnnotations(
				SUBTITLE,
				circles,
				areaLegend,
				innerColorLegend,
				outerColorLegend);
	}

	private LegendComponent createAreaLegend(
			String attribute,
			String scaling,
			Scaler scaler,
			Range<Double> scalableRange,
			Interpolator<Double> interpolator,
			Range<Double> outputRange,
			String numberFormat) throws AlgorithmExecutionException {
		if (scalableRange == null || scalableRange.isEqual()) {
			return new NullLegendComponent();
		}
		
		LegendComponent areaLegend;
		try {
			/* To determine how to label the middle of the legend component,
			 * we figure the apparent size of the middle circle should be
			 * the mean of the extrema circle sizes.
			 * 
			 * We then un-interpolate and un-scale that point to find what
			 * number in the original data would be represented by that circle.
			 */
			double areaMidrange =
				Averager.mean(outputRange.getMin(), outputRange.getMax());
			double areaMidrangePreimage = interpolator.invert(areaMidrange);			
			double rawMidArea = scaler.invert(areaMidrangePreimage);
			
			// Add circle area legend
			areaLegend =
				new CircleAreaLegend(
						scalableRange,
						scaling,
						rawMidArea,
						areaMidrange,
						outputRange,
						"Area",
						attribute,
						AREA_LEGEND_LOWER_LEFT_X,
						Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS,
						numberFormat);
		} catch (InterpolatorInversionException e) {
			throw new AlgorithmExecutionException(
					"Couldn't create circle area legend: "
					+ e.getMessage(),
					e);
		}
		
		return areaLegend;
	}

	private LegendComponent createInnerColorLegend(
			boolean isUsingInnerColor,
			String attribute,
			String scaling,
			Scaler scaler,
			Range<Double> scalableRange,
			Interpolator<Color> interpolator,
			Range<Color> colorRange, String innerColorNumberFormat) throws AlgorithmExecutionException {
		if (scalableRange == null || scalableRange.isEqual()) {
			return new NullLegendComponent();
		}
				
		LegendComponent innerColorLegend = new NullLegendComponent();		
		if (isUsingInnerColor) {
			try {
				/* To determine how to label the middle of the legend component,
				 * we figure the apparent color of the gradient's middle should be
				 * the mean of the extrema colors.
				 * 
				 * We then un-interpolate and un-scale that color to find what
				 * number in the original data would be represented by that color.
				 */
				Color innerColorMidrange =
					Averager.mean(colorRange.getMin(), colorRange.getMax());
				double innerColorMidrangePreimage;
				innerColorMidrangePreimage =
					interpolator.invert(innerColorMidrange);
				
				double rawMidInnerColorQuantity =
					scaler.invert(innerColorMidrangePreimage);
				
				// Add circle inner color legend
				innerColorLegend =
					new ColorLegend(
							scalableRange,
							scaling,
							rawMidInnerColorQuantity,
							colorRange,
							"Interior Color",
							attribute,
							INNER_COLOR_LEGEND_LOWER_LEFT_X,
							Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS,
							INNER_COLOR_GRADIENT_WIDTH,
							INNER_COLOR_GRADIENT_HEIGHT,
							innerColorNumberFormat);
			} catch (InterpolatorInversionException e) {
				throw new AlgorithmExecutionException(
						"Couldn't create circle inner color legend: "
						+ e.getMessage(),
						e);
			}
		}
		
		return innerColorLegend;
	}

	private LegendComponent createOuterColorLegend(
			boolean isUsingOuterColor,
			String attribute,
			String scaling,
			Scaler scaler,
			Range<Double> scalableRange,
			Interpolator<Color> interpolator,
			Range<Color> colorRange,
			String numberFormat) throws AlgorithmExecutionException {
		if (scalableRange == null || scalableRange.isEqual()) {
			return new NullLegendComponent();
		}
		
		LegendComponent outerColorLegend = new NullLegendComponent();
		if (isUsingOuterColor) {
			try {
				/* To determine how to label the middle of the legend component,
				 * we figure the apparent color of the gradient's middle should be
				 * the mean of the extrema colors.
				 * 
				 * We then un-interpolate and un-scale that color to find what
				 * number in the original data would be represented by that color.
				 */
				Color outerColorMidrange =
					Averager.mean(colorRange.getMin(), colorRange.getMax());
				double outerColorMidrangePreimage =
					interpolator.invert(outerColorMidrange);
				double rawMidOuterColorQuantity =
					scaler.invert(outerColorMidrangePreimage);
				
				// Add circle outer color legend
				outerColorLegend =
					new ColorLegend(
							scalableRange,
							scaling,
							rawMidOuterColorQuantity,
							colorRange,
							"Exterior Color",
							attribute,
							OUTER_COLOR_LEGEND_LOWER_LEFT_X,
							Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS,
							OUTER_COLOR_GRADIENT_WIDTH,
							OUTER_COLOR_GRADIENT_HEIGHT,
							numberFormat);
			} catch(InterpolatorInversionException e) {
				throw new AlgorithmExecutionException(
						"Couldn't create circle inner color legend: "
						+ e.getMessage(),
						e);
			}
		}
		
		return outerColorLegend;
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