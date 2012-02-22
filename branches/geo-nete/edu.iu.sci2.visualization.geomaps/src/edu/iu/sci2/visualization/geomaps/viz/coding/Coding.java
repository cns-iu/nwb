package edu.iu.sci2.visualization.geomaps.viz.coding;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;


public interface Coding<D extends VizDimension> {
	Strategy strategyForValue(double value);
	PostScriptable makeLabeledReference(Range<Double> usableRange, Range<Double> scaledRange) throws LegendCreationException;
	D getDimension();
	String getColumnName();
	Scaling getScaling();
}
