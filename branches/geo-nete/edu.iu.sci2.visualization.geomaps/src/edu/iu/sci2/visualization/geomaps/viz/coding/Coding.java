package edu.iu.sci2.visualization.geomaps.viz.coding;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;


public interface Coding<D extends VizDimension> {
	Strategy strategyForValue(double value);
	PostScriptable makeLabeledReference(NumericFormatType numericFormatType) throws LegendCreationException;
	D dimension();
	String columnName();
	Scaling scaling();
}
