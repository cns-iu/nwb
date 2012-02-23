package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Color;
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.List;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.data.interpolation.ColorInterpolator;
import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator1D;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.utility.Averages;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.coding.AbstractCoding;
import edu.iu.sci2.visualization.geomaps.viz.coding.AbstractColorCoding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.AreaLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceCircles;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendComposite;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.VizLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.CircleAreaStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.FillColorStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.StrokeColorStrategy;

public enum CircleDimension implements VizDimension {
	AREA("circleAreaColumnName", "None (uniform sizes)", "circleAreaScaling", null) {
		@Override
		public Strategy defaultStrategy() {
			return CircleAreaStrategy.forArea(Circle.DEFAULT_CIRCLE_AREA);
		}
		
		@Override
		public Binding<CircleDimension> bindingFor(Dictionary<String, Object> parameters) {
			return new Binding<CircleDimension>(this, parameters) {				
				@Override
				public Coding<CircleDimension> codingForDataRange(final Range<Double> dataRange) {
					final Range<Double> vizRange =
							Range.between(0.1, Circle.DEFAULT_CIRCLE_AREA_MAXIMUM); // TODO constants			
					final Interpolator<Double> interpolator = Interpolator1D.between(dataRange, vizRange);
					
					return new AbstractCoding<CircleDimension>(this) {
						@Override
						public Strategy strategyForValue(double value) {
							return CircleAreaStrategy.forArea(interpolator.apply(value));
						}

						@Override
						public PostScriptable makeLabeledReference(
								Range<Double> usableRange,
								Range<Double> scaledRange) throws LegendCreationException {
							// Add circle area legend
							VizLegend<Double> generalLegend =
									new VizLegend<Double>(
											interpolator.inRange(),
											interpolator.outRange(),
											getScaling().toString(),
											"Area",
											getColumnName(),
											NumericFormatType.guessNumberFormat(getColumnName(), usableRange));	
							
							try {
								double midpointOfScaledData = Averages.meanOfDoubles(interpolator.inRange().getPointA(), interpolator.inRange().getPointB());
								double unscaledValueForMidrangeArea = getScaling().invert(midpointOfScaledData);
								double midrangeArea = interpolator.apply(midpointOfScaledData);
								
								AreaLegend areaLegend =
										new AreaLegend(generalLegend, unscaledValueForMidrangeArea, midrangeArea);
								
								PostScriptable labeledCircleSizes = new LabeledReferenceCircles(areaLegend, CircleDimension.AREA_LEGEND_LOWER_LEFT_X, LegendComposite.DEFAULT_LOWER_LEFT_Y_IN_POINTS);
								
								return labeledCircleSizes;
							} catch (ScalingException e) {
								throw new LegendCreationException("TODO Problem formatting numbers for legend.", e);
							}
						}
					};
				}
			};
		}
	},
	OUTER_COLOR("outerColorColumnName", "None (uniform coloring)", "outerColorScaling", "outerColorRange") {
		@Override
		public void addOptionsToAlgorithmParameters(
				DropdownMutator mutator, List<String> numericColumnNames) {
			super.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
			addColorRangeParameter(mutator, getRangeParameterId());
		}

		@Override
		public Strategy defaultStrategy() {
			return StrokeColorStrategy.theDefault();
		}

		@Override
		public Binding<CircleDimension> bindingFor(final Dictionary<String, Object> parameters) {
			return new Binding<CircleDimension>(this, parameters) {
				@Override
				public Coding<CircleDimension> codingForDataRange(Range<Double> dataRange) {
					Range<Color> outRange = Constants.COLOR_RANGES.get(parameters.get(getRangeParameterId()));			
					final Interpolator<Color> interpolator = ColorInterpolator.between(dataRange, outRange);
					
					return new AbstractColorCoding<CircleDimension>(this, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return StrokeColorStrategy.forColor(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Exterior Color";
						}

						@Override
						public double lowerLeftX() {
							return CircleDimension.OUTER_COLOR_LEGEND_LOWER_LEFT_X;
						}

						@Override
						public double lowerLeftY() {
							return LegendComposite.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
						}

						@Override
						public Color defaultColor() {
							return Circle.DEFAULT_OUTLINE_COLOR; // TODO explain
						}
					};
				}
			};
		}
	},
	INNER_COLOR("innerColorColumnName", "None (uniform coloring)", "innerColorScaling", "innerColorRange") {
		@Override
		public void addOptionsToAlgorithmParameters(DropdownMutator mutator,
				List<String> numericColumnNames) {
			super.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
			addColorRangeParameter(mutator, getRangeParameterId());
		}

		@Override
		public Strategy defaultStrategy() {
			return FillColorStrategy.forColor(null);
		}
		
		@Override
		public Binding<CircleDimension> bindingFor(final Dictionary<String, Object> parameters) {
			return new Binding<CircleDimension>(this, parameters) {
				@Override
				public Coding<CircleDimension> codingForDataRange(Range<Double> dataRange) {
					Range<Color> outRange = Constants.COLOR_RANGES.get(parameters.get(getRangeParameterId()));			
					final Interpolator<Color> interpolator = ColorInterpolator.between(dataRange, outRange);
					
					return new AbstractColorCoding<CircleDimension>(this, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return FillColorStrategy.forColor(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Interior Color";
						}

						@Override
						public double lowerLeftX() {
							return CircleDimension.INNER_COLOR_LEGEND_LOWER_LEFT_X;
						}

						@Override
						public double lowerLeftY() {
							return LegendComposite.DEFAULT_LOWER_LEFT_Y_IN_POINTS;
						}

						@Override
						public Color defaultColor() {
							return null; // TODO Explain
						}
					};
				}
			};
		}
	};
	
	public static final double OUTER_COLOR_LEGEND_LOWER_LEFT_X =
			LegendComposite.DEFAULT_LOWER_LEFT_X_IN_POINTS
			+ ((1.0 * LegendComposite.DEFAULT_WIDTH_IN_POINTS) / EnumSet.allOf(CircleDimension.class).size());
	public static final int INNER_COLOR_GRADIENT_HEIGHT = 10;
	public static final double INNER_COLOR_GRADIENT_WIDTH =
			0.8 * (LegendComposite.DEFAULT_WIDTH_IN_POINTS / EnumSet.allOf(CircleDimension.class).size());
	public static final double INNER_COLOR_LEGEND_LOWER_LEFT_X =
			LegendComposite.DEFAULT_LOWER_LEFT_X_IN_POINTS;
	// Page layout sizes and dimensions
	public static final double AREA_LEGEND_LOWER_LEFT_X =
			LegendComposite.DEFAULT_LOWER_LEFT_X_IN_POINTS
			+ ((2.0 * LegendComposite.DEFAULT_WIDTH_IN_POINTS) / EnumSet.allOf(CircleDimension.class).size());
	public static final int OUTER_COLOR_GRADIENT_HEIGHT = INNER_COLOR_GRADIENT_HEIGHT;
	public static final double OUTER_COLOR_GRADIENT_WIDTH = INNER_COLOR_GRADIENT_WIDTH;
	
	private String columnNameParameterId;
	private String columnNameParameterDisablingToken;
	private String scalingParameterId;
	private String rangeParameterId;

	/**
	 * @param columnNameParameterId
	 * @param columnNameParameterDisablingToken
	 * @param scalingParameterId
	 */
	private CircleDimension(String columnNameParameterId, String columnNameParameterDisablingToken,
			String scalingParameterId, String rangeParameterId) {
		this.columnNameParameterId = columnNameParameterId;
		this.columnNameParameterDisablingToken = columnNameParameterDisablingToken;
		this.scalingParameterId = scalingParameterId;
		this.rangeParameterId = rangeParameterId;
	}
	
	@Override
	public abstract Binding<CircleDimension> bindingFor(Dictionary<String, Object> parameters);
	@Override
	public abstract Strategy defaultStrategy();

	@Override
	public String getColumnNameParameterId() {
		return columnNameParameterId;
	}

	@Override
	public String getColumnNameParameterDisablingToken() {
		return columnNameParameterDisablingToken;
	}

	@Override
	public String getScalingParameterId() {
		return scalingParameterId;
	}

	@Override
	public String getRangeParameterId() {
		return rangeParameterId;
	}

	@Override
	public void addOptionsToAlgorithmParameters(DropdownMutator mutator, List<String> numericColumnNames) {
		List<String> options = Lists.newArrayList(numericColumnNames);

		options.add(getColumnNameParameterDisablingToken());
		mutator.add(getColumnNameParameterId(), options, getColumnNameParameterDisablingToken());
		
		mutator.add(
				getScalingParameterId(),
				Collections2.transform(EnumSet.allOf(Scaling.class), Functions.toStringFunction()));
	}

	public static void addColorRangeParameter(DropdownMutator mutator, String parameterId) {
		mutator.add(parameterId, Lists.newArrayList(Constants.COLOR_RANGES.keySet()));
	}
}