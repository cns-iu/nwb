package edu.iu.sci2.visualization.geomaps.viz.coding;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.VizLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public abstract class AbstractCoding<D extends VizDimension, V> implements Coding<D> {
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
	public abstract PostScriptable makeLabeledReference(NumericFormatType numericFormatType) throws LegendCreationException;

	
	public VizLegend<V> makeVizLegend(NumericFormatType numericFormatType) {
		return new VizLegend<V>(
				usableRange,
				interpolator.outRange(),
				scaling().toString(),
				"Area",
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
