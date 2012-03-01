package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Range;

public interface Legend<D, U> {
	Range<D> getDataRange();
	Range<U> getVizRange();
}
