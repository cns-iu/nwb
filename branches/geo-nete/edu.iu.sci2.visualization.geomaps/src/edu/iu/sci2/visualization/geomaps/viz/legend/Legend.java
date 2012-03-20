package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;

public interface Legend<D, U> {
	Continuum<D> getDataRange();
	Continuum<U> getVizRange();
}
