package edu.iu.sci2.visualization.geomaps.viz.legend;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;

public interface Legend<D, U> {
	Range<Double> getDataRange();
	Continuum<U> getVizContinuum();
}
