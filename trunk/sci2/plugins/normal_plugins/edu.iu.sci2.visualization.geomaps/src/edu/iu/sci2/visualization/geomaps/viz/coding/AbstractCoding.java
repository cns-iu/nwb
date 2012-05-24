package edu.iu.sci2.visualization.geomaps.viz.coding;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;
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
	private final Range<Double> usableRange;
	private final Continuum<V> vizContinuum;
	
	protected AbstractCoding(Binding<D> binding, Range<Double> usableRange, Continuum<V> vizContinuum) {
		this.binding = binding;
		this.usableRange = usableRange;
		this.vizContinuum = vizContinuum;
	}

	
	@Override
	public abstract Strategy strategyForValue(double value);
	@Override
	public abstract LabeledReference makeLabeledReference(PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException;

	public abstract String legendDescription();
	
	
	public VizLegendModel<V> makeVizLegend(NumericFormatType numericFormatType) {
		return new VizLegendModel<V>(
				usableRange,
				vizContinuum,
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
