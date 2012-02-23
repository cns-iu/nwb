package edu.iu.sci2.visualization.geomaps.viz.coding;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public abstract class AbstractCoding<D extends VizDimension> implements Coding<D> {
	private final Binding<D> binding;
	
	/**
	 * @param columnName
	 * @param scaling
	 */
	public AbstractCoding(Binding<D> binding) {
		this.binding = binding;
	}

	@Override
	public abstract Strategy strategyForValue(double value);
	@Override
	public abstract PostScriptable makeLabeledReference(Range<Double> usableRange, Range<Double> scaledRange) throws LegendCreationException;

	
	@Override
	public D getDimension() {
		return binding.getDimension();
	}

	@Override
	public String getColumnName() {
		return binding.getColumnName();
	}

	@Override
	public Scaling getScaling() {
		return binding.getScaling();
	}
}
