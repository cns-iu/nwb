package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Color;
import java.awt.geom.Point2D;
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
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceCircles;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.VizLegendModel;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
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
				public Coding<CircleDimension> codingForDataRange(final Range<Double> usableRange, final Range<Double> dataRange) {
					Range<Double> vizRange = Circle.DEFAULT_CIRCLE_AREA_RANGE;
					// TODO Don't force data min = 0, instead use actual data min and draw actual corresponding circle
					Range<Double> usableRangeFromZero = Range.between(0.0, usableRange.getPointB()); // TODO !?
					Range<Double> dataRangeFromZero = Range.between(0.0, dataRange.getPointB()); // TODO !?
					final Interpolator<Double> interpolator = Interpolator1D.between(dataRangeFromZero, vizRange);
					
					return new AbstractCoding<CircleDimension, Double>(this, usableRangeFromZero, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return CircleAreaStrategy.forArea(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Area";
						}
						
						@Override
						public LabeledReference makeLabeledReference(PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException {
							VizLegendModel<Double> generalLegend = makeVizLegend(numericFormatType);
							
							try {
								double midpointOfScaledData =
										Averages.meanOfDoubles(
												interpolator.getInRange().getPointA(),
												interpolator.getInRange().getPointB());
								double unscaledValueForMidrangeArea = scaling().invert(midpointOfScaledData);
								double midrangeArea = interpolator.apply(midpointOfScaledData);

								AreaLegend areaLegend =
										new AreaLegend(generalLegend, unscaledValueForMidrangeArea, midrangeArea);
								
								return new LabeledReferenceCircles(
										areaLegend,
										new Point2D.Double(
												pageLayout.legendariumLowerLeft().getX()
												+ ((2.0 * pageLayout.legendariumDimensions().getWidth()) / EnumSet.allOf(CircleDimension.class).size()),
												pageLayout.legendLowerLeft().getY()));
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
				public Coding<CircleDimension> codingForDataRange(Range<Double> usableRange, Range<Double> dataRange) {
					Range<Color> outRange = AbstractColorCoding.COLOR_RANGES.get(parameters.get(getRangeParameterId()));			
					final Interpolator<Color> interpolator = ColorInterpolator.between(dataRange, outRange);
					
					return new AbstractColorCoding<CircleDimension>(this, usableRange, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return StrokeColorStrategy.forColor(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Exterior Color";
						}

						@Override
						public Point2D.Double lowerLeft(PageLayout pageLayout) {
							return new Point2D.Double(
									pageLayout.legendariumLowerLeft().getX()
									+ ((1.0 * pageLayout.legendariumDimensions().getWidth()) / EnumSet.allOf(CircleDimension.class).size()),
									pageLayout.legendLowerLeft().getY());
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
				public Coding<CircleDimension> codingForDataRange(Range<Double> usableRange, Range<Double> dataRange) {
					Range<Color> outRange = AbstractColorCoding.COLOR_RANGES.get(parameters.get(getRangeParameterId()));			
					final Interpolator<Color> interpolator = ColorInterpolator.between(dataRange, outRange);
					
					return new AbstractColorCoding<CircleDimension>(this, usableRange, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return FillColorStrategy.forColor(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Interior Color";
						}

						@Override
						public Point2D.Double lowerLeft(PageLayout pageLayout) {
							return new Point2D.Double(
									pageLayout.legendariumLowerLeft().getX()
									+ ((0.0 * pageLayout.legendariumDimensions().getWidth()) / EnumSet.allOf(CircleDimension.class).size()),
									pageLayout.legendLowerLeft().getY());
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
		mutator.add(parameterId, Lists.newArrayList(AbstractColorCoding.COLOR_RANGES.keySet()));
	}
}