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
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.coding.AbstractColorCoding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendComposite;
import edu.iu.sci2.visualization.geomaps.viz.strategy.FillColorStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public enum FeatureDimension implements VizDimension {
	REGION_COLOR("featureColorColumnName", "None (uniform coloring)", "featureColorScaling", "featureColorRange") {
		@Override
		public void addOptionsToAlgorithmParameters(
				DropdownMutator mutator, List<String> numericColumnNames) {
			super.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
			CircleDimension.addColorRangeParameter(mutator, getRangeParameterId());
		}
		
		@Override
		public Strategy defaultStrategy() {
			return FillColorStrategy.forColor(Color.GRAY);
		}

		@Override
		public Binding<FeatureDimension> bindingFor(final Dictionary<String, Object> parameters) {
			return new Binding<FeatureDimension>(this, parameters) {
				@Override
				public Coding<FeatureDimension> codingForDataRange(Range<Double> dataRange) {
					Range<Color> outRange = Constants.COLOR_RANGES.get(parameters.get(getRangeParameterId()));			
					final Interpolator<Color> interpolator = ColorInterpolator.between(dataRange, outRange);
					
					return new AbstractColorCoding<FeatureDimension>(this, interpolator) {
						@Override
						public Strategy strategyForValue(double value) {
							return FillColorStrategy.forColor(interpolator.apply(value));
						}

						@Override
						public String legendDescription() {
							return "Region Color";
						}

						@Override
						public double lowerLeftX() {
							return RegionAnnotationMode.COLOR_GRADIENT_LOWER_LEFT_X;
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
	
	private String columnNameParameterId;
	private String columnNameParameterDisablingToken;
	private String scalingParameterId;
	private String rangeParameterId;

	/**
	 * @param columnNameParameterId
	 * @param columnNameParameterDisablingToken
	 * @param scalingParameterId
	 * @param rangeParameterId
	 */
	private FeatureDimension(String columnNameParameterId,
			String columnNameParameterDisablingToken, String scalingParameterId,
			String rangeParameterId) {
		this.columnNameParameterId = columnNameParameterId;
		this.columnNameParameterDisablingToken = columnNameParameterDisablingToken;
		this.scalingParameterId = scalingParameterId;
		this.rangeParameterId = rangeParameterId;
	}
	
	
	@Override
	public abstract Strategy defaultStrategy();
	@Override
	public abstract Binding<FeatureDimension> bindingFor(Dictionary<String, Object> parameters);	

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
}
