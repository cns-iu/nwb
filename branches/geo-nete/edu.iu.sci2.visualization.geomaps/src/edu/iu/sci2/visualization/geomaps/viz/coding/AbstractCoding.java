package edu.iu.sci2.visualization.geomaps.viz.coding;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.VizLegendModel;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public abstract class AbstractCoding<D extends Enum<D> & VizDimension, V> implements Coding<D> {
	private final Binding<D> binding;
	private Range<Double> usableRange;
	protected final Interpolator<V> interpolator;
	
	public AbstractCoding(Binding<D> binding, Range<Double> usableRange, Interpolator<V> interpolator) {
		this.binding = binding;
		this.usableRange = usableRange;
		this.interpolator = interpolator;
	}

	
	@Override
	public abstract Strategy strategyForValue(double value);
	@Override
	public abstract LabeledReference makeLabeledReference(PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException;

	public abstract String legendDescription();
	
	
	public VizLegendModel<V> makeVizLegend(NumericFormatType numericFormatType) {
		return new VizLegendModel<V>(
				usableRange,
				interpolator.getOutContinuum(),
				scaling().toString(),
				legendDescription(),
				columnName(),
				numericFormatType);
	}
	
	@Override
	public D dimension() {
		return binding.dimension();
	}

	@Override
	public String columnName() {
		return binding.columnName();
	}

	@Override
	public Scaling scaling() {
		return binding.scaling();
	}
}
