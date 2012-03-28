package edu.iu.sci2.visualization.geomaps.viz.coding;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

/**
 * A coding defines how numerical data in some visualization dimension maps onto a visual
 * representation.  This mapping is indicated by the produced LabeledReference (a legend).
 * 
 * <p>Delegate methods are provided to the underlying VizDimension.Binding.
 * 
 * @param <D>	The visual dimension that this coding maps quantitative data into
 */
public interface Coding<D extends VizDimension> {
	/**
	 * The visual representation strategy for this {@code value}.
	 */
	Strategy strategyForValue(double value);
	/**
	 * A legend that indicates the data -> visualization representation of this coding. 
	 */
	LabeledReference makeLabeledReference(
			PageLayout pageLayout, NumericFormatType numericFormatType) throws LegendCreationException;
	D dimension();
	String columnName();
	Scaling scaling();
}
